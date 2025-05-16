package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.domain.dto.AuthRequest;
import com.alkileapp.alkile_app.domain.dto.AuthResponse;
import com.alkileapp.alkile_app.domain.dto.RegisterRequest;
import com.alkileapp.alkile_app.domain.entities.Role;
import com.alkileapp.alkile_app.domain.entities.User;
import com.alkileapp.alkile_app.infrastructure.repository.Role.RoleRepository;
import com.alkileapp.alkile_app.application.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AuthService(AuthenticationManager authenticationManager, 
                     JwtService jwtService, 
                     IUserService userService,
                     PasswordEncoder passwordEncoder,
                     RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        
        User user = userService.findOneByUsername(request.username())
                .orElseThrow();
        
        String jwtToken = jwtService.generateToken(user);
        
        return new AuthResponse(jwtToken);
    }

    public AuthResponse register(RegisterRequest request) {
       
        if (userService.findOneByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (userService.existsByEmail(request.email())) {
            throw new RuntimeException("Email already in use");
        }

        Role userRole = roleRepository.findByName("ADMIN")
    .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setAddress(request.address());
        user.setPhone(request.phone());
        user.setActive(true);
        user.setRoles(Collections.singleton(userRole));

        User savedUser = userService.save(user);

        String jwtToken = jwtService.generateToken(savedUser);

        return new AuthResponse(jwtToken);
    }
}