package com.ooush.api.service.exercise;

import com.ooush.api.dto.request.ExerciseRequest;
import com.ooush.api.dto.response.ExerciseResponse;
import com.ooush.api.entity.Exercise;
import com.ooush.api.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ExerciseServiceImpl implements ExerciseService {

	@Autowired
	private ExerciseRepository exerciseRepository;

	@Override
	public List<ExerciseResponse> fetchExercises(ExerciseRequest exerciseRequest) {
		List<ExerciseResponse> exerciseResponseList = new ArrayList<>();
		List<Exercise> exerciseList = exerciseRepository.findAll();
		for (Exercise exercise : exerciseList) {
			ExerciseResponse exerciseResponse = new ExerciseResponse();
			exerciseResponse.setId(exercise.getId());
			exerciseResponse.setName(exercise.getName());
			exerciseResponseList.add(exerciseResponse);
		}
		return exerciseResponseList;
	}

}
