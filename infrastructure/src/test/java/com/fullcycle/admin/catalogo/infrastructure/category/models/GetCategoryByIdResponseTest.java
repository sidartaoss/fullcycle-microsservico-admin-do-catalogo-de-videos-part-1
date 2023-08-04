package com.fullcycle.admin.catalogo.infrastructure.category.models;

import com.fullcycle.admin.catalogo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

@JacksonTest
class GetCategoryByIdResponseTest {

    @Autowired
    private JacksonTester<GetCategoryByIdResponse> json;

    @Test
    void testMarshal() throws IOException {
        // Given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var now = Instant.now();
        final var expectedCreatedAt = now;
        final var expectedUpdatedAt = now;
        final var expectedDeleteddAt = now;
        final var response = new GetCategoryByIdResponse(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeleteddAt
        );
        // When
        final var actualJson = this.json.write(response);
        // Then
        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedIsActive)
                .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString())
                .hasJsonPathValue("$.deleted_at", expectedDeleteddAt.toString());
    }

    @Test
    void testUnmarshal() throws IOException {
        // Given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var now = Instant.now();
        final var expectedCreatedAt = now;
        final var expectedUpdatedAt = now;
        final var expectedDeleteddAt = now;
        final var json = """
                {
                    "id": "%s",
                    "name": "%s",
                    "description": "%s",
                    "is_active": %s,
                    "created_at": "%s",
                    "updated_at": "%s",
                    "deleted_at": "%s"
                }
                """
                .formatted(expectedId,
                        expectedName,
                        expectedDescription,
                        expectedIsActive,
                        expectedCreatedAt.toString(),
                        expectedUpdatedAt.toString(),
                        expectedDeleteddAt.toString());
        // When
        final var actualJson = this.json.parse(json);
        // Then
        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive)
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
                .hasFieldOrPropertyWithValue("deletedAt", expectedDeleteddAt);
    }
}
