package com.alkileapp.alkile_app.infrastructure.repository.User;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alkileapp.alkile_app.application.services.IUserService;
import com.alkileapp.alkile_app.domain.entities.Role;
import com.alkileapp.alkile_app.domain.entities.User;
import com.alkileapp.alkile_app.infrastructure.repository.Role.RoleRepository;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        Role roleUser = roleRepository.findByRoleName("USER");
        List<Role> roles = new ArrayList<>();
        if (roleUser != null) {
            roles.add(roleUser);
        }

        if (user.isAdmin()) {
            Role roleAdmin = roleRepository.findByRoleName("ADMIN");
            if (roleAdmin != null) {
                roles.add(roleAdmin);
            }
        }

        user.setRoles(new java.util.HashSet<>(roles));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}