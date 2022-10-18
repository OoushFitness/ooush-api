package com.ooush.api.service.exercise;

import com.ooush.api.dto.request.ExerciseRequest;
import com.ooush.api.dto.request.UpdateUserExerciseRequest;
import com.ooush.api.dto.response.ExerciseResponse;
import com.ooush.api.entity.UserExercise;

import java.util.List;

public interface ExerciseService {

	List<ExerciseResponse> fetchExercises(ExerciseRequest exerciseRequest);
	UserExercise updateUserExercise(UpdateUserExerciseRequest updateUserExerciseRequest);

	void removeUserExercise(Integer exerciseId, Integer exerciseDayId);

}
