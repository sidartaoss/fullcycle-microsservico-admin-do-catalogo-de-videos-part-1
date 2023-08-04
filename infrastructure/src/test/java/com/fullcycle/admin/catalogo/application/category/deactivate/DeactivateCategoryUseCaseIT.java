package com.fullcycle.admin.catalogo.application.category.deactivate;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@IntegrationTest
public class DeactivateCategoryUseCaseIT {

    @Autowired
    private DeactivateCategoryUseCase deactivateCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Nested
    @DisplayName("Deactivate with a valid command")
    class DeactivateWithValidCommand {

        @Test
        void Given_a_valid_command_When_calls_deactivate_category_Then_should_return_an_inactive_category_id() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            final var aCommand = DeactivateCategoryCommand.with(expectedId.getValue());
            final var expectedIsActive = false;

            assertEquals(0, categoryRepository.count());

            categoryRepository.save(CategoryJpaEntity.from(aCategory));

            assertEquals(1, categoryRepository.count());

            // when
            final var actualOutput = deactivateCategoryUseCase.execute(aCommand).get();

            // then
            assertNotNull(actualOutput);
            assertNotNull(actualOutput.id());

            final var actualCategory =
                    categoryRepository.findById(actualOutput.id()).get();

            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertNotNull(actualCategory.getCreatedAt());
            assertNotNull(actualCategory.getUpdatedAt());
            assertNotNull(actualCategory.getDeletedAt());

        }
    }
}
