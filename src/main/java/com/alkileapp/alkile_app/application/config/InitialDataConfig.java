package com.alkileapp.alkile_app.application.config;

import com.alkileapp.alkile_app.domain.entities.Role;
import com.alkileapp.alkile_app.domain.entities.User;
import com.alkileapp.alkile_app.infrastructure.repository.Role.RoleRepository;
import com.alkileapp.alkile_app.application.services.IUserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Collections;
import java.util.Optional;

@Configuration
public class InitialDataConfig {

    @Bean
    @Order(1)
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                Role adminRole = new Role("ADMIN","Administrador del sistema");
                roleRepository.save(adminRole);
            }

            if (roleRepository.findByName("CUSTOMER").isEmpty()) {
                Role userRole = new Role("CUSTOMER", "Usuario Cliente");
                roleRepository.save(userRole);
            }

            if (roleRepository.findByName("SUPPLIER").isEmpty()) {
                Role userRole = new Role("SUPPLIER", "Usuario Proveedor");
                roleRepository.save(userRole);
            }
        };
    }
    
    @Bean
    @Order(2)
    CommandLineRunner initAdminUser(IUserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userService.findOneByUsername("admin").isEmpty()) {
                Optional<Role> adminRoleOpt = roleRepository.findByName("ADMIN");
                if (adminRoleOpt.isPresent()) {
                    Role adminRole = adminRoleOpt.get();
                    
                    User adminUser = new User();
                    adminUser.setUsername("admin");
                    adminUser.setEmail("admin@alkileapp.com");
                    adminUser.setPassword(passwordEncoder.encode("123456789"));
                    adminUser.setName("Administrador");
                    adminUser.setAddress("Dirección Administrativa");
                    adminUser.setPhone("0000000000");
                    adminUser.setActive(true);
                    adminUser.setRoles(Collections.singleton(adminRole));
                    
                    userService.save(adminUser);
                    
                    System.out.println("Usuario administrador creado con éxito.");
                }
            } else {
                System.out.println("El usuario administrador ya existe.");
            }
        };
    }

    @Bean
    @Order(3)
    CommandLineRunner initSupplierUser(IUserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userService.findOneByUsername("prove").isEmpty()) {
                Optional<Role> adminRoleOpt = roleRepository.findByName("SUPPLIER");
                if (adminRoleOpt.isPresent()) {
                    Role adminRole = adminRoleOpt.get();
                    
                    User adminUser = new User();
                    adminUser.setUsername("supplier");
                    adminUser.setEmail("supplier@alkileapp.com");
                    adminUser.setPassword(passwordEncoder.encode("123456789"));
                    adminUser.setName("Proveedor");
                    adminUser.setAddress("Dirección Proveedor");
                    adminUser.setPhone("1234567890");
                    adminUser.setActive(true);
                    adminUser.setRoles(Collections.singleton(adminRole));
                    
                    userService.save(adminUser);
                    
                    System.out.println("Usuario Proveedor creado con éxito.");
                }
            } else {
                System.out.println("El usuario proveedor ya existe.");
            }
        };
    }
}