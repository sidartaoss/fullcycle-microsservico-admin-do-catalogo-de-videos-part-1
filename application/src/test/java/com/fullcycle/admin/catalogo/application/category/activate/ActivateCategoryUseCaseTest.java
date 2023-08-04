package com.fullcycle.admin.catalogo.application.category.activate;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.DomainException;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class ActivateCategoryUseCaseTest {

    /**
     * 1. Teste do caminho feliz
     * 2. Teste passando uma propriedade inválida (name)
     * 4. Teste simulando um erro genérico vindo do Gateway
     * 4. Teste ativar categoria passando ID inválido.
     */

    @Mock
    CategoryGateway categoryGateway;

    @InjectMocks
    DefaultActivateCategoryUseCase activateCategoryUseCase;

    @Nested
    @DisplayName("Activate with valid command")
    class ActivateWithValidCommand {

        @BeforeEach
        void init() {
            reset(categoryGateway);
            when(categoryGateway.update(any(Category.class)))
                    .thenAnswer(returnsFirstArg());
        }

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_return_an_ouput() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            final var actualOutput = activateCategoryUseCase.execute(aCommand).get();
            // then
            assertNotNull(actualOutput);
        }

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_return_an_ouput_with_category_id() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            final var actualOutput = activateCategoryUseCase.execute(aCommand).get();
            // then
            assertNotNull(actualOutput.id());
        }

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_verify_that_gateway_findById_was_invoked_only_once() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway, times(1)).findById(any(CategoryID.class));
        }

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_verify_that_gateway_update_was_invoked_only_once() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway, times(1)).update(any(Category.class));
        }

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_verify_argument_category_id_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedId, anUpdatedCategory.getId());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_verify_argument_category_name_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedName, anUpdatedCategory.getName());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_verify_argument_category_description_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedDescription, anUpdatedCategory.getDescription());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_verify_argument_category_isActive_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            final var expectedIsActive = true;
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedIsActive, anUpdatedCategory.isActive());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_verify_argument_category_createdAt_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_verify_argument_category_updatedAt_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return anUpdatedCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_verify_argument_category_deletedAt_was_is_null() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.isNull(anUpdatedCategory.getDeletedAt());
            }));
        }
    }

    @Nested
    @DisplayName("Activate with generic error from gateway")
    class ActivateWithGenericErrorFromGateway {

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
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            final var expectedErrorMessage = GATEWAY_ERROR;
            // when
            final var notification = activateCategoryUseCase.execute(aCommand).getLeft();
            // then
            assertEquals(expectedErrorMessage, notification.firstError().message());
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_that_gateway_update_was_invoked_only_once() {
            // given
            final String expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway, times(1)).update(any(Category.class));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_id_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedId, anUpdatedCategory.getId());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_name_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedName, anUpdatedCategory.getName());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_description_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedDescription, anUpdatedCategory.getDescription());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_isActive_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            final var expectedIsActive = true;
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(expectedIsActive, anUpdatedCategory.isActive());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_createdAt_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.equals(aCategory.getCreatedAt(), anUpdatedCategory.getCreatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_updatedAt_was_passed_to_gateway_update() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(Category.with(aCategory)));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return anUpdatedCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt());
            }));
        }

        @Test
        void Given_a_valid_command_When_gateway_throws_random_exception_Then_should_verify_argument_category_deletedAt_is_null() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            when(categoryGateway.findById(eq(expectedId)))
                    .thenReturn(Optional.of(aCategory));
            // when
            activateCategoryUseCase.execute(aCommand);
            // then
            verify(categoryGateway).update(argThat(anUpdatedCategory -> {
                return Objects.isNull(anUpdatedCategory.getDeletedAt());
            }));
        }
    }

    @Nested
    @DisplayName("Activate with invalid id")
    class ActivateWithInvalidId {

        @BeforeEach
        void cleanUp() {
            reset(categoryGateway);
        }

        @Test
        void Given_a_command_with_invalid_id_When_calls_activate_category_Then_should_return_not_found_exception() {
            // given
            final var expectedId = "123";
            final var aCommand = ActivateCategoryCommand.with(expectedId);
            when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                    .thenReturn(Optional.empty());
            final Executable invokeInvalidMethod = () -> activateCategoryUseCase.execute(aCommand);
            final var expectedErrorMessage = "Category with ID %s was not found"
                    .formatted(expectedId);
            // when
            final var actualException = assertThrows(DomainException.class, invokeInvalidMethod);
            // then
            assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        }

        @Test
        void Given_a_command_with_invalid_id_When_calls_activate_category_Then_should_return_error_count_as_1() {
            // given
            final var expectedId = "123";
            final var aCommand = ActivateCategoryCommand.with(expectedId);
            when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                    .thenReturn(Optional.empty());
            final Executable invokeInvalidMethod = () -> activateCategoryUseCase.execute(aCommand);
            final var expectedErrorCount = 1;
            // when
            final var actualException = assertThrows(DomainException.class, invokeInvalidMethod);
            // then
            assertEquals(expectedErrorCount, actualException.getErrors().size());
        }

        @Test
        void Given_a_command_with_invalid_id_When_calls_activate_category_Then_should_verify_that_gateway_findById_was_invoked_only_once() {
            // given
            final var expectedId = "123";
            final var aCommand = ActivateCategoryCommand.with(expectedId);
            when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                    .thenReturn(Optional.empty());
            final Executable invokeInvalidMethod = () -> activateCategoryUseCase.execute(aCommand);
            // when
            assertThrows(DomainException.class, invokeInvalidMethod);
            // then
            verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));
        }

        @Test
        void Given_a_command_with_invalid_id_When_calls_activate_category_verify_that_gateway_update_was_never_invoked() {
            // given
            final var expectedId = "123";
            final var aCommand = ActivateCategoryCommand.with(expectedId);
            when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
                    .thenReturn(Optional.empty());
            final Executable invokeInvalidMethod = () -> activateCategoryUseCase.execute(aCommand);
            // when
            assertThrows(DomainException.class, invokeInvalidMethod);
            // then
            verify(categoryGateway, never()).update(any(Category.class));
        }
    }
}
