package com.ooush.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ooush.api.entity.ExerciseDay;
import com.ooush.api.entity.UserWorkoutDay;
import com.ooush.api.entity.Users;

@Repository
public interface UserWorkoutDayRepository extends CrudRepository<UserWorkoutDay, Integer> {

    UserWorkoutDay findByUserAndExerciseDay(Users user, ExerciseDay exerciseDay);

}
