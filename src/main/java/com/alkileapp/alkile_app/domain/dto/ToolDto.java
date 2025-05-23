package com.alkileapp.alkile_app.domain.dto;

public record ToolDto(
                Long id,
                String name,
                String description,
                Double dailyCost,
                Integer stock,
                String ImageUrl,
                Long categoryId,
                SupplierDto supplier) {
}