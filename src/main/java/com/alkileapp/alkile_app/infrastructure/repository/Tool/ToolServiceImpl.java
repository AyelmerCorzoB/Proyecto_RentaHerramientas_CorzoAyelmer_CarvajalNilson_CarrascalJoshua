package com.alkileapp.alkile_app.infrastructure.repository.Tool;

import com.alkileapp.alkile_app.application.services.IToolService;
import com.alkileapp.alkile_app.domain.entities.Tool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ToolServiceImpl implements IToolService {

    private final ToolRepository toolRepository;

    public ToolServiceImpl(ToolRepository toolRepository) {
        this.toolRepository = toolRepository;
    }

    @Override
    public List<Tool> findAll() {
        return toolRepository.findAll();
    }

    @Override
    public Optional<Tool> findById(Long id) {
        return toolRepository.findById(id);
    }

    @Override
    public Tool save(Tool tool) {
        return toolRepository.save(tool);
    }

    @Override
    public void deleteById(Long id) {
        toolRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return toolRepository.existsById(id);
    }
}
