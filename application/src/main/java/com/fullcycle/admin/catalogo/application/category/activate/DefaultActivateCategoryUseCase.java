package com.fullcycle.admin.catalogo.application.category.activate;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;
import com.fullcycle.admin.catalogo.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultActivateCategoryUseCase extends ActivateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultActivateCategoryUseCase(CategoryGateway categoryGateway) {
        Objects.requireNonNull(categoryGateway);
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, ActivateCategoryOutput> execute(ActivateCategoryCommand aCommand) {
        final var anId = CategoryID.from(aCommand.id());
        final var aCategory = this.categoryGateway.findById(anId)
                .orElseThrow(notFound(anId));
        aCategory.activate();
        return aCategory.hasErrors() ? Left(aCategory.getNotification()) : update(aCategory);
    }

    private Either<Notification, ActivateCategoryOutput> update(final Category aCategory) {
        return Try(() -> this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, ActivateCategoryOutput::from);
    }

    private Supplier<DomainException> notFound(final CategoryID anId) {
        return () -> DomainException.with(new Error(
                "Category with ID %s was not found".formatted(anId.getValue())));
    }
}
