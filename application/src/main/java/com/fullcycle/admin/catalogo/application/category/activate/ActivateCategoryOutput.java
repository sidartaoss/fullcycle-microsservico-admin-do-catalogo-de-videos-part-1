package com.fullcycle.admin.catalogo.application.category.activate;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;

public record ActivateCategoryOutput(
        String id
) {

    public static ActivateCategoryOutput from(final String anId) {
        return new ActivateCategoryOutput(anId);
    }

    public static ActivateCategoryOutput from(Category aCategory) {
        return new ActivateCategoryOutput(aCategory.getId().getValue());
    }
}
