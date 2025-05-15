package com.alkileapp.alkile_app.infrastructure.repository.User;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alkileapp.alkile_app.application.services.IRoleService;
import com.alkileapp.alkile_app.application.services.IUserService;
import com.alkileapp.alkile_app.domain.dto.UserDto;
import com.alkileapp.alkile_app.domain.entities.Role;
import com.alkileapp.alkile_app.domain.entities.User;
import com.alkileapp.alkile_app.domain.exceptions.ObjectNotFoundException;


@Service
public class UserImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IRoleService roleService;

    @Override
    public User registrOneCustomer(UserDto newUser) {
        validatePassword(newUser);

        User user = new User();
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setFirstName(newUser.getUsername());
        user.setFirstName(newUser.getName());
        Role defaultRole = roleService.findDefaultRole()
                        .orElseThrow(() -> new ObjectNotFoundException("Role not found. Default Role"));
        user.setRole(new HashSet<>(defaultRole));

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    private void validatePassword(UserDto dto) {

        if(!StringUtils.hasText(dto.getPassword()) || !StringUtils.hasText(dto.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }

        if(!dto.getPassword().equals(dto.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }

    }

}
