package com.alkileapp.alkile_app.application.services;

import java.util.List;
import com.alkileapp.alkile_app.domain.entities.User;

public interface IUserService {
    List<User> findAll();
    User save(User user);
    User findById(Long id);
    void deleteById(Long id);
    boolean existsByEmail(String email);
}