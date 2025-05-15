package com.alkileapp.alkile_app.application.services;
import java.util.Optional;

import com.alkileapp.alkile_app.domain.entities.Role;

public interface IRoleService {
    Optional<Role> findDefaultRole();
}
