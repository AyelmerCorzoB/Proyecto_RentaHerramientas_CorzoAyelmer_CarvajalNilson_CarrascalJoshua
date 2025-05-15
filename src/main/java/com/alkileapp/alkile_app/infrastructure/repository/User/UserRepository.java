package com.alkileapp.alkile_app.infrastructure.repository.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alkileapp.alkile_app.domain.entities.User;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
}
