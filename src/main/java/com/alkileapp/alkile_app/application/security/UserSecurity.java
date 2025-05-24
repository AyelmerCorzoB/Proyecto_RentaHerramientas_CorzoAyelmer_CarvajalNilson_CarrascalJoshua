package com.alkileapp.alkile_app.application.security;

import com.alkileapp.alkile_app.domain.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurity {

    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return ((User) principal).getId().equals(userId);
        } else if (principal instanceof org.springframework.security.core.userdetails.User) {
            return false;
        }
        
        return false;
    }
}