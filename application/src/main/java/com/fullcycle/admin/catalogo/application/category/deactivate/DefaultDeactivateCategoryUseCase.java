package com.fullcycle.admin.catalogo.application.category.deactivate;

import com.fullcycle.admin.catalogo.application.category.activate.ActivateCategoryOutput;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultDeactivateCategoryUseCase extends DeactivateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultDeactivateCategoryUseCase(CategoryGateway categoryGateway) {
        Objects.requireNonNull(categoryGateway);
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, DeactivateCategoryOutput> execute(DeactivateCategoryCommand aCommand) {
        final var anId = CategoryID.from(aCommand.id());
        final var aCategory = this.categoryGateway.findById(anId)
                .orElseThrow(notFound(anId));
        aCategory.deactivate();
        return aCategory.hasErrors() ? Left(aCategory.getNotification()) : update(aCategory);
    }

    private Either<Notification, DeactivateCategoryOutput> update(final Category aCategory) {
        return Try(() -> this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, DeactivateCategoryOutput::from);
    }

    private Supplier<DomainException> notFound(final CategoryID anId) {
        return () -> DomainException.with(new Error(
                "Category with ID %s was not found".formatted(anId.getValue())));
    }
}
