package com.alkileapp.alkile_app.application.config;

import com.alkileapp.alkile_app.domain.entities.Role;
import com.alkileapp.alkile_app.infrastructure.repository.Role.RoleRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitialDataConfig {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("USER").isEmpty()) {
                Role userRole = new Role("USER");
                roleRepository.save(userRole);
            }
            
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                Role adminRole = new Role("ADMIN");
                roleRepository.save(adminRole);
            }
        };
    }
}