package com.ooush.api.repository;

import com.ooush.api.entity.ExerciseDay;
import com.ooush.api.entity.UserExercise;
import com.ooush.api.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExerciseRepository extends CrudRepository<UserExercise, Integer> {

	List<UserExercise> findAllByUserAndExerciseDay(Users user, ExerciseDay exerciseDay);

}
