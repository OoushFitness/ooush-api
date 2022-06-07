package com.ooush.api.service.exercise;

import com.ooush.api.dto.request.ExerciseRequest;
import com.ooush.api.dto.response.ExerciseResponse;

import java.util.List;

public interface ExerciseService {

	List<ExerciseResponse> fetchExercises(ExerciseRequest exerciseRequest);

}
