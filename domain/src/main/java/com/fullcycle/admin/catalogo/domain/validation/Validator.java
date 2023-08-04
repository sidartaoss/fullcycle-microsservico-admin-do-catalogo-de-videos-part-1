package com.fullcycle.admin.catalogo.domain.validation;

import java.util.Objects;

public abstract class Validator {

    private final ValidationHandler handler;

    protected Validator(final ValidationHandler handler) {
        Objects.requireNonNull(handler);
        this.handler = handler;
    }

    public abstract void validate();

    protected ValidationHandler validationHandler() {
        return handler;
    }
}

