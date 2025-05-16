package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.IUserService;
import com.alkileapp.alkile_app.domain.dto.UserDto;
import com.alkileapp.alkile_app.domain.entities.Role;
import com.alkileapp.alkile_app.domain.entities.User;
import com.alkileapp.alkile_app.infrastructure.repository.Role.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(IUserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<User> users = userService.findAll();
        List<UserDto> userDtos = users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        return userOptional
                .map(user -> ResponseEntity.ok(convertToDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        if (userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().build();
        }

        User user = convertToEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedUser));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        // Verificar si el email ya está en uso por otro usuario
        Optional<User> existingUserWithEmail = userService.findByEmail(userDto.getEmail());
        if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getId().equals(id)) {
            return ResponseEntity.badRequest().build();
        }

        User user = convertToEntity(userDto);
        user.setId(id);
        
        // Si se proporciona una nueva contraseña, codificarla
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            // Mantener la contraseña existente
            userService.findById(id).ifPresent(existingUser -> 
                user.setPassword(existingUser.getPassword())
            );
        }

        User updatedUser = userService.update(user);
        return ResponseEntity.ok(convertToDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private UserDto convertToDTO(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setAddress(user.getAddress());
        dto.setRegistrationDate(user.getRegistrationDate());
        dto.setActive(user.isActive());
        
        // Convertir roles a strings
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
        dto.setRoles(roleNames);
        
        return dto;
    }

    private User convertToEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        user.setActive(dto.isActive());
        
        // Convertir nombres de roles a entidades Role
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : dto.getRoles()) {
                roleRepository.findByName(roleName).ifPresent(roles::add);
            }
            user.setRoles(roles);
        }
        
        return user;
    }
}