package com.alkileapp.alkile_app.infrastructure.repository.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alkileapp.alkile_app.domain.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}