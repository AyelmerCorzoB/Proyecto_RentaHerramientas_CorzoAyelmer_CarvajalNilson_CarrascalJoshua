package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.IToolService;
import com.alkileapp.alkile_app.domain.dto.SupplierDto;
import com.alkileapp.alkile_app.domain.dto.ToolDto;
import com.alkileapp.alkile_app.domain.entities.Tool;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tools")
public class ToolController {

    private final IToolService toolService;

    public ToolController(IToolService toolService) {
        this.toolService = toolService;
    }

    @GetMapping
    public ResponseEntity<List<ToolDto>> getAllTools() {
        List<ToolDto> tools = toolService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tools);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToolDto> getById(@PathVariable Long id) {
        return toolService.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tool> create(@RequestBody Tool tool) {
        return ResponseEntity.ok(toolService.save(tool));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ToolDto> update(@PathVariable Long id, @RequestBody Tool tool) {
        if (!toolService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        tool.setId(id);
        Tool updatedTool = toolService.save(tool);
        return ResponseEntity.ok(convertToDto(updatedTool));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!toolService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        toolService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ToolDto convertToDto(Tool tool) {
        return new ToolDto(
                tool.getId(),
                tool.getName(),
                tool.getDescription(),
                tool.getDailyCost(),
                tool.getStock(),
                tool.getCategory().getId(),
                new SupplierDto(
                        tool.getSupplier().getId(),
                        tool.getSupplier().getTaxId(),
                        tool.getSupplier().getCompany(),
                        tool.getSupplier().getRating(),
                        tool.getSupplier().getUser().getId()
                )
        );
    }
}