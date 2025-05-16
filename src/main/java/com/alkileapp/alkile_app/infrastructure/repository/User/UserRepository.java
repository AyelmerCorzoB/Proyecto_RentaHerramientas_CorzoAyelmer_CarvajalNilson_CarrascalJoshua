package com.alkileapp.alkile_app.infrastructure.repository.User;

import com.alkileapp.alkile_app.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}