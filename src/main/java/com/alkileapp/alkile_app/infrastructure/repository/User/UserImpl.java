package com.alkileapp.alkile_app.infrastructure.repository.User;

import com.alkileapp.alkile_app.application.services.IUserService;
import com.alkileapp.alkile_app.domain.entities.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserImpl implements IUserService {

    private final UserRepository userRepository;

    public UserImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> findOneByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}