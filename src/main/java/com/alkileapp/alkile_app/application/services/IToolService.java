package com.alkileapp.alkile_app.application.services;

import com.alkileapp.alkile_app.domain.entities.Tool;
import java.util.List;
import java.util.Optional;

public interface IToolService {
    List<Tool> findAll();
    Optional<Tool> findById(Long id);
    Tool save(Tool tool);
    void deleteById(Long id);
    boolean existsById(Long id);
}