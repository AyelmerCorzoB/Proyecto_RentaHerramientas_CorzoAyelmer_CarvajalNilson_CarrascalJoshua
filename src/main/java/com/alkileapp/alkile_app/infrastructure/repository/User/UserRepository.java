package com.alkileapp.alkile_app.infrastructure.repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alkileapp.alkile_app.domain.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    User findByEmail(String email);
}