package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.application.security.JwtService;
import com.alkileapp.alkile_app.domain.dto.AuthRequest;
import com.alkileapp.alkile_app.domain.dto.AuthResponse;
import com.alkileapp.alkile_app.domain.dto.RegisterRequest;
import com.alkileapp.alkile_app.domain.entities.Role;
import com.alkileapp.alkile_app.domain.entities.User;
import com.alkileapp.alkile_app.infrastructure.repository.Role.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final IUserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            IUserService userService,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
    }

    public AuthResponse login(String username, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String token = jwtService.generateToken(userDetails);

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .orElseThrow(() -> new RuntimeException("User has no role assigned"));

        return new AuthResponse(token, role);
    }

    public AuthResponse register(RegisterRequest request) {
        if (userService.findOneByUsername(request.username()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        if (userService.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        // Buscar rol por defecto
        Role userRole = roleRepository.findByName("CUSTOMER")  // <-- Rol por defecto
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        // Crear nuevo usuario
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

        String role = userRole.getName();

        return new AuthResponse(jwtToken, role);
    }
}