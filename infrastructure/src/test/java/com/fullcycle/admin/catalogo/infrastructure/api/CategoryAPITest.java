package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.ControllerTest;
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
import com.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdOutput;
import com.fullcycle.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.fullcycle.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.fullcycle.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ControllerTest(controllers = CategoryAPI.class)
class CategoryAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @MockBean
    private ActivateCategoryUseCase activateCategoryUseCase;

    @MockBean
    private DeactivateCategoryUseCase deactivateCategoryUseCase;

    @Nested
    @DisplayName("Create with a valid input")
    class CreateWithValidInput {

        @Test
        void Given_a_valid_input_When_calls_create_category_Then_should_return_category_id()
                throws Exception {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedId = "123";
            final var anInput =
                    new CreateCategoryRequest(expectedName, expectedDescription);

            when(createCategoryUseCase.execute(any(CreateCategoryCommand.class)))
                    .thenReturn(Right(CreateCategoryOutput.from(expectedId)));

            final var request = post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(anInput));

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", "/categories/" + expectedId))
                    .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", equalTo(expectedId)));
            verify(createCategoryUseCase, times(1))
                    .execute(argThat(command -> {
                        return Objects.equals(expectedName, command.name())
                                && Objects.equals(expectedDescription, command.description());
                    }));
        }
    }

    @Nested
    @DisplayName("Create with an invalid input")
    class CreateWithInvalidInput {

        @Test
        void Given_an_invalid_input_When_calls_create_category_Then_should_return_domain_exception()
                throws Exception {
            // Given
            final String expectedName = " ";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedErrorMessage = "'name' should not be null";
            final var anInput =
                    new CreateCategoryRequest(expectedName, expectedDescription);

            when(createCategoryUseCase.execute(any(CreateCategoryCommand.class)))
                    .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

            final var request = post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(anInput));

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(header().string("Location", nullValue()))
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
            verify(createCategoryUseCase, times(1))
                    .execute(argThat(command -> {
                        return Objects.equals(expectedName, command.name())
                                && Objects.equals(expectedDescription, command.description());
                    }));
        }

        @Test
        void Given_an_invalid_input_When_calls_create_category_and_throw_error_Then_should_return_domain_exception()
                throws Exception {
            // Given
            final String expectedName = " ";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedErrorMessage = "'name' should not be null";
            final var anInput =
                    new CreateCategoryRequest(expectedName, expectedDescription);

            when(createCategoryUseCase.execute(any(CreateCategoryCommand.class)))
                    .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

            final var request = post("/categories")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(anInput));

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(header().string("Location", nullValue()))
                    .andExpect(jsonPath("$.errors", hasSize(1)))
                    .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
            verify(createCategoryUseCase, times(1))
                    .execute(argThat(command -> {
                        return Objects.equals(expectedName, command.name())
                                && Objects.equals(expectedDescription, command.description());
                    }));
        }
    }

    @Nested
    @DisplayName("Get category with a valid id")
    class GetCategoryWithValidId {

        @Test
        void Given_a_valid_category_id_When_calls_find_by_id_Then_should_return_category() throws Exception {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId().getValue();
            final var expectedIsActive = aCategory.isActive();

            when(getCategoryByIdUseCase.execute(eq(expectedId)))
                    .thenReturn(GetCategoryByIdOutput.from(aCategory));

            final var request = get("/categories/{id}", expectedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", equalTo(expectedId)))
                    .andExpect(jsonPath("$.name", equalTo(expectedName)))
                    .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                    .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                    .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                    .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                    .andExpect(jsonPath("$.deleted_at", is(nullValue())));
            verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));
        }
    }

    @Nested
    @DisplayName("Get category with an invalid id")
    class GetCategoryWithInvalidId {

        @Test
        void Given_an_invalid_id_When_calls_find_by_id_Then_should_return_not_found() throws Exception {
            // Given
            final var expectedId = "123";
            final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);

            final var request = get("/categories/{id}", expectedId)
                    .contentType(MediaType.APPLICATION_JSON);

            when(getCategoryByIdUseCase.execute(eq(expectedId)))
                    .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

            verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId));
        }
    }

    @Nested
    @DisplayName("Update with a valid input")
    class UpdateWithValidInput {

        @Test
        void Given_a_valid_input_When_calls_update_category_Then_should_return_category_updated()
                throws Exception {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedId = "123";
            final var anInput =
                    new UpdateCategoryRequest(expectedName, expectedDescription);

            when(updateCategoryUseCase.execute(any(UpdateCategoryCommand.class)))
                    .thenReturn(Right(UpdateCategoryOutput.from(expectedId)));

            final var request = put("/categories/{id}", expectedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(anInput));

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", equalTo(expectedId)));
            verify(updateCategoryUseCase, times(1))
                    .execute(argThat(command -> {
                        return Objects.equals(expectedName, command.name())
                                && Objects.equals(expectedDescription, command.description());
                    }));
        }
    }

    @Nested
    @DisplayName("Update with an invalid id")
    class UpdateWithInvalidId {

        @Test
        void Given_an_invalid_id_When_calls_update_category_Then_should_return_not_found_exception()
                throws Exception {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var anInput =
                    new UpdateCategoryRequest(expectedName, expectedDescription);

            final var expectedId = "not-found";
            final var expectedErrorMessage = "%s with ID %s was not found"
                    .formatted(Category.class.getSimpleName(), expectedId);

            when(updateCategoryUseCase.execute(any(UpdateCategoryCommand.class)))
                    .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

            final var request = put("/categories/{id}", expectedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(anInput));

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isNotFound())
                    .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
            verify(updateCategoryUseCase, times(1))
                    .execute(argThat(command -> {
                        return Objects.equals(expectedName, command.name())
                                && Objects.equals(expectedDescription, command.description());
                    }));
        }
    }

    @Nested
    @DisplayName("Update with an invalid name")
    class UpdateWithInvalidName {

        @Test
        void Given_an_invalid_id_When_calls_update_category_Then_should_return_domain_exception()
                throws Exception {
            // Given
            final var expectedName = " ";
            final var expectedDescription = "A categoria mais assistida";
            final var anInput =
                    new UpdateCategoryRequest(expectedName, expectedDescription);

            final var expectedId = "123";
            final var expectedErrorMessage = "'name' should not be empty";
            final var expectedErrorCount = 1;

            when(updateCategoryUseCase.execute(any(UpdateCategoryCommand.class)))
                    .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

            final var request = put("/categories/{id}", expectedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(anInput));

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                    .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
            verify(updateCategoryUseCase, times(1))
                    .execute(argThat(command -> {
                        return Objects.equals(expectedName, command.name())
                                && Objects.equals(expectedDescription, command.description());
                    }));
        }
    }

    @Nested
    @DisplayName("Activate with a valid input")
    class ActivateWithValidInput {

        @Test
        void Given_a_valid_input_When_calls_activate_category_Then_should_return_an_active_category_id()
                throws Exception {
            // Given
            final var expectedId = "123";

            when(activateCategoryUseCase.execute(any(ActivateCategoryCommand.class)))
                    .thenReturn(Right(ActivateCategoryOutput.from(expectedId)));

            final var request = put("/categories/{id}/active", expectedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", equalTo(expectedId)));
            verify(activateCategoryUseCase, times(1))
                    .execute(argThat(command -> {
                        return Objects.equals(expectedId, command.id());
                    }));
        }
    }

    @Nested
    @DisplayName("Deactivate with a valid input")
    class DeactivateWithValidInput {

        @Test
        void Given_a_valid_input_When_calls_deactivate_category_Then_should_return_an_inactivated_category_id()
                throws Exception {
            // Given
            final var expectedId = "123";

            when(deactivateCategoryUseCase.execute(any(DeactivateCategoryCommand.class)))
                    .thenReturn(Right(DeactivateCategoryOutput.from(expectedId)));

            final var request = put("/categories/{id}/inactive", expectedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isOk())
                    .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(jsonPath("$.id", equalTo(expectedId)));
            verify(deactivateCategoryUseCase, times(1))
                    .execute(argThat(command -> {
                        return Objects.equals(expectedId, command.id());
                    }));
        }
    }

    @Nested
    @DisplayName("Delete category with a valid id")
    class DeleteCategoryWithValidId {

        @Test
        void Given_a_valid_category_id_When_calls_delete_Then_should_return_no_content() throws Exception {
            // Given
            final var expectedId = "123";

            doNothing().when(deleteCategoryUseCase)
                    .execute(eq(expectedId));

            final var request = delete("/categories/{id}", expectedId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isNoContent());
            verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId));
        }
    }

    @Nested
    @DisplayName("List categories with valid params")
    class ListCategoriesWithValidParams {

        @Test
        void Given_a_valid_params_When_calls_list_categories_Then_should_return_categories()
                throws Exception {
            // Given
            final var aCategory = Category.newCategory("Movies", " ");
            final var expectedPage = 0;
            final var expectedPerPage = 10;
            final var expectedTerms = "movies";
            final var expectedSort = "description";
            final var expectedDirection = "desc";
            final var expectedItemsCount = 1;
            final var expectedTotal = 1;
            final var expectedItems = List.of(CategoryListOutput.from(aCategory));

            when(listCategoriesUseCase.execute(any(CategorySearchQuery.class)))
                    .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

            final var request = get("/categories")
                    .queryParam("page", String.valueOf(expectedPage))
                    .queryParam("perPage", String.valueOf(expectedPerPage))
                    .queryParam("sort", expectedSort)
                    .queryParam("dir", expectedDirection)
                    .queryParam("search", expectedTerms)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON);

            // When
            final var response = mockMvc.perform(request)
                    .andDo(print());

            // Then
            response
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                    .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                    .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                    .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                    .andExpect(jsonPath("$.items[0].id", is(equalTo(aCategory.getId().getValue()))))
                    .andExpect(jsonPath("$.items[0].name", is(equalTo(aCategory.getName()))))
                    .andExpect(jsonPath("$.items[0].description", is(equalTo(aCategory.getDescription()))))
                    .andExpect(jsonPath("$.items[0].is_active", is(equalTo(aCategory.isActive()))))
                    .andExpect(jsonPath("$.items[0].created_at", is(equalTo(aCategory.getCreatedAt().toString()))))
                    .andExpect(jsonPath("$.items[0].deleted_at", is(equalTo(aCategory.getDeletedAt()))));
            verify(listCategoriesUseCase, times(1)).execute(argThat(query -> {
                return Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms());
            }));
        }
    }
}
