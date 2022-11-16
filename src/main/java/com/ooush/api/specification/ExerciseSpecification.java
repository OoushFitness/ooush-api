package com.ooush.api.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.ooush.api.dto.request.ExerciseRequest;
import com.ooush.api.entity.Exercise;

public class ExerciseSpecification implements Specification<Exercise> {

    private ExerciseRequest searchCriteria;

    public ExerciseSpecification(ExerciseRequest exerciseRequest) {
        this.searchCriteria = exerciseRequest;
    }

    @Override
    public Predicate toPredicate(Root<Exercise> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        final List<Predicate> predicates = new ArrayList<>();

        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
