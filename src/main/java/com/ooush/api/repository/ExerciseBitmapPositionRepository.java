package com.ooush.api.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ooush.api.entity.ExerciseBitmapPosition;

@Repository
public interface ExerciseBitmapPositionRepository extends CrudRepository<ExerciseBitmapPosition, Integer> {

    List<ExerciseBitmapPosition> findAll();

}
