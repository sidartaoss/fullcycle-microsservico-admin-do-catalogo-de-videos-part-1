package com.fullcycle.admin.catalogo.application.category.update;

public record UpdateCategoryCommand(
        String id,
        String name,
        String description
) {
    public static UpdateCategoryCommand with(
            final String id,
            final String name,
            final String description
    ) {
        return new UpdateCategoryCommand(id, name, description);
    }
}
