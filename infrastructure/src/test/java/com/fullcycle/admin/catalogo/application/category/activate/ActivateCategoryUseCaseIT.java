package com.fullcycle.admin.catalogo.application.category.activate;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.application.category.deactivate.DeactivateCategoryCommand;
import com.fullcycle.admin.catalogo.application.category.deactivate.DeactivateCategoryUseCase;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@IntegrationTest
public class ActivateCategoryUseCaseIT {

    @Autowired
    private ActivateCategoryUseCase activateCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Nested
    @DisplayName("Activate with a valid command")
    class ActivateWithValidCommand {

        @Test
        void Given_a_valid_command_When_calls_activate_category_Then_should_return_an_active_category_id() {
            // given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription)
                    .deactivate();
            final var expectedId = aCategory.getId();
            final var aCommand = ActivateCategoryCommand.with(expectedId.getValue());
            final var expectedIsActive = true;

            assertEquals(0, categoryRepository.count());
            save(aCategory);
            assertEquals(1, categoryRepository.count());

            // when
            final var actualOutput = activateCategoryUseCase.execute(aCommand).get();

            // then
            assertNotNull(actualOutput);
            assertNotNull(actualOutput.id());

            final var actualCategory =
                    categoryRepository.findById(actualOutput.id()).get();

            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertEquals(LocalDate.ofInstant(aCategory.getCreatedAt(), ZoneOffset.UTC),
                    LocalDate.ofInstant(actualCategory.getCreatedAt(), ZoneOffset.UTC));
            assertTrue(actualCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt()));
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
