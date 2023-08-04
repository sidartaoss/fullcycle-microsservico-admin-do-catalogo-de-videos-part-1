package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Nested
    @DisplayName("Create with a valid command")
    class CreateWithValidCommand {

        @Test
        void Given_a_valid_command_When_calls_create_category_Then_should_return_category_id() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            final var expectedIsActive = true;

            assertEquals(0, categoryRepository.count());

            // when
            final var actualOutput = createCategoryUseCase.execute(aCommand).get();
            // then
            assertNotNull(actualOutput);
            assertNotNull(actualOutput.id());

            assertEquals(1, categoryRepository.count());

            final var actualCategory =
                    categoryRepository.findById(actualOutput.id()).get();

            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertNotNull(actualCategory.getCreatedAt());
            assertNotNull(actualCategory.getUpdatedAt());
            assertNull(actualCategory.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("Create with an invalid name")
    class CreateWithInvalidName {

        @Test
        void Given_an_invalid_name_When_calls_create_category_Then_should_return_a_domain_exception() {
            // given
            final String expectedName = null;
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            final var expectedErrorMessage = "'name' should not be null";
            final var expectedErrorCount = 1;

            assertEquals(0, categoryRepository.count());

            // when
            Notification notification = createCategoryUseCase.execute(aCommand).getLeft();

            // then
            assertEquals(expectedErrorCount, notification.getErrors().size());
            assertEquals(expectedErrorMessage, notification.firstError().message());

            assertEquals(0, categoryRepository.count());

            verify(categoryGateway, never()).create(any(Category.class));
        }
    }

    @Nested
    @DisplayName("Create with generic error from gateway")
    class CreateWithGenericErrorFromGateway {

        private static final String GATEWAY_ERROR = "Gateway Error";

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_return_an_error_message() {
            // given
            final String expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            doThrow(new IllegalStateException(GATEWAY_ERROR))
                    .when(categoryGateway).create(any(Category.class));
            final var expectedErrorCount = 1;
            // when
            Notification notification = createCategoryUseCase.execute(aCommand).getLeft();
            // then
            assertEquals(expectedErrorCount, notification.getErrors().size());
            assertEquals(GATEWAY_ERROR, notification.firstError().message());
        }
    }
}
