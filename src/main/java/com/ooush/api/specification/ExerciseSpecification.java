package com.ooush.api.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import com.ooush.api.dto.request.ExerciseRequest;
import com.ooush.api.entity.Exercise;

public class ExerciseSpecification implements Specification<Exercise> {

    private final transient ExerciseRequest filter;

    public ExerciseSpecification(ExerciseRequest exerciseRequest) {
        super();
        this.filter = exerciseRequest;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Exercise> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {

        final List<Predicate> predicates = new ArrayList<>();

        if (filter.getSearchName() != null) {
            predicates.add(cb.like(root.get("name"), "%" + filter.getSearchName() + "%"));
        }

        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
