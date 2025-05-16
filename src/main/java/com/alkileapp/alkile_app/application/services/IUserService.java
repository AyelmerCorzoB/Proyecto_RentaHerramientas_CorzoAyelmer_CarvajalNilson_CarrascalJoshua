package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.domain.entities.User;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    Optional<User> findOneByUsername(String username);
    boolean existsByEmail(String email);
    User save(User user);
    
    List<User> findAll();
    Optional<User> findById(Long id);
    void deleteById(Long id);
    User update(User user);
    Optional<User> findByEmail(String email);
    boolean existsById(Long id);
}