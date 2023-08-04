package com.fullcycle.admin.catalogo.infrastructure.category;

import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository repository;

    @Nested
    @DisplayName("Create with a valid category")
    class CreateWithValidCategory {

        @Test
        void Given_a_valid_category_When_calls_create_Then_should_return_a_new_category() {
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            final var expectedIsActive = true;

            assertEquals(0, repository.count());

            final var actualCategory = categoryGateway.create(aCategory);

            assertEquals(1, repository.count());

            assertEquals(expectedId, actualCategory.getId());
            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
            assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
            assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
            assertNull(actualCategory.getDeletedAt());

            final CategoryJpaEntity actualEntity = repository.findById(expectedId.getValue()).get();

            assertEquals(expectedId.getValue(), actualEntity.getId());
            assertEquals(expectedName, actualEntity.getName());
            assertEquals(expectedDescription, actualEntity.getDescription());
            assertEquals(expectedIsActive, actualEntity.isActive());
            assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
            assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
            assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
            assertNull(actualEntity.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("Update with a valid category")
    class UpdateWithValidCategory {

        @Test
        void Given_a_valid_category_When_calls_update_Then_should_return_an_updated_category() {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory("Film", "A categoria m");
            final var expectedId = aCategory.getId();
            final var expectedIsActive = true;

            assertEquals(0, repository.count());

            repository.save(CategoryJpaEntity.from(aCategory));

            assertEquals(1, repository.count());

            final CategoryJpaEntity actualInvalidEntity = repository.findById(expectedId.getValue()).get();
            assertEquals("Film", actualInvalidEntity.getName());
            assertEquals("A categoria m", actualInvalidEntity.getDescription());
            assertEquals(expectedIsActive, actualInvalidEntity.isActive());

            final var anUpdatedCategory = aCategory.clone().update(expectedName, expectedDescription);

            // When
            final var actualCategory = categoryGateway.update(anUpdatedCategory);

            // Then
            assertEquals(1, repository.count());

            assertEquals(expectedId, actualCategory.getId());
            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertEquals(anUpdatedCategory.getCreatedAt(), actualCategory.getCreatedAt());
            assertTrue(actualCategory.getUpdatedAt().isAfter(aCategory.getUpdatedAt()));
            assertEquals(anUpdatedCategory.getDeletedAt(), actualCategory.getDeletedAt());
            assertNull(actualCategory.getDeletedAt());

            final CategoryJpaEntity actualEntity = repository.findById(expectedId.getValue()).get();

            assertEquals(expectedId.getValue(), actualEntity.getId());
            assertEquals(expectedName, actualEntity.getName());
            assertEquals(expectedDescription, actualEntity.getDescription());
            assertEquals(expectedIsActive, actualEntity.isActive());
            assertEquals(actualCategory.getCreatedAt(), actualEntity.getCreatedAt());
            assertEquals(actualCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
            assertEquals(actualCategory.getDeletedAt(), actualEntity.getDeletedAt());
            assertNull(actualEntity.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("Delete with a valid category id")
    class DeleteWithValidCategoryId {

        @Test
        void Given_a_valid_category_id_When_calls_delete_Then_should_delete_category() {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();

            assertEquals(0, repository.count());

            repository.save(CategoryJpaEntity.from(aCategory));

            assertEquals(1, repository.count());

            // When
            categoryGateway.deleteById(expectedId);

            // Then
            assertEquals(0, repository.count());
        }
    }

    @Nested
    @DisplayName("Delete with an invalid category id")
    class DeleteWithInvalidCategoryId {

        @Test
        void Given_an_invalid_category_id_When_calls_delete_Then_should_not_delete_category() {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);

            assertEquals(0, repository.count());

            repository.save(CategoryJpaEntity.from(aCategory));

            assertEquals(1, repository.count());

            // When
            categoryGateway.deleteById(CategoryID.from("invalid"));

            // Then
            assertEquals(1, repository.count());
        }
    }

    @Nested
    @DisplayName("Get category by a valid id")
    class GetCategoryByValidId {

        @Test
        void Given_a_valid_category_id_When_calls_find_by_id_Then_should_return_category() {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);
            final var expectedId = aCategory.getId();
            final var expectedIsActive = true;

            assertEquals(0, repository.count());

            repository.save(CategoryJpaEntity.from(aCategory));

            assertEquals(1, repository.count());

            // When
            final var actualCategory = categoryGateway.findById(expectedId).get();

            // Then
            assertEquals(1, repository.count());

            assertEquals(expectedId, actualCategory.getId());
            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
            assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
            assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
            assertNull(actualCategory.getDeletedAt());
        }

        @Test
        void Given_a_non_stored_valid_category_id_When_calls_find_by_id_Then_should_return_empty() {
            // Given
            final var nonStoredValidCategoryId = CategoryID.from("empty");

            assertEquals(0, repository.count());

            // When
            final var actualCategory = categoryGateway.findById(nonStoredValidCategoryId);

            // Then
            assertEquals(0, repository.count());
            assertTrue(actualCategory.isEmpty());
        }
    }

    @Nested
    @DisplayName("List paginated categories")
    class ListPaginatedCategories {

        @Test
        void Given_pre_persisted_categories_When_calls_findAll_Then_should_return_paginated() {
            // Given
            final var expectedPage = 0;
            final var expectedPerPage = 1;
            final var expectedTotal = 3;
            final var terms = "";
            final var sort = "name";
            final var direction = "asc";

            final var filmes = Category.newCategory("Filmes", " ");
            final var series = Category.newCategory("Séries", " ");
            final var documentarios = Category.newCategory("Documentários", " ");

            assertEquals(0, repository.count());

            repository.saveAll(List.of(
                    CategoryJpaEntity.from(filmes),
                    CategoryJpaEntity.from(series),
                    CategoryJpaEntity.from(documentarios)
            ));

            assertEquals(expectedTotal, repository.count());

            final var aQuery = new CategorySearchQuery(
                    expectedPage,
                    expectedPerPage,
                    terms,
                    sort,
                    direction
            );

            // When
            final var actualResult = categoryGateway.findAll(aQuery);

            // Then
            assertNotNull(actualResult);
            assertFalse(actualResult.items().isEmpty());

            assertEquals(expectedTotal, actualResult.total());
            assertEquals(expectedPage, actualResult.currentPage());
            assertEquals(expectedPerPage, actualResult.items().size());
            assertEquals(expectedPerPage, actualResult.perPage());

            assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
        }

        @Test
        void Given_empty_categories_table_When_calls_findAll_Then_should_return_empty_page() {
            // Given
            final var expectedPage = 0;
            final var expectedPerPage = 1;
            final var expectedTotal = 0;
            final var terms = "";
            final var sort = "name";
            final var direction = "asc";

            final var aQuery = new CategorySearchQuery(
                    expectedPage,
                    expectedPerPage,
                    terms,
                    sort,
                    direction
            );

            // When
            final var actualResult = categoryGateway.findAll(aQuery);

            // Then
            assertNotNull(actualResult);
            assertTrue(actualResult.items().isEmpty());

            assertEquals(expectedTotal, actualResult.total());
            assertEquals(expectedPage, actualResult.currentPage());
            assertEquals(expectedTotal, actualResult.items().size());
            assertEquals(expectedPerPage, actualResult.perPage());
        }

        @Test
        void Given_a_follow_pagination_When_calls_findAll_with_page_Then_should_return_paginated() {
            /** PAGE 0 **/
            // Given
            var expectedPage = 0;
            final var expectedPerPage = 1;
            final var expectedTotal = 3;
            final var terms = "";
            final var sort = "name";
            final var direction = "asc";

            final var filmes = Category.newCategory("Filmes", " ");
            final var series = Category.newCategory("Séries", " ");
            final var documentarios = Category.newCategory("Documentários", " ");

            assertEquals(0, repository.count());

            repository.saveAll(List.of(
                    CategoryJpaEntity.from(filmes),
                    CategoryJpaEntity.from(series),
                    CategoryJpaEntity.from(documentarios)
            ));

            assertEquals(expectedTotal, repository.count());

            var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, terms, sort, direction);

            // When
            var actualResult = categoryGateway.findAll(aQuery);

            // Then
            assertNotNull(actualResult);
            assertFalse(actualResult.items().isEmpty());

            assertEquals(expectedTotal, actualResult.total());
            assertEquals(expectedPage, actualResult.currentPage());
            assertEquals(expectedPerPage, actualResult.items().size());
            assertEquals(expectedPerPage, actualResult.perPage());

            assertEquals(documentarios.getId(), actualResult.items().get(0).getId());

            /** PAGE 1 **/
            expectedPage = 1;
            aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, terms, sort, direction);

            actualResult = categoryGateway.findAll(aQuery);

            // Then
            assertNotNull(actualResult);
            assertFalse(actualResult.items().isEmpty());

            assertEquals(expectedTotal, actualResult.total());
            assertEquals(expectedPage, actualResult.currentPage());
            assertEquals(expectedPerPage, actualResult.items().size());
            assertEquals(expectedPerPage, actualResult.perPage());

            assertEquals(filmes.getId(), actualResult.items().get(0).getId());

            /** PAGE 2 **/
            expectedPage = 2;
            aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, terms, sort, direction);

            actualResult = categoryGateway.findAll(aQuery);

            // Then
            assertNotNull(actualResult);
            assertFalse(actualResult.items().isEmpty());

            assertEquals(expectedTotal, actualResult.total());
            assertEquals(expectedPage, actualResult.currentPage());
            assertEquals(expectedPerPage, actualResult.items().size());
            assertEquals(expectedPerPage, actualResult.perPage());

            assertEquals(series.getId(), actualResult.items().get(0).getId());
        }

        @Test
        void Given_doc_as_terms_When_calls_findAll_and_terms_matches_category_name_Then_should_return_paginated() {
            // Given
            final var expectedPage = 0;
            final var expectedPerPage = 1;
            final var expectedTotal = 1;
            final var terms = "doc";
            final var sort = "name";
            final var direction = "asc";

            final var filmes = Category.newCategory("Filmes", " ");
            final var series = Category.newCategory("Séries", " ");
            final var documentarios = Category.newCategory("Documentários", " ");

            assertEquals(0, repository.count());

            repository.saveAll(List.of(
                    CategoryJpaEntity.from(filmes),
                    CategoryJpaEntity.from(series),
                    CategoryJpaEntity.from(documentarios)
            ));

            assertEquals(3, repository.count());

            final var aQuery = new CategorySearchQuery(
                    expectedPage,
                    expectedPerPage,
                    terms,
                    sort,
                    direction
            );

            // When
            final var actualResult = categoryGateway.findAll(aQuery);

            // Then
            assertNotNull(actualResult);
            assertFalse(actualResult.items().isEmpty());

            assertEquals(expectedTotal, actualResult.total());
            assertEquals(expectedPage, actualResult.currentPage());
            assertEquals(expectedPerPage, actualResult.items().size());
            assertEquals(expectedPerPage, actualResult.perPage());

            assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
        }

        @Test
        void Given_mais_assistida_as_terms_When_calls_findAll_and_matches_description_Then_should_return_paginated() {
            // Given
            final var expectedPage = 0;
            final var expectedPerPage = 1;
            final var expectedTotal = 1;
            final var terms = "MAIS ASSISTIDA";
            final var sort = "name";
            final var direction = "asc";

            final var filmes = Category.newCategory("Filmes", "A categoria mais assistida");
            final var series = Category.newCategory("Séries", "Uma categoria assistida");
            final var documentarios = Category
                    .newCategory("Documentários", "A categoria menos assistida");

            assertEquals(0, repository.count());

            repository.saveAll(List.of(
                    CategoryJpaEntity.from(filmes),
                    CategoryJpaEntity.from(series),
                    CategoryJpaEntity.from(documentarios)
            ));

            assertEquals(3, repository.count());

            final var aQuery = new CategorySearchQuery(
                    expectedPage,
                    expectedPerPage,
                    terms,
                    sort,
                    direction
            );

            // When
            final var actualResult = categoryGateway.findAll(aQuery);

            // Then
            assertNotNull(actualResult);
            assertFalse(actualResult.items().isEmpty());

            assertEquals(expectedTotal, actualResult.total());
            assertEquals(expectedPage, actualResult.currentPage());
            assertEquals(expectedPerPage, actualResult.items().size());
            assertEquals(expectedPerPage, actualResult.perPage());

            assertEquals(filmes.getId(), actualResult.items().get(0).getId());
        }
    }
}
