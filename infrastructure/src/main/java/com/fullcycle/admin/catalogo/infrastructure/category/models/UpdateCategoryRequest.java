package com.fullcycle.admin.catalogo.infrastructure.category.models;

public record UpdateCategoryRequest(
        String name,
        String description
) {
}
