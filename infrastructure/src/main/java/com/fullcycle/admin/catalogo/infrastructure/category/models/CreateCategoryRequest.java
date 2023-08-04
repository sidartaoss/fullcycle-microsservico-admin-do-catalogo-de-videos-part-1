package com.fullcycle.admin.catalogo.infrastructure.category.models;

public record CreateCategoryRequest(
        String name,
        String description
) {
}
