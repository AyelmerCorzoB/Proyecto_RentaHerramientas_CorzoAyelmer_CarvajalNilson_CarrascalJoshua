package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.domain.entities.User;
import java.util.Optional;

public interface IUserService {
    Optional<User> findOneByUsername(String username);
    boolean existsByEmail(String email);
    User save(User user);
}