package com.fullcycle.admin.catalogo.domain.category;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private static Notification notification;

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(anUpdatedAt, "'updatedAt' should not be null");
    }

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt) {
        this(anId, aName, aDescription, isActive, aCreatedAt, anUpdatedAt);
        this.deletedAt = aDeletedAt;
    }

    public static Category newCategory(
            final String aName,
            final String aDescription) {
        final CategoryID anId = CategoryID.unique();
        final var now = Instant.now();
        final var isActive = true;
        final var category = new Category(anId, aName, aDescription, isActive, now, now);
        notification = Notification.create();
        category.validate(notification);
        return category;
    }

    public static Category with(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        return new Category(
                anId,
                aName,
                aDescription,
                isActive,
                aCreatedAt,
                anUpdatedAt,
                aDeletedAt
        );
    }

    public static Category with(final Category aCategory) {
        return with(
                aCategory.id,
                aCategory.name,
                aCategory.description,
                aCategory.isActive(),
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt
        );
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }
        this.updatedAt = Instant.now();
        this.active = false;
//        this.validate(new ThrowsValidationHandler());
        this.validate(notification);
        return this;
    }

    public Category activate() {
        this.deletedAt = null;
        this.updatedAt = Instant.now();
        this.active = true;
//        this.validate(new ThrowsValidationHandler());
        this.validate(notification);
        return this;
    }

    public Category update(final String aName, final String aDescription) {
        this.updatedAt = Instant.now();
        this.name = aName;
        this.description = aDescription;
//        this.validate(new ThrowsValidationHandler());
        this.validate(notification);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Notification getNotification() {
        return notification;
    }

    public boolean hasErrors() {
        return notification.hasErrors();
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
