package com.fullcycle.admin.catalogo.application.category.deactivate;

public record DeactivateCategoryCommand(String id) {

    public static DeactivateCategoryCommand with(String id) {
        return new DeactivateCategoryCommand(id);
    }
}
