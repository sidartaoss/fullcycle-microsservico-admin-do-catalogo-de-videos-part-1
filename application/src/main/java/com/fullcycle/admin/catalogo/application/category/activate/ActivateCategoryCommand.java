package com.fullcycle.admin.catalogo.application.category.activate;

public record ActivateCategoryCommand(
        String id
) {

    public static ActivateCategoryCommand with(final String id) {
        return new ActivateCategoryCommand(id);
    }
}
