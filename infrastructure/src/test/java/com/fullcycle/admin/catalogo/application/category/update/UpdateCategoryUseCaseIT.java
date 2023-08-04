package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
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
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@IntegrationTest
class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Nested
    @DisplayName("Update with a valid command")
    class UpdateWithValidCommand {

        @Test
        void Given_a_valid_command_When_calls_update_category_Then_should_return_category_id() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var expectedIsActive = true;
            final var aCommand = UpdateCategoryCommand
                    .with(expectedId.getValue(), expectedName, expectedDescription);

            assertEquals(0, categoryRepository.count());
            save(aCategory);
            assertEquals(1, categoryRepository.count());

            // when
            final var actualOutput = updateCategoryUseCase.execute(aCommand).get();
            // then
            assertNotNull(actualOutput);
            assertNotNull(actualOutput.id());

            final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

            assertEquals(expectedId.getValue(), actualCategory.getId());
            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertEquals(LocalDate.ofInstant(aCategory.getCreatedAt(), ZoneOffset.UTC),
                    LocalDate.ofInstant(actualCategory.getCreatedAt(), ZoneOffset.UTC));
            assertTrue(actualCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt()));
            assertNull(actualCategory.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("Update with an invalid name")
    class UpdateWithInvalidName {

        @Test
        void Given_an_invalid_name_When_calls_update_category_Then_should_return_a_domain_exception() {
            // given
            final String expectedName = null;
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand
                    .with(expectedId.getValue(), expectedName, expectedDescription);

            final var expectedErrorMessage = "'name' should not be null";
            final var expectedErrorCount = 1;

            assertEquals(0, categoryRepository.count());
            save(aCategory);
            assertEquals(1, categoryRepository.count());

            // when
            Notification notification = updateCategoryUseCase.execute(aCommand).getLeft();

            // then
            assertEquals(expectedErrorMessage, notification.firstError().message());
            assertEquals(expectedErrorCount, notification.getErrors().size());

            verify(categoryGateway, never()).update(any(Category.class));
        }
    }

    @Nested
    @DisplayName("Update with an invalid id")
    class UpdateWithInvalidId {

        @Test
        void Given_a_command_with_invalid_id_When_calls_update_category_Then_should_return_not_found_exception() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedId = "123";
            final var aCommand = UpdateCategoryCommand
                    .with(expectedId, expectedName, expectedDescription);

            assertEquals(0, categoryRepository.count());

            final Executable invokeInvalidMethod = () -> updateCategoryUseCase.execute(aCommand);
            final var expectedErrorMessage = "Category with ID %s was not found"
                    .formatted(expectedId);

            // when
            final var actualException = assertThrows(NotFoundException.class, invokeInvalidMethod);

            // then
            assertEquals(expectedErrorMessage, actualException.getMessage());
        }
    }

    @Nested
    @DisplayName("Update with generic error from gateway")
    class UpdateWithGenericErrorFromGateway {

        private static final String GATEWAY_ERROR = "Gateway Error";

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_return_an_error_message() {
            // given
            final String expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var expectedIsActive = true;
            final var aCommand = UpdateCategoryCommand
                    .with(expectedId.getValue(), expectedName, expectedDescription);

            doThrow(new IllegalStateException(GATEWAY_ERROR))
                    .when(categoryGateway).update(any(Category.class));

            assertEquals(0, categoryRepository.count());
            save(aCategory);
            assertEquals(1, categoryRepository.count());

            // when
            final var notification = updateCategoryUseCase.execute(aCommand).getLeft();

            // then
            assertEquals(GATEWAY_ERROR, notification.firstError().message());

            final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

            assertEquals(expectedId.getValue(), actualCategory.getId());
            assertEquals(aCategory.getName(), actualCategory.getName());
            assertEquals(aCategory.getDescription(), actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertEquals(LocalDate.ofInstant(aCategory.getCreatedAt(), ZoneOffset.UTC),
                    LocalDate.ofInstant(actualCategory.getCreatedAt(), ZoneOffset.UTC));
            assertEquals(LocalDate.ofInstant(aCategory.getUpdatedAt(), ZoneOffset.UTC),
                    LocalDate.ofInstant(actualCategory.getUpdatedAt(), ZoneOffset.UTC));
            assertNull(actualCategory.getDeletedAt());
        }
    }

    private void save(final Category... aCategory) {
        final var list = Arrays.stream(aCategory)
                .map(CategoryJpaEntity::from)
                .toList();
        categoryRepository.saveAll(list);
    }
}
