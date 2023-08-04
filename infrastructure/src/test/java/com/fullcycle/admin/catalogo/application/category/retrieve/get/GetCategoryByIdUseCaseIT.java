package com.fullcycle.admin.catalogo.application.category.retrieve.get;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@IntegrationTest
class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Nested
    @DisplayName("Get category with a valid id")
    class GetCategoryWithValidId {

        @Test
        void Given_a_valid_category_id_When_calls_find_by_id_Then_should_return_category() {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            final var expectedIsActive = true;

            assertEquals(0, categoryRepository.count());

            save(aCategory);

            assertEquals(1, categoryRepository.count());

            // When
            final var actualCategory = getCategoryByIdUseCase.execute(expectedId.getValue());

            // Then
            assertEquals(1, categoryRepository.count());

            assertEquals(expectedId.getValue(), actualCategory.id());
            assertEquals(expectedName, actualCategory.name());
            assertEquals(expectedDescription, actualCategory.description());
            assertEquals(expectedIsActive, actualCategory.active());
            assertEquals(LocalDate.ofInstant(aCategory.getCreatedAt(), ZoneOffset.UTC),
                    LocalDate.ofInstant(actualCategory.createdAt(), ZoneOffset.UTC));
            assertEquals(LocalDate.ofInstant(aCategory.getUpdatedAt(), ZoneOffset.UTC),
                    LocalDate.ofInstant(actualCategory.updatedAt(), ZoneOffset.UTC));
            assertNull(actualCategory.deletedAt());
        }
    }

    @Nested
    @DisplayName("Get category with an invalid id")
    class GetCategoryWithInvalidId {

        @Test
        void Given_an_invalid_id_When_calls_get_category_by_id_Then_should_return_not_found_error_message() {
            // given
            final var expectedId = CategoryID.from("123");
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
    @DisplayName("Get category by id with a generic error from gateway")
    class GetCategoryByIdWithAGenericErrorFromGateway {

        private static final String GATEWAY_ERROR = "Gateway Error";

        @Test
        void Given_a_valid_id_When_calls_get_category_by_id_and_gateway_throws_exception_Then_should_return_exception() {
            // given
            final var expectedId = CategoryID.from("123");
            doThrow(new IllegalStateException(GATEWAY_ERROR))
                    .when(categoryGateway).findById(eq(expectedId));
            // when
            Executable invalidMethodCall = () -> getCategoryByIdUseCase.execute(expectedId.getValue());
            // then
            final var actualException = assertThrows(IllegalStateException.class, invalidMethodCall);
            assertEquals(GATEWAY_ERROR, actualException.getMessage());
        }
    }

    private void save(final Category... aCategory) {
        final var list = Arrays.stream(aCategory)
                .map(CategoryJpaEntity::from)
                .toList();
        categoryRepository.saveAll(list);
    }
}
