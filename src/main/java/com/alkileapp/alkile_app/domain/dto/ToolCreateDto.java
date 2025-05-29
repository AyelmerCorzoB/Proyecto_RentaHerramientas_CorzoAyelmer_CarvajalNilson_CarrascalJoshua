package com.alkileapp.alkile_app.domain.dto;

public record ToolCreateDto(
    String name,
    String description,
    Double dailyCost,
    Integer stock,
    Long categoryId,
    Long supplierId
) {}