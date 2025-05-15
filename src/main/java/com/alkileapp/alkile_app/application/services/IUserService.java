package com.alkileapp.alkile_app.application.services;


import java.util.Optional;

import com.alkileapp.alkile_app.domain.dto.UserDto;
import com.alkileapp.alkile_app.domain.entities.User;
public interface IUserService {
    User registrOneCustomer(UserDto newUser);

    Optional<User> findOneByUsername(String username);
}
