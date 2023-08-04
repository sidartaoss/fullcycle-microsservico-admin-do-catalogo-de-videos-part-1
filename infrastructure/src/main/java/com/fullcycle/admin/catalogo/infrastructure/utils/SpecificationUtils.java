package com.fullcycle.admin.catalogo.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(prop)), like(term.toUpperCase()));
    }

    public static <T> Specification<T> oneEqualsOne() {
        return (root, query, cb) -> cb.equal(cb.literal(1), 1);
    }

    private static String like(final String term) {
        return "%" + term + "%";
    }
}
