package com.fullcycle.admin.catalogo.application.category.delete;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    /**
     * 1. Teste do caminho feliz
     * 2. Teste simulando um erro genérico vindo do Gateway
     * 3. Teste deletar categoria passando ID inválido.
     */

    @Mock
    CategoryGateway categoryGateway;

    @InjectMocks
    DefaultDeleteCategoryUseCase deleteCategoryUseCase;

    @Nested
    @DisplayName("Delete with valid id")
    class DeleteWithValidId {

        @BeforeEach
        void cleanUp() {
            reset(categoryGateway);
        }

        @Test
        void Given_a_valid_id_When_calls_delete_category_Then_should_be_Ok() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            doNothing().when(categoryGateway)
                    .deleteById(eq(expectedId));
            // when
            Executable validMethodCall = () -> deleteCategoryUseCase.execute(expectedId.getValue());
            // then
            assertDoesNotThrow(validMethodCall);
        }

        @Test
        void Given_a_valid_id_When_calls_delete_category_Then_should_verify_gateway_deleteById_was_called_only_once() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            doNothing().when(categoryGateway)
                    .deleteById(eq(expectedId));
            // when
            assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId.getValue()));
            // then
            verify(categoryGateway, times(1)).deleteById(eq(expectedId));
        }
    }

    @Nested
    @DisplayName("Delete with invalid id")
    class DeleteWithInvalidId {

        @BeforeEach
        void cleanUp() {
            reset(categoryGateway);
        }

        @Test
        void Given_an_invalid_id_When_calls_delete_category_Then_should_be_Ok() {
            // given
            final var expectedId = "123";
            doNothing().when(categoryGateway)
                    .deleteById(eq(CategoryID.from(expectedId)));
            // when
            Executable validMethodCall = () -> deleteCategoryUseCase.execute(expectedId);
            // then
            assertDoesNotThrow(validMethodCall);
        }

        @Test
        void Given_an_invalid_id_When_calls_delete_Then_should_verify_gateway_deleteById_was_called_only_once() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            doNothing().when(categoryGateway)
                    .deleteById(eq(expectedId));
            // when
            assertDoesNotThrow(() -> deleteCategoryUseCase.execute(expectedId.getValue()));
            // then
            verify(categoryGateway, times(1)).deleteById(eq(expectedId));
        }
    }

    @Nested
    @DisplayName("Delete with generic error from gateway")
    class DeleteWithGenericErrorFromGateway {

        @BeforeEach
        void cleanUp() {
            reset(categoryGateway);
        }

        @Test
        void Given_a_valid_id_When_gateway_throws_error_Then_should_return_exception() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            doThrow(new IllegalStateException("Gateway error"))
                    .when(categoryGateway).deleteById(expectedId);
            // when
            final Executable invalidMethodCall = () -> deleteCategoryUseCase.execute(expectedId.getValue());
            // then
            assertThrows(IllegalStateException.class, invalidMethodCall);
        }

        @Test
        void Given_an_invalid_id_When_gateway_throws_error_Then_should_verify_that_deleteById_was_called_only_once() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            doThrow(new IllegalStateException("Gateway error"))
                    .when(categoryGateway).deleteById(expectedId);
            // when
            assertThrows(IllegalStateException.class, () -> deleteCategoryUseCase.execute(expectedId.getValue()));
            // then
            verify(categoryGateway, times(1)).deleteById(eq(expectedId));
        }
    }
}
