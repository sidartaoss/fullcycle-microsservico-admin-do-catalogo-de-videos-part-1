package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.category.activate.ActivateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.activate.ActivateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.activate.ActivateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.deactivate.DeactivateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.deactivate.DeactivateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.deactivate.DeactivateCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.infrastructure.api.CategoryAPI;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CategoryListResponse;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.models.GetCategoryByIdResponse;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final ActivateCategoryUseCase activateCategoryUseCase;
    private final DeactivateCategoryUseCase deactivateCategoryUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final ListCategoriesUseCase listCategoriesUseCase,
            final ActivateCategoryUseCase activateCategoryUseCase,
            final DeactivateCategoryUseCase deactivateCategoryUseCase) {
        Objects.requireNonNull(createCategoryUseCase);
        Objects.requireNonNull(getCategoryByIdUseCase);
        Objects.requireNonNull(updateCategoryUseCase);
        Objects.requireNonNull(listCategoriesUseCase);
        Objects.requireNonNull(activateCategoryUseCase);
        Objects.requireNonNull(deactivateCategoryUseCase);
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
        this.activateCategoryUseCase = activateCategoryUseCase;
        this.deactivateCategoryUseCase = deactivateCategoryUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateCategoryRequest anInput) {
        final var aName = anInput.name();
        final var aDescription = anInput.description();
        final var aCreateCategoryCommand = CreateCategoryCommand.with(aName, aDescription);
        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/%s".formatted(output.id())))
                        .body(output);
        return this.createCategoryUseCase.execute(aCreateCategoryCommand)
                .fold(onError(), onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {
        final var aQuery = new CategorySearchQuery(page, perPage, search, sort, direction);
        return listCategoriesUseCase.execute(aQuery)
                    .map(CategoryApiPresenter::present);
    }

    @Override
    public GetCategoryByIdResponse getById(final String anId) {
        final var output = this.getCategoryByIdUseCase.execute(anId);
        return CategoryApiPresenter.present(output);
    }

    @Override
    public ResponseEntity<?> update(final String anId, final UpdateCategoryRequest anInput) {
        final var aName = anInput.name();
        final var aDescription = anInput.description();
        final var anUpdateCategoryCommand = UpdateCategoryCommand.with(anId, aName, aDescription);
        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;
        return updateCategoryUseCase.execute(anUpdateCategoryCommand)
                .fold(onError(), onSuccess);
    }

    @Override
    public void delete(final String anId) {
        this.deleteCategoryUseCase.execute(anId);
    }

    @Override
    public ResponseEntity<?> activate(String anId) {
        final var anActivateCategoryCommand = ActivateCategoryCommand.with(anId);
        final Function<ActivateCategoryOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;
        return activateCategoryUseCase.execute(anActivateCategoryCommand)
                .fold(onError(), onSuccess);
    }

    @Override
    public ResponseEntity<?> deactivate(String anId) {
        final var anDeactivateCategoryCommand = DeactivateCategoryCommand.with(anId);
        final Function<DeactivateCategoryOutput, ResponseEntity<?>> onSuccess =
                ResponseEntity::ok;
        return deactivateCategoryUseCase.execute(anDeactivateCategoryCommand)
                .fold(onError(), onSuccess);
    }

    private static Function<Notification, ResponseEntity<?>> onError() {
        return notification ->
                ResponseEntity.unprocessableEntity().body(notification);
    }
}
