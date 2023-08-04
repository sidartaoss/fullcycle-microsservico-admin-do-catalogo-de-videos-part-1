package com.fullcycle.admin.catalogo.infrastructure.category.persistence;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Nested
    @DisplayName("Save with an invalid category")
    class SaveWithInvalidCategory {

        @Test
        void Given_an_invalid_null_name_When_calls_save_Then_should_return_an_error() {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);

            final var anEntity = CategoryJpaEntity.from(aCategory);
            anEntity.setName(null);

            final var expectedMessage = "could not execute statement [NULL not allowed for column \"NAME\"; SQL statement:\n" +
                    "insert into category (active,created_at,deleted_at,description,name,updated_at,id) values (?,?,?,?,?,?,?) [23502-214]] [insert into category (active,created_at,deleted_at,description,name,updated_at,id) values (?,?,?,?,?,?,?)]";

            // When
            Executable invalidMethodCall = () -> repository.saveAndFlush(anEntity);
            // Then
            final var actualException = assertThrows(DataIntegrityViolationException.class, invalidMethodCall);
            final var actualCause = assertInstanceOf(ConstraintViolationException.class, actualException.getCause());
            assertEquals(expectedMessage, actualCause.getMessage());
        }

        @Test
        void Given_an_invalid_null_createdAt_When_calls_save_Then_should_return_an_error() {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);

            final var anEntity = CategoryJpaEntity.from(aCategory);
            anEntity.setCreatedAt(null);

            final var expectedMessage = "could not execute statement [NULL not allowed for column \"CREATED_AT\"; SQL statement:\n" +
                    "insert into category (active,created_at,deleted_at,description,name,updated_at,id) values (?,?,?,?,?,?,?) [23502-214]] [insert into category (active,created_at,deleted_at,description,name,updated_at,id) values (?,?,?,?,?,?,?)]";

            // When
            Executable invalidMethodCall = () -> repository.saveAndFlush(anEntity);
            // Then
            final var actualException = assertThrows(DataIntegrityViolationException.class, invalidMethodCall);
            final var actualCause = assertInstanceOf(ConstraintViolationException.class, actualException.getCause());
            assertEquals(expectedMessage, actualCause.getMessage());
        }

        @Test
        void Given_an_invalid_null_updatedAt_When_calls_save_Then_should_return_an_error() {
            // Given
            final var expectedName = "Filmes";
            final var expectedDescription = "A categoria mais assistida";
            final var aCategory = Category.newCategory(expectedName, expectedDescription);

            final var anEntity = CategoryJpaEntity.from(aCategory);
            anEntity.setUpdatedAt(null);

            final var expectedMessage = "could not execute statement [NULL not allowed for column \"UPDATED_AT\"; SQL statement:\n" +
                    "insert into category (active,created_at,deleted_at,description,name,updated_at,id) values (?,?,?,?,?,?,?) [23502-214]] [insert into category (active,created_at,deleted_at,description,name,updated_at,id) values (?,?,?,?,?,?,?)]";

            // When
            Executable invalidMethodCall = () -> repository.saveAndFlush(anEntity);
            // Then
            final var actualException = assertThrows(DataIntegrityViolationException.class, invalidMethodCall);
            final var actualCause = assertInstanceOf(ConstraintViolationException.class, actualException.getCause());
            assertEquals(expectedMessage, actualCause.getMessage());
        }
    }
}
