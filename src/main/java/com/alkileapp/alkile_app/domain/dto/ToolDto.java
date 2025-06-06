package com.alkileapp.alkile_app.domain.dto;

import com.alkileapp.alkile_app.domain.entities.User;

public record ToolDto(
    Long id,
    String name,
    String description,
    Double dailyCost,
    Integer stock,
    String imageUrl,
    Long categoryId,
    User supplier
) {}