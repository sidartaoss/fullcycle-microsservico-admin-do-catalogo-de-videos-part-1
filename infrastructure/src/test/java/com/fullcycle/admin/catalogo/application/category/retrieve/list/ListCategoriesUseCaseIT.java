package com.fullcycle.admin.catalogo.application.category.retrieve.list;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@IntegrationTest
class ListCategoriesUseCaseIT {

    @Autowired
    private ListCategoriesUseCase listCategoriesUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void mockUp() {
        final var categories = Stream.of(
                        Category.newCategory("Filmes", " "),
                        Category.newCategory("Netflix Originals", "Títulos de autoria da Netflix"),
                        Category.newCategory("Amazon Originals", "Títulos de autoria da Amazon Prime"),
                        Category.newCategory("Documentários", " "),
                        Category.newCategory("Sports", " "),
                        Category.newCategory("Kids", "Categoria para crianças"),
                        Category.newCategory("Series", " ")
                )
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Nested
    @DisplayName("List categories with valid params")
    class ListCategoriesWithValidParams {

        @Nested
        @DisplayName("Empty page")
        class EmptyPage {

            @Test
            void Given_a_valid_term_When_term_doesnt_match_pre_persisted_Then_should_return_empty_page() {
                // Given
                final var expectedPage = 0;
                final var expectedPerPage = 10;
                final var expectedTerms = "lajr0llfp 0io90sfe";
                final var expectedSort = "name";
                final var expectedDirection = "asc";
                final var expectedItemsCount = 0;
                final var expectedTotal = 0;

                final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                        expectedDirection);

                // When
                final var actualResult = listCategoriesUseCase.execute(aQuery);

                // Then
                assertNotNull(actualResult);
                assertTrue(actualResult.items().isEmpty());
                assertEquals(expectedItemsCount, actualResult.items().size());

                assertEquals(expectedPage, actualResult.currentPage());
                assertEquals(expectedPerPage, actualResult.perPage());
                assertEquals(expectedTotal, actualResult.total());
            }
        }

        @Nested
        @DisplayName("Categories filtered")
        class CategoriesFiltered {

            @ParameterizedTest
            @CsvSource({
                    "fil,0,10,1,1,Filmes",
                    "net,0,10,1,1,Netflix Originals",
                    "ZON,0,10,1,1,Amazon Originals",
                    "KI,0,10,1,1,Kids",
                    "crianças,0,10,1,1,Kids",
                    "da Amazon,0,10,1,1,Amazon Originals",
            })
            void Given_a_valid_term_When_calls_list_categories_Then_should_return_categories_filtered(
                    final String expectedTerms,
                    final int expectedPage,
                    final int expectedPerPage,
                    final int expectedItemsCount,
                    final long expectedTotal,
                    final String expectedCategoryName
            ) {
                // Given
                final var expectedSort = "name";
                final var expectedDirection = "asc";

                final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                        expectedDirection);

                // When
                final var actualResult = listCategoriesUseCase.execute(aQuery);

                // Then
                assertNotNull(actualResult);
                assertFalse(actualResult.items().isEmpty());
                assertEquals(expectedItemsCount, actualResult.items().size());

                assertEquals(expectedPage, actualResult.currentPage());
                assertEquals(expectedPerPage, actualResult.perPage());
                assertEquals(expectedTotal, actualResult.total());
                assertEquals(expectedCategoryName, actualResult.items().get(0).name());
            }
        }

        @Nested
        @DisplayName("Categories ordered")
        class CategoriesOrdered {

            @ParameterizedTest
            @CsvSource({
                    "name,asc,0,10,7,7,Amazon Originals",
                    "name,desc,0,10,7,7,Sports",
                    "createdAt,asc,0,10,7,7,Filmes",
                    "createdAt,desc,0,10,7,7,Series",
                    "name,asc,0,10,7,7,Amazon Originals",
                    "name,asc,0,10,7,7,Amazon Originals",
            })
            void Given_valid_sort_and_direction_When_calls_list_categories_Then_should_return_categories_ordered(
                    final String expectedSort,
                    final String expectedDirection,
                    final int expectedPage,
                    final int expectedPerPage,
                    final int expectedItemsCount,
                    final long expectedTotal,
                    final String expectedCategoryName
            ) {
                // Given
                final var expectedTerms = "";

                final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                        expectedDirection);

                // When
                final var actualResult = listCategoriesUseCase.execute(aQuery);

                // Then
                assertNotNull(actualResult);
                assertFalse(actualResult.items().isEmpty());
                assertEquals(expectedItemsCount, actualResult.items().size());

                assertEquals(expectedPage, actualResult.currentPage());
                assertEquals(expectedPerPage, actualResult.perPage());
                assertEquals(expectedTotal, actualResult.total());
                assertEquals(expectedCategoryName, actualResult.items().get(0).name());
            }
        }

        @Nested
        @DisplayName("Categories paginated")
        class CategoriesPaginated {

            @ParameterizedTest
            @CsvSource({
                    "0,2,2,7,Amazon Originals;Documentários",
                    "1,2,2,7,Filmes;Kids",
                    "2,2,2,7,Netflix Originals;Series",
                    "3,2,1,7,Sports",
            })
            void Given_valid_sort_and_direction_When_calls_list_categories_Then_should_return_categories_ordered(
                    final int expectedPage,
                    final int expectedPerPage,
                    final int expectedItemsCount,
                    final long expectedTotal,
                    final String expectedCategoriesName
            ) {
                // Given
                final var expectedTerms = "";
                final var expectedSort = "name";
                final var expectedDirection = "asc";

                final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort,
                        expectedDirection);

                // When
                final var actualResult = listCategoriesUseCase.execute(aQuery);

                // Then
                assertNotNull(actualResult);
                assertFalse(actualResult.items().isEmpty());
                assertEquals(expectedItemsCount, actualResult.items().size());

                assertEquals(expectedPage, actualResult.currentPage());
                assertEquals(expectedPerPage, actualResult.perPage());
                assertEquals(expectedTotal, actualResult.total());

                int index = 0;
                for (final String expectedName : expectedCategoriesName.split(";")) {
                    final String actualName = actualResult.items().get(index).name();
                    assertEquals(expectedName, actualName);
                    index++;
                }
            }
        }
    }
}
