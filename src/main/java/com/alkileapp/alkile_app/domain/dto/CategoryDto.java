package com.alkileapp.alkile_app.domain.dto;

import java.util.List;

public record CategoryDto(
        Long id,
        String name,
        String description,
        List<ToolDto> tools
) {}