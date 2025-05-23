
package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.IReservationService;
import com.alkileapp.alkile_app.application.services.IToolService;
import com.alkileapp.alkile_app.domain.dto.SupplierDto;
import com.alkileapp.alkile_app.domain.dto.ToolDto;
import com.alkileapp.alkile_app.domain.entities.Category;
import com.alkileapp.alkile_app.domain.entities.Tool;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alkile/tools")

public class ToolController {

  private final IToolService toolService;

  private final IReservationService reservationService;

  public ToolController(IToolService toolService, IReservationService reservationService) {
    this.toolService = toolService;
    this.reservationService = reservationService;
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
  public ResponseEntity<?> deleteTool(@PathVariable Long id) {
    try {
      reservationService.deleteById(id);
      toolService.deleteById(id);
      return ResponseEntity.noContent().build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.notFound().build();
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(Map.of("message", "No se puede eliminar la herramienta: " + e.getMessage()));
    }
  }

  private ToolDto convertToDto(Tool tool) {
    Long categoryId = Optional.ofNullable(tool.getCategory())
        .map(Category::getId)
        .orElse(null);
    return new ToolDto(
        tool.getId(),
        tool.getName(),
        tool.getDescription(),
        tool.getDailyCost(),
        tool.getStock(),
        categoryId,

        new SupplierDto(
            tool.getSupplier().getId(),
            tool.getSupplier().getTaxId(),
            tool.getSupplier().getCompany(),
            tool.getSupplier().getRating(),
            tool.getSupplier().getUser().getId()));
  }
}