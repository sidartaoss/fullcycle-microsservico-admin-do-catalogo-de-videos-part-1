package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest {

    /**
     * 1. Teste do caminho feliz
     * 2. Teste passando uma propriedade inválida (name)
     * 3. Teste criando uma categoria inativa
     * 4. Teste simulando um erro genérico vindo do Gateway
     */
    @Mock
    CategoryGateway categoryGateway;

    @InjectMocks
    DefaultCreateCategoryUseCase createCategoryUseCase;

    @Nested
    @DisplayName("Create with valid command")
    class CreateWithValidCommand {

        @BeforeEach
        void init() {
            reset(categoryGateway);
            when(categoryGateway.create(any(Category.class)))
                    .thenAnswer(returnsFirstArg());
        }

        @Test
        void Given_a_valid_command_When_calls_create_category_Then_should_return_an_ouput() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            final var actualOutput = createCategoryUseCase.execute(aCommand).get();
            // then
            assertNotNull(actualOutput);
        }

        @Test
        void Given_a_valid_command_When_calls_create_category_Then_should_return_an_ouput_with_a_newly_created_category_id() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            final var actualOutput = createCategoryUseCase.execute(aCommand).get();
            // then
            assertNotNull(actualOutput.id());
        }

        @Test
        void Given_a_valid_command_When_calls_create_category_Then_should_verify_that_gateway_create_was_invoked_only_once() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway, times(1)).create(any(Category.class));
        }

        @Test
        void Given_a_valid_command_When_calls_create_category_Then_should_verify_argument_category_id_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.nonNull(aCategory.getId());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_create_category_Then_should_verify_argument_category_name_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.equals(expectedName, aCategory.getName());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_create_category_Then_should_verify_argument_category_description_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.equals(expectedDescription, aCategory.getDescription());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_create_category_Then_should_verify_argument_category_isActive_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            final var expectedIsActive = true;
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.equals(expectedIsActive, aCategory.isActive());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_create_category_Then_should_verify_argument_category_createdAt_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.nonNull(aCategory.getCreatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_create_category_Then_should_verify_argument_category_updatedAt_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.nonNull(aCategory.getUpdatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_create_category_Then_should_verify_argument_category_deletedAt_was_null() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.isNull(aCategory.getDeletedAt());
            }));
        }
    }

    @Nested
    @DisplayName("Create with invalid name")
    class CreateWithInvalidName {

        @BeforeEach
        void cleanUp() {
            reset(categoryGateway);
        }

        @Test
        void Given_an_invalid_name_When_calls_create_category_Then_should_return_an_error_message() {
            // given
            final String expectedName = null;
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            final var expectedErrorMessage = "'name' should not be null";
            // when
            Notification notification = createCategoryUseCase.execute(aCommand).getLeft();
            // then
            assertEquals(expectedErrorMessage, notification.firstError().message());
        }

        @Test
        void Given_an_invalid_name_When_calls_create_category_Then_should_return_error_count_as_1() {
            // given
            final String expectedName = null;
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            final var expectedErrorCount = 1;
            // when
            Notification notification = createCategoryUseCase.execute(aCommand).getLeft();
            // then
            assertEquals(expectedErrorCount, notification.getErrors().size());
        }

        @Test
        void Given_an_invalid_name_When_calls_create_category_Then_should_verify_that_create_has_not_been_called() {
            // given
            final String expectedName = null;
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway, times(0)).create(any(Category.class));
        }
    }

    @Nested
    @DisplayName("Create with generic error from gateway")
    class CreateWithGenericErrorFromGateway {

        private static final String GATEWAY_ERROR = "Gateway Error";

        @BeforeEach
        void init() {
            reset(categoryGateway);
            when(categoryGateway.create(any(Category.class)))
                    .thenThrow(new IllegalStateException(GATEWAY_ERROR));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_return_an_error_message() {
            // given
            final String expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            final var expectedErrorMessage = GATEWAY_ERROR;
            // when
            Notification notification = createCategoryUseCase.execute(aCommand).getLeft();
            // then
            assertEquals(expectedErrorMessage, notification.firstError().message());
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_that_gateway_create_was_invoked_only_once() {
            // given
            final String expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway, times(1)).create(any(Category.class));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_id_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.nonNull(aCategory.getId());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_name_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.equals(expectedName, aCategory.getName());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_description_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.equals(expectedDescription, aCategory.getDescription());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_isActive_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            final var expectedIsActive = true;
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.equals(expectedIsActive, aCategory.isActive());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_createdAt_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.nonNull(aCategory.getCreatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_updatedAt_was_passed_to_gateway_create() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.nonNull(aCategory.getUpdatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_deletedAt_was_null() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription);
            // when
            createCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).create(argThat(aCategory -> {
                return Objects.isNull(aCategory.getDeletedAt());
            }));
        }
    }
}
