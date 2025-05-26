package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.IReservationService;
import com.alkileapp.alkile_app.application.services.IToolService;
import com.alkileapp.alkile_app.application.services.IUserService;
import com.alkileapp.alkile_app.domain.dto.ToolDto;
import com.alkileapp.alkile_app.domain.entities.Category;
import com.alkileapp.alkile_app.domain.entities.Tool;
import com.alkileapp.alkile_app.domain.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alkile/tools")
public class ToolController {

  private final IToolService toolService;
  private final IReservationService reservationService;
  private final String UPLOAD_DIR = "uploads/";
  private final IUserService supplierService;

  public ToolController(IToolService toolService, IReservationService reservationService,
      IUserService supplierService) {
    this.toolService = toolService;
    this.reservationService = reservationService;
    this.supplierService = supplierService;
    try {
      Files.createDirectories(Paths.get(UPLOAD_DIR));
    } catch (IOException e) {
      throw new RuntimeException("No se pudo crear el directorio de uploads", e);
    }
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

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> createToolWithImages(
      @RequestPart("tool") String toolJson,
      @RequestPart(value = "images", required = false) List<MultipartFile> images) {

    try {
      Tool tool = new ObjectMapper().readValue(toolJson, Tool.class);

      if (tool.getSupplier() != null && tool.getSupplier().getId() != null) {
        Optional<User> supplierOpt = supplierService.findById(tool.getSupplier().getId());
        if (supplierOpt.isEmpty() || supplierOpt.get().getName() == null) {
          return ResponseEntity.badRequest()
              .body(Map.of("message", "El proveedor seleccionado no es válido o no tiene usuario asociado"));
        }

        tool.setSupplier(supplierOpt.get());
      }

      if (images != null && !images.isEmpty()) {
        String imagePaths = images.stream()
            .map(this::uploadImage)
            .collect(Collectors.joining(","));
        tool.setImageUrl(imagePaths);
      }

      Tool savedTool = toolService.save(tool);
      return ResponseEntity.ok(convertToDto(savedTool));

    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(Map.of("message", "Error al crear herramienta: " + e.getMessage()));
    }
  }

  @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> updateToolWithImages(
      @PathVariable Long id,
      @RequestPart("tool") String toolJson,
      @RequestPart(value = "images", required = false) List<MultipartFile> images) {

    if (!toolService.existsById(id)) {
      return ResponseEntity.notFound().build();
    }

    try {
      Tool tool = new ObjectMapper().readValue(toolJson, Tool.class);
      tool.setId(id);

      if (images != null && !images.isEmpty()) {
        String imagePaths = images.stream()
            .map(this::uploadImage)
            .collect(Collectors.joining(","));
        tool.setImageUrl(imagePaths);
      }

      Tool updatedTool = toolService.save(tool);
      return ResponseEntity.ok(convertToDto(updatedTool));

    } catch (Exception e) {
      return ResponseEntity.badRequest()
          .body(Map.of("message", "Error al actualizar herramienta: " + e.getMessage()));
    }
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

  private String uploadImage(MultipartFile file) {
    try {
      String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
      Path path = Paths.get(UPLOAD_DIR + fileName);

      Files.copy(file.getInputStream(), path);

      return "/" + UPLOAD_DIR + fileName;

    } catch (IOException e) {
      throw new RuntimeException("Error al guardar la imagen: " + e.getMessage(), e);
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
        tool.getImageUrl(),
        categoryId,
        tool.getSupplier());
  }
}