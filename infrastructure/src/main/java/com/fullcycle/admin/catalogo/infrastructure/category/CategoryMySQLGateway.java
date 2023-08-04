package com.fullcycle.admin.catalogo.infrastructure.category;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.category.CategorySearchQuery;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(final CategoryRepository repository) {
        Objects.requireNonNull(repository);
        this.repository = repository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(final CategoryID anId) {
        final var anIdValue = anId.getValue();
        final var exists = this.repository.existsById(anIdValue);
        if (exists) {
            this.repository.deleteById(anIdValue);
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return this.repository.findById(anId.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    private Category save(final Category aCategory) {
        return this.repository.save(CategoryJpaEntity.from(aCategory))
                .toAggregate();
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        final var page = aQuery.page();
        final var perPage = aQuery.perPage();
        final var sort = Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort());
        final var pageRequest = PageRequest.of(page, perPage, sort);
        final var terms = aQuery.terms();
        final var specifications = isTermsNotInformed(terms) ?
                filterOneEqualsOne() : filterLikeNameOrDescription(terms);

        final Page<CategoryJpaEntity> pageResult = this.repository.findAll(Specification
                .where(specifications), pageRequest);
        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult
                        .map(CategoryJpaEntity::toAggregate)
                        .toList()
        );
    }

    private Specification<CategoryJpaEntity> filterLikeNameOrDescription(String terms) {
        return SpecificationUtils.<CategoryJpaEntity>like("name", terms).or(
                SpecificationUtils.like("description", terms));
    }

    private Specification<CategoryJpaEntity> filterOneEqualsOne() {
        return SpecificationUtils.<CategoryJpaEntity>oneEqualsOne();
    }

    private boolean isTermsNotInformed(String terms) {
        return terms == null || terms.isBlank();
    }
}
