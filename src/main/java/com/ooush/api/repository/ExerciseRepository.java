package com.ooush.api.repository;

import com.ooush.api.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alex Green
 */
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Integer>, JpaSpecificationExecutor<Exercise> {

	List<Exercise> findAll();
	Exercise findUniqueById(Integer id);

}
