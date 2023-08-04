package com.fullcycle.admin.catalogo.application.category.delete;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@IntegrationTest
class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Nested
    @DisplayName("Delete with valid id")
    class DeleteWithValidId {

        @Test
        void Given_a_valid_id_When_calls_delete_category_Then_should_be_Ok() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();

            assertEquals(0, categoryRepository.count());
            save(aCategory);
            assertEquals(1, categoryRepository.count());

            // when
            Executable validMethodCall = () -> deleteCategoryUseCase.execute(expectedId.getValue());

            // then
            assertDoesNotThrow(validMethodCall);
            assertEquals(0, categoryRepository.count());
        }
    }

    @Nested
    @DisplayName("Delete with invalid id")
    class DeleteWithInvalidId {

        @Test
        void Given_an_invalid_id_When_calls_delete_category_Then_should_be_Ok() {
            // given
            final var expectedId = CategoryID.from("123");

            assertEquals(0, categoryRepository.count());

            // when
            Executable validMethodCall = () -> deleteCategoryUseCase.execute(expectedId.getValue());

            // then
            assertDoesNotThrow(validMethodCall);
            assertEquals(0, categoryRepository.count());
        }
    }

    @Nested
    @DisplayName("Delete with generic error from gateway")
    class DeleteWithGenericErrorFromGateway {

        private static final String GATEWAY_ERROR = "Gateway error";

        @Test
        void Given_a_valid_id_When_gateway_throws_error_Then_should_return_exception() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            doThrow(new IllegalStateException(GATEWAY_ERROR))
                    .when(categoryGateway).deleteById(eq(expectedId));

            // when
            final Executable invalidMethodCall = () -> deleteCategoryUseCase.execute(expectedId.getValue());

            // then
            assertThrows(IllegalStateException.class, invalidMethodCall);
            verify(categoryGateway, times(1)).deleteById(eq(expectedId));
        }
    }

    private void save(final Category... aCategory) {
        final var list = Arrays.stream(aCategory)
                .map(CategoryJpaEntity::from)
                .toList();
        categoryRepository.saveAll(list);
    }
}
