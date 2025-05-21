package com.alkileapp.alkile_app.infrastructure.controllers;

import com.alkileapp.alkile_app.application.services.ICategoryService;
import com.alkileapp.alkile_app.domain.dto.CategoryDto;
import com.alkileapp.alkile_app.domain.dto.SupplierDto;
import com.alkileapp.alkile_app.domain.dto.ToolDto;
import com.alkileapp.alkile_app.domain.entities.Category;
import com.alkileapp.alkile_app.domain.entities.Tool;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/alkile/categories")
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {
        List<CategoryDto> categories = categoryService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        return ResponseEntity.ok(categoryService.save(category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable Long id, @RequestBody Category category) {
        if (!categoryService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        category.setId(id);
        Category updatedCategory = categoryService.save(category);
        return ResponseEntity.ok(convertToDto(updatedCategory));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!categoryService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private CategoryDto convertToDto(Category category) {
        List<ToolDto> tools = category.getTools().stream()
                .map(this::convertToolToDto)
                .collect(Collectors.toList());
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                tools);
    }

    private ToolDto convertToolToDto(Tool tool) {
    Long categoryId = Optional.ofNullable(tool.getCategory())
                              .map(Category::getId)
                              .orElse(null);

    SupplierDto supplierDto = new SupplierDto(
        tool.getSupplier().getId(),
        tool.getSupplier().getTaxId(),
        tool.getSupplier().getCompany(),
        tool.getSupplier().getRating(),
        tool.getSupplier().getUser().getId()
    );

    return new ToolDto(
        tool.getId(),
        tool.getName(),
        tool.getDescription(),
        tool.getDailyCost(),
        tool.getStock(),
        categoryId,
        supplierDto
    );
}
}
