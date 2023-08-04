package com.fullcycle.admin.catalogo.application.category.deactivate;

import com.fullcycle.admin.catalogo.application.UseCase;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class DeactivateCategoryUseCase
        extends UseCase<DeactivateCategoryCommand, Either<Notification, DeactivateCategoryOutput>> {
}
