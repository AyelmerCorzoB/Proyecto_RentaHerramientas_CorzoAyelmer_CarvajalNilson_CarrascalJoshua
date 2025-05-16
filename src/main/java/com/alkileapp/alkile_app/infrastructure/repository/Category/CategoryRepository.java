package com.alkileapp.alkile_app.infrastructure.repository.Category;

import com.alkileapp.alkile_app.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
