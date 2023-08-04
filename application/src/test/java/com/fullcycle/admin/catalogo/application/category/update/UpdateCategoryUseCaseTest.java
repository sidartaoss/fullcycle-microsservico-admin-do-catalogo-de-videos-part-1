package com.fullcycle.admin.catalogo.application.category.update;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    /**
     * 1. Teste do caminho feliz
     * 2. Teste passando uma propriedade inválida (name)
     * 3. Teste atualizando uma categoria para inativa
     * 4. Teste simulando um erro genérico vindo do Gateway
     * 5. Teste atualizar categoria passando ID inválido.
     */

    @Mock
    CategoryGateway categoryGateway;

    @InjectMocks
    DefaultUpdateCategoryUseCase updateCategoryUseCase;

    @Nested
    @DisplayName("Update with valid command")
    class UpdateWithValidCommand {

        @BeforeEach
        void init() {
            reset(categoryGateway);
            when(categoryGateway.update(any(Category.class)))
                    .thenAnswer(returnsFirstArg());
        }

        @Test
        void Given_a_valid_command_When_calls_update_category_Then_should_return_an_ouput() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            final var actualOutput = updateCategoryUseCase.execute(aCommand).get();
            // then
            assertNotNull(actualOutput);
        }

        @Test
        void Given_a_valid_command_When_calls_update_category_Then_should_return_an_ouput_with_category_id() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            final var actualOutput = updateCategoryUseCase.execute(aCommand).get();
            // then
            assertNotNull(actualOutput.id());
        }

        @Test
        void Given_a_valid_command_When_calls_update_category_Then_should_verify_that_gateway_findById_was_invoked_only_once() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway, times(1)).findById(any(CategoryID.class));
        }

        @Test
        void Given_a_valid_command_When_calls_update_category_Then_should_verify_that_gateway_update_was_invoked_only_once() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway, times(1)).update(any(Category.class));
        }

        @Test
        void Given_a_valid_command_When_calls_update_category_Then_should_verify_argument_category_id_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedId, anUpdatedCategory.getId());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_update_category_Then_should_verify_argument_category_name_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedName, anUpdatedCategory.getName());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_update_category_Then_should_verify_argument_category_description_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedDescription, anUpdatedCategory.getDescription());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_update_Then_should_verify_argument_isActive_was_passed_to_gateway() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            final var expectedIsActive = true;
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedIsActive, anUpdatedCategory.isActive());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_update_category_Then_should_verify_argument_category_createdAt_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_update_category_Then_should_verify_argument_category_updatedAt_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return anUpdatedCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_update_category_Then_should_verify_argument_category_deletedAt_was_is_null() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.isNull(anUpdatedCategory.getDeletedAt());
            }));
        }
    }

    @Nested
    @DisplayName("Update with an invalid name")
    class UpdateWithInvalidName {

        @BeforeEach
        void cleanUp() {
            reset(categoryGateway);
        }

        @Test
        void Given_an_invalid_name_When_calls_update_category_Then_should_return_a_domain_exception() {
            // given
            final String expectedName = null;
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            final var expectedErrorMessage = "'name' should not be null";
            // when
            Notification notification = updateCategoryUseCase.execute(aCommand).getLeft();
            // then
            assertEquals(expectedErrorMessage, notification.firstError().message());
        }

        @Test
        void Given_an_invalid_name_When_calls_update_category_Then_should_return_error_count_as_1() {
            // given
            final String expectedName = null;
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            final var expectedErrorCount = 1;
            // when
            Notification notification = updateCategoryUseCase.execute(aCommand).getLeft();
            // then
            assertEquals(expectedErrorCount, notification.getErrors().size());
        }

        @Test
        void Given_an_invalid_name_When_calls_update_category_Then_should_verify_that_create_has_not_been_called() {
            // given
            final String expectedName = null;
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway, times(0)).create(any(Category.class));
        }
    }

    @Nested
    @DisplayName("Update with generic error from gateway")
    class UpdateWithGenericErrorFromGateway {

        private static final String GATEWAY_ERROR = "Gateway Error";

        @BeforeEach
        void init() {
            reset(categoryGateway);
            when(categoryGateway.update(any(Category.class)))
                    .thenThrow(new IllegalStateException(GATEWAY_ERROR));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_return_an_error_message() {
            // given
            final String expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            final var expectedErrorMessage = GATEWAY_ERROR;
            // when
            Notification notification = updateCategoryUseCase.execute(aCommand).getLeft();
            // then
            assertEquals(expectedErrorMessage, notification.firstError().message());
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_exception_Then_should_verify_update_was_invoked_only_once() {
            // given
            final String expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway, times(1)).update(any(Category.class));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_exception_Then_should_verify_id_was_passed_to_gateway() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.nonNull(anUpdatedCategory.getId());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_exception_Then_should_verify_name_was_passed_to_gateway() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedName, anUpdatedCategory.getName());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_exception_Then_should_verify_description_was_passed_to_gateway() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedDescription, anUpdatedCategory.getDescription());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_exception_Then_should_verify_isActive_was_passed_to_gateway() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            final var expectedIsActive = true;
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedIsActive, anUpdatedCategory.isActive());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_exception_Then_should_verify_createdAt_was_passed_to_gateway() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_exception_Then_should_verify_updatedAt_was_passed_to_gateway() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return anUpdatedCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_exception_Then_should_verify_deletedAt_was_passed_to_gateway() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", " ");
            final var expectedId = aCategory.getId();
            final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription);
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            updateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.isNull(anUpdatedCategory.getDeletedAt());
            }));
        }
    }

    @Nested
    @DisplayName("Update with invalid id")
    class UpdateWithInvalidId {

        @BeforeEach
        void cleanUp() {
            reset(categoryGateway);
        }

        @Test
        void Given_a_command_with_invalid_id_When_calls_update_category_Then_should_return_not_found_exception() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedId = "123";
            final var aCommand = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription);
            when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                    .thenReturn(Optional.empty());
            final Executable invokeInvalidMethod = () -> updateCategoryUseCase.execute(aCommand);
            final var expectedErrorMessage = "Category with ID %s was not found"
                    .formatted(expectedId);
            // when
            final var actualException = assertThrows(NotFoundException.class, invokeInvalidMethod);
            // then
            assertEquals(expectedErrorMessage, actualException.getMessage());
        }

        @Test
        void Given_a_command_with_invalid_id_When_calls_update_Then_should_verify_findById_was_invoked_only_once() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedId = "123";
            final var aCommand = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription);
            when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                    .thenReturn(Optional.empty());
            final Executable invokeInvalidMethod = () -> updateCategoryUseCase.execute(aCommand);
            // when
            assertThrows(DomainException.class, invokeInvalidMethod);
            // then
            verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));
        }

        @Test
        void Given_a_command_with_invalid_id_When_calls_update_category_verify_that_gateway_update_was_never_invoked() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedId = "123";
            final var aCommand = UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription);
            when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                    .thenReturn(Optional.empty());
            final Executable invokeInvalidMethod = () -> updateCategoryUseCase.execute(aCommand);
            // when
            assertThrows(DomainException.class, invokeInvalidMethod);
            // then
            verify(categoryGateway, never()).update(any(Category.class));
        }
    }
}
