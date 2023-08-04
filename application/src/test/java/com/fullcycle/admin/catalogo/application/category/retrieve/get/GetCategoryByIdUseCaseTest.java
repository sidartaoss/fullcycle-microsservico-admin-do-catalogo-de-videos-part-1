package com.fullcycle.admin.catalogo.application.category.retrieve.get;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class GetCategoryByIdUseCaseTest {

    /**
     * 1. Teste do caminho feliz
     * 2. Teste recuperar categoria passando ID inválido
     * 3. Teste simulando um erro genérico vindo do Gateway
     */

    @Mock
    CategoryGateway categoryGateway;

    @InjectMocks
    DefaultGetCategoryByIdUseCase getCategoryByIdUseCase;

    @Nested
    @DisplayName("Get a category by id with a valid id")
    class GetCategoryByIdWithValidId {

        @BeforeEach
        void cleanUp() {
            reset(categoryGateway);
        }

        @Test
        void Given_a_valid_id_When_calls_get_category_by_id_Then_should_return_an_ouput_category() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            final var actualOutput = getCategoryByIdUseCase.execute(expectedId.getValue());
            // then
            assertNotNull(actualOutput);
        }

        @Test
        void Given_a_valid_id_When_calls_get_category_by_id_Then_should_return_an_ouput_category_and_id() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            final var actualOutput = getCategoryByIdUseCase.execute(expectedId.getValue());
            // then
            assertEquals(expectedId.getValue(), actualOutput.id());
        }

        @Test
        void Given_a_valid_id_When_calls_get_category_by_id_Then_should_return_an_ouput_category_and_name() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            final var actualOutput = getCategoryByIdUseCase.execute(expectedId.getValue());
            // then
            assertEquals(expectedName, actualOutput.name());
        }

        @Test
        void Given_a_valid_id_When_calls_get_category_by_id_Then_should_return_an_ouput_category_and_description() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            final var actualOutput = getCategoryByIdUseCase.execute(expectedId.getValue());
            // then
            assertEquals(expectedDescription, actualOutput.description());
        }

        @Test
        void Given_a_valid_id_When_calls_get_category_by_id_Then_should_return_an_ouput_category_and_isActive() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedIsActive = true;
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            final var actualOutput = getCategoryByIdUseCase.execute(expectedId.getValue());
            // then
            assertEquals(expectedIsActive, actualOutput.active());
        }

        @Test
        void Given_a_valid_id_When_calls_get_category_by_id_Then_should_return_an_ouput_category_and_createdAt() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            final var actualOutput = getCategoryByIdUseCase.execute(expectedId.getValue());
            // then
            assertEquals(aCategory.getCreatedAt(), actualOutput.createdAt());
        }

        @Test
        void Given_a_valid_id_When_calls_get_category_by_id_Then_should_return_an_ouput_category_and_updatedAt() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            final var actualOutput = getCategoryByIdUseCase.execute(expectedId.getValue());
            // then
            assertEquals(aCategory.getUpdatedAt(), actualOutput.updatedAt());
        }

        @Test
        void Given_a_valid_id_When_calls_get_category_by_id_Then_should_return_an_ouput_category_and_deletedAt_as_null() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            final var actualOutput = getCategoryByIdUseCase.execute(expectedId.getValue());
            // then
            assertNull(actualOutput.deletedAt());
        }
    }

    @Nested
    @DisplayName("Get a category by id with an invalid id")
    class GetCategoryByIdWithAnInvalidId {

        @BeforeEach
        void cleanUp() {
            reset(categoryGateway);
        }

        @Test
        void Given_an_invalid_id_When_calls_get_category_by_id_Then_should_return_not_found_error_message() {
            // given
            final var expectedId = CategoryID.from("123");
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.empty());
            final var expectedErrorMessage = "Category with ID %s was not found"
                    .formatted(expectedId.getValue());
            // when
            Executable invalidMethodCall = () -> getCategoryByIdUseCase.execute(expectedId.getValue());
            // then
            final var actualException = assertThrows(NotFoundException.class, invalidMethodCall);
            assertEquals(expectedErrorMessage, actualException.getMessage());
        }
    }

    @Nested
    @DisplayName("Get a category by id with a valid id and a generic error from gateway")
    class GetCategoryByIdWithAValidIdAndAGenericErrorFromGateway {

        private static final String GATEWAY_ERROR = "Gateway Error";

        @BeforeEach
        void cleanUp() {
            reset(categoryGateway);
        }

        @Test
        void Given_a_valid_id_When_calls_get_category_by_id_and_gateway_throws_exception_Then_should_return_exception() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            when(categoryGateway.findById(eq(expectedId)))
                    .thenThrow(new IllegalStateException(GATEWAY_ERROR));
            // when
            Executable invalidMethodCall = () -> getCategoryByIdUseCase.execute(expectedId.getValue());
            // then
            final var actualException = assertThrows(IllegalStateException.class, invalidMethodCall);
            assertEquals(GATEWAY_ERROR, actualException.getMessage());
        }
    }
}
