package com.fullcycle.admin.catalogo.e2e.category;

import com.fullcycle.admin.catalogo.E2ETest;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.models.GetCategoryByIdResponse;
import com.fullcycle.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@E2ETest
@Testcontainers
class CategoryE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);
        System.out.println("Container is running on port %s".formatted(mappedPort));
        registry.add("mysql.port", () -> mappedPort);
    }

    @Nested
    @DisplayName("Create with valid values")
    class CreateWithValidValues {

        @Test
        void As_a_Catalog_Admin_I_Should_be_able_to_create_a_new_category_with_valid_values() throws Exception {
            assertTrue(MYSQL_CONTAINER.isRunning());
            assertEquals(0, categoryRepository.count());

            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedIsActive = true;

            // When
            final var actualId = givenACategory(expectedName, expectedDescription);

            // Then
            final var actualCategory = retrieveACategory(actualId.getValue());

            assertEquals(expectedName, actualCategory.name());
            assertEquals(expectedDescription, actualCategory.description());
            assertEquals(expectedIsActive, actualCategory.active());
            assertNotNull(actualCategory.createdAt());
            assertNotNull(actualCategory.updatedAt());
            assertNull(actualCategory.deletedAt());
        }
    }

    @Nested
    @DisplayName("List categories")
    class ListCategories {

        @Test
        void As_a_Catalog_Admin_I_Should_be_able_to_navigate_to_all_categories() throws Exception {
            assertTrue(MYSQL_CONTAINER.isRunning());
            assertEquals(0, categoryRepository.count());

            // Given
            final var aNameFilmes = "Filmes";
            final var aNameDocumentarios = "Documentários";
            final var aNameSeries = "Series";

            givenACategory(aNameFilmes, " ");
            givenACategory(aNameDocumentarios, " ");
            givenACategory(aNameSeries, " ");

            var expectedPage = 0;
            final var expectedPerPage = 1;
            final var expectedTotal = 3;

            // When & Then
            listCategories(expectedPage, expectedPerPage)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.current_page", is(equalTo(expectedPage))))
                    .andExpect(jsonPath("$.per_page", is(equalTo(expectedPerPage))))
                    .andExpect(jsonPath("$.total", is(equalTo(expectedTotal))))
                    .andExpect(jsonPath("$.items", hasSize(expectedPerPage)))
                    .andExpect(jsonPath("$.items[0].name", is(equalTo(aNameDocumentarios))));

            expectedPage = 1;
            listCategories(expectedPage, expectedPerPage)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.current_page", is(equalTo(expectedPage))))
                    .andExpect(jsonPath("$.per_page", is(equalTo(expectedPerPage))))
                    .andExpect(jsonPath("$.total", is(equalTo(expectedTotal))))
                    .andExpect(jsonPath("$.items", hasSize(expectedPerPage)))
                    .andExpect(jsonPath("$.items[0].name", is(equalTo(aNameFilmes))));

            expectedPage = 2;
            listCategories(expectedPage, expectedPerPage)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.current_page", is(equalTo(expectedPage))))
                    .andExpect(jsonPath("$.per_page", is(equalTo(expectedPerPage))))
                    .andExpect(jsonPath("$.total", is(equalTo(expectedTotal))))
                    .andExpect(jsonPath("$.items", hasSize(expectedPerPage)))
                    .andExpect(jsonPath("$.items[0].name", is(equalTo(aNameSeries))));

            expectedPage = 3;
            listCategories(expectedPage, expectedPerPage)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.current_page", is(equalTo(expectedPage))))
                    .andExpect(jsonPath("$.per_page", is(equalTo(expectedPerPage))))
                    .andExpect(jsonPath("$.total", is(equalTo(expectedTotal))))
                    .andExpect(jsonPath("$.items", hasSize(0)));
        }

        @Test
        void As_a_Catalog_Admin_I_Should_be_able_to_search_between_all_categories() throws Exception {
            assertTrue(MYSQL_CONTAINER.isRunning());
            assertEquals(0, categoryRepository.count());

            // Given
            final var aNameFilmes = "Filmes";
            final var aNameDocumentarios = "Documentários";
            final var aNameSeries = "Series";

            givenACategory(aNameFilmes, " ");
            givenACategory(aNameDocumentarios, " ");
            givenACategory(aNameSeries, " ");

            var expectedPage = 0;
            final var expectedPerPage = 1;
            final var expectedTotal = 1;

            // When & Then
            listCategories(expectedPage, expectedPerPage, "fil")
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.current_page", is(equalTo(expectedPage))))
                    .andExpect(jsonPath("$.per_page", is(equalTo(expectedPerPage))))
                    .andExpect(jsonPath("$.total", is(equalTo(expectedTotal))))
                    .andExpect(jsonPath("$.items", hasSize(expectedPerPage)))
                    .andExpect(jsonPath("$.items[0].name", is(equalTo(aNameFilmes))));

        }

        @Test
        void As_a_Catalog_Admin_I_Should_be_able_to_sort_all_categories_by_descriptionDesc() throws Exception {
            assertTrue(MYSQL_CONTAINER.isRunning());
            assertEquals(0, categoryRepository.count());

            // Given
            final var aNameFilmes = "Filmes";
            final var aNameDocumentarios = "Documentários";
            final var aNameSeries = "Series";

            givenACategory(aNameFilmes, "C");
            givenACategory(aNameDocumentarios, "Z");
            givenACategory(aNameSeries, "A");

            var expectedPage = 0;
            final var expectedPerPage = 3;
            final var expectedTotal = 3;

            // When & Then
            listCategories(expectedPage, expectedPerPage, "", "description", "desc")
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.current_page", is(equalTo(expectedPage))))
                    .andExpect(jsonPath("$.per_page", is(equalTo(expectedPerPage))))
                    .andExpect(jsonPath("$.total", is(equalTo(expectedTotal))))
                    .andExpect(jsonPath("$.items", hasSize(expectedPerPage)))
                    .andExpect(jsonPath("$.items[0].name", is(equalTo(aNameDocumentarios))))
                    .andExpect(jsonPath("$.items[1].name", is(equalTo(aNameFilmes))))
                    .andExpect(jsonPath("$.items[2].name", is(equalTo(aNameSeries))));

        }
    }

    @Nested
    @DisplayName("Get a category with valid id")
    class GetWithValidId {

        @Test
        void As_a_Catalog_Admin_I_Should_be_able_to_get_a_category_by_its_identifier() throws Exception {
            assertTrue(MYSQL_CONTAINER.isRunning());
            assertEquals(0, categoryRepository.count());

            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedIsActive = true;

            // When
            final var actualId = givenACategory(expectedName, expectedDescription);

            // Then
            final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertNotNull(actualCategory.getCreatedAt());
            assertNotNull(actualCategory.getUpdatedAt());
            assertNull(actualCategory.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("Get a category with invalid id")
    class GetWithInvalidId {

        @Test
        void As_a_Catalog_Admin_I_Should_be_able_to_see_a_treated_error_by_getting_a_not_found_category()
                throws Exception {
            assertTrue(MYSQL_CONTAINER.isRunning());
            assertEquals(0, categoryRepository.count());

            // Given
            final var expectedId = "123";
            final var aRequest = get("/categories/{id}", expectedId)
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
            final var expectedErrorMessage = "Category with ID %s was not found"
                    .formatted(expectedId);

            // When & Then
            mockMvc.perform(aRequest)
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is(equalTo(expectedErrorMessage))));
        }
    }

    @Nested
    @DisplayName("Update a category with valid request")
    class UpdateWithValidRequest {

        @Test
        void As_a_Catalog_Admin_I_Should_be_able_to_update_a_category_by_its_identifier() throws Exception {
            assertTrue(MYSQL_CONTAINER.isRunning());
            assertEquals(0, categoryRepository.count());

            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedIsActive = true;

            final var actualId = givenACategory("Movies", " ");

            final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription);

            final var aRequest = put("/categories/{id}", actualId.getValue())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Json.writeValueAsString(aRequestBody));
            // When
            mockMvc.perform(aRequest)
                    .andExpect(status().isOk());

            // Then
            final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertNotNull(actualCategory.getCreatedAt());
            assertNotNull(actualCategory.getUpdatedAt());
            assertNull(actualCategory.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("Activate a category with valid id")
    class ActivateWithValidId {

        @Test
        void As_a_Catalog_Admin_I_Should_be_able_to_activate_a_category_by_its_identifier() throws Exception {
            assertTrue(MYSQL_CONTAINER.isRunning());
            assertEquals(0, categoryRepository.count());

            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedIsActive = true;

            final var actualId = givenACategory(expectedName, expectedDescription);

            final var aRequest = put("/categories/{id}/active", actualId.getValue())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
            // When
            mockMvc.perform(aRequest)
                    .andExpect(status().isOk());

            // Then
            final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertNotNull(actualCategory.getCreatedAt());
            assertNotNull(actualCategory.getUpdatedAt());
            assertNull(actualCategory.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("Deactivate a category with valid id")
    class DeactivateWithValidId {

        @Test
        void As_a_Catalog_Admin_I_Should_be_able_to_deactivate_a_category_by_its_identifier() throws Exception {
            assertTrue(MYSQL_CONTAINER.isRunning());
            assertEquals(0, categoryRepository.count());

            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var expectedIsActive = false;

            final var actualId = givenACategory(expectedName, expectedDescription);

            final var aRequest = put("/categories/{id}/inactive", actualId.getValue())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);
            // When
            mockMvc.perform(aRequest)
                    .andExpect(status().isOk());

            // Then
            final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

            assertEquals(expectedName, actualCategory.getName());
            assertEquals(expectedDescription, actualCategory.getDescription());
            assertEquals(expectedIsActive, actualCategory.isActive());
            assertNotNull(actualCategory.getCreatedAt());
            assertNotNull(actualCategory.getUpdatedAt());
            assertNotNull(actualCategory.getDeletedAt());
        }
    }

    @Nested
    @DisplayName("Delete a category with valid id")
    class DeleteWithValidId {

        @Test
        void As_a_Catalog_Admin_I_Should_be_able_to_delete_a_category_by_its_identifier() throws Exception {
            assertTrue(MYSQL_CONTAINER.isRunning());
            assertEquals(0, categoryRepository.count());

            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";

            final var actualId = givenACategory(expectedName, expectedDescription);

            final var aRequest = delete("/categories/{id}", actualId.getValue())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON);

            // When
            mockMvc.perform(aRequest)
                    .andExpect(status().isNoContent());

            // Then
            final var actualCategory = categoryRepository.findById(actualId.getValue());
            assertTrue(actualCategory.isEmpty());
        }
    }

    private CategoryID givenACategory(final String aName, final String aDescription) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription);

        final var aRequest = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        final var actualId = this.mockMvc.perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/categories/", "");

        return CategoryID.from(actualId);
    }

    private GetCategoryByIdResponse retrieveACategory(final String anId) throws Exception {
        final var aRequest = get("/categories/{id}", anId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mockMvc.perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, GetCategoryByIdResponse.class);
    }

    private ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        final String sort = "";
        final String direction = "";
        return this.listCategories(page, perPage, search, sort, direction);
    }

    private ResultActions listCategories(final int page, final int perPage) throws Exception {
        final String search = "";
        final String sort = "";
        final String direction = "";
        return this.listCategories(page, perPage, search, sort, direction);
    }

    private ResultActions listCategories(
            final int page, final int perPage,
            final String search, final String sort,
            final String direction) throws Exception {
        final var aRequest = get("/categories")
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mockMvc.perform(aRequest);
    }
}
