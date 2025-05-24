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
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.Optional;

@Configuration
public class InitialDataConfig {

    @Bean
    @Order(1)
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                Role adminRole = new Role("ADMIN", "Administrador del sistema");
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
    CommandLineRunner initAdminUser(IUserService userService, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
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
    CommandLineRunner initSupplierUser(IUserService userService, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (userService.findOneByUsername("prove").isEmpty()) {
                Optional<Role> adminRoleOpt = roleRepository.findByName("SUPPLIER");
                if (adminRoleOpt.isPresent()) {
                    Role adminRole = adminRoleOpt.get();

                    User supplierUser = new User();
                    supplierUser.setUsername("supplier");
                    supplierUser.setEmail("supplier@alkileapp.com");
                    supplierUser.setPassword(passwordEncoder.encode("123456789"));
                    supplierUser.setName("Proveedor");
                    supplierUser.setAddress("Dirección Proveedor");
                    supplierUser.setPhone("1234567890");
                    supplierUser.setActive(true);
                    supplierUser.setRoles(Collections.singleton(adminRole));

                    userService.save(supplierUser);

                    System.out.println("Usuario Proveedor creado con éxito.");
                }
            } else {
                System.out.println("El usuario proveedor ya existe.");
            }
        };
    }
    @Bean
    @Order(2)
    CommandLineRunner initCustomerUser(IUserService userService, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (userService.findOneByUsername("customer").isEmpty()) {
                Optional<Role> customerRoleOpt = roleRepository.findByName("CUSTOMER");
                if (customerRoleOpt.isPresent()) {
                    Role customerRole = customerRoleOpt.get();

                    User customerUser = new User();
                    customerUser.setUsername("customer");
                    customerUser.setEmail("customer@alkileapp.com");
                    customerUser.setPassword(passwordEncoder.encode("123456789"));
                    customerUser.setName("Customer");
                    customerUser.setAddress("Dirección Customer");
                    customerUser.setPhone("0000000000");
                    customerUser.setActive(true);
                    customerUser.setRoles(Collections.singleton(customerRole));

                    userService.save(customerUser);

                    System.out.println("Usuario customer creado con éxito.");
                }
            } else {
                System.out.println("El usuario customer ya existe.");
            }
        };
    }

    @Bean
    @Order(4)
    CommandLineRunner initDatabaseData(DataSource dataSource) {
        return args -> {
            System.out.println("Ejecutando script SQL...");
            executeSqlScript(dataSource);
            System.out.println("Se inserto todo correctamente...");
        };
    }

    private void executeSqlScript(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("data.sql"));
        populator.setContinueOnError(true);
        DatabasePopulatorUtils.execute(populator, dataSource);
    }
}