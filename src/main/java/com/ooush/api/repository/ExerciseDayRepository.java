package com.ooush.api.repository;

import com.ooush.api.entity.ExerciseDay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alex Green
 */
@Repository
public interface ExerciseDayRepository extends CrudRepository<ExerciseDay, Integer> {

	List<ExerciseDay> findAll();
	ExerciseDay findByDayId(Integer dayId);

}
