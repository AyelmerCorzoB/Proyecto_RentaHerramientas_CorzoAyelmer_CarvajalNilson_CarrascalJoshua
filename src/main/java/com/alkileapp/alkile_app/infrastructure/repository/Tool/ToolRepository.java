package com.alkileapp.alkile_app.infrastructure.repository.Tool;

import com.alkileapp.alkile_app.domain.entities.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolRepository extends JpaRepository<Tool, Long> {
}
