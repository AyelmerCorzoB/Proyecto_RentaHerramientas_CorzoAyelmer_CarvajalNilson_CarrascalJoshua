package com.alkileapp.alkile_app.infrastructure.repository.User;

import com.alkileapp.alkile_app.application.services.IUserService;
import com.alkileapp.alkile_app.domain.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
    
    @Override
    public List<User> findAll() {
        return userRepository.findAllWithRoles();
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public User update(User user) {
        return userRepository.save(user);
    }
    
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}