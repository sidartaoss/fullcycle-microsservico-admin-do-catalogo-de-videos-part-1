package com.fullcycle.admin.catalogo.application.category.deactivate;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;

public record DeactivateCategoryOutput(
        String id
) {

    public static DeactivateCategoryOutput from(final String anId) {
        return new DeactivateCategoryOutput(anId);
    }

    public static DeactivateCategoryOutput from(Category aCategory) {
        return new DeactivateCategoryOutput(aCategory.getId().getValue());
    }
}
