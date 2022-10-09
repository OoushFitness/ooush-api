package com.ooush.api.service.exercise;

import com.ooush.api.constants.OoushConstants;
import com.ooush.api.dto.request.ExerciseRequest;
import com.ooush.api.dto.request.UpdateUserExerciseRequest;
import com.ooush.api.dto.response.ExerciseResponse;
import com.ooush.api.entity.Exercise;
import com.ooush.api.entity.ExerciseDay;
import com.ooush.api.entity.UserExercise;
import com.ooush.api.entity.Users;
import com.ooush.api.repository.ExerciseDayRepository;
import com.ooush.api.repository.ExerciseRepository;
import com.ooush.api.repository.UserExerciseRepository;
import com.ooush.api.service.users.UserService;

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

	@Autowired
	private UserExerciseRepository userExerciseRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ExerciseDayRepository exerciseDayRepository;

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

	@Override
	public UserExercise updateUserExercise(UpdateUserExerciseRequest updateUserExerciseRequest) {
		// Add new custom user exercise if no exercise id present
		Exercise exercise;
		Users currentLoggedInUser = userService.getCurrentLoggedInUser();
		ExerciseDay exerciseDay = exerciseDayRepository.findByDayId(updateUserExerciseRequest.getExerciseDayId());
		if (updateUserExerciseRequest.getExerciseId() == null) {
			 exercise = addCustomExercise(updateUserExerciseRequest);
		} else {
			exercise = exerciseRepository.findUniqueById(updateUserExerciseRequest.getExerciseId());
		}

		// Add new user exercise mapper if none found by user id, day and exercise, otherwise update
		UserExercise userExercise = userExerciseRepository.findByUserAndExerciseDayAndExercise(
				currentLoggedInUser,
				exerciseDay,
				exercise
		);
		if (userExercise == null) {
			userExercise = new UserExercise();
			userExercise.setExercise(exercise);
			userExercise.setExerciseDay(exerciseDay);
			userExercise.setUser(currentLoggedInUser);
		}
		userExercise.setReps(updateUserExerciseRequest.getReps());
		userExercise.setWeight(updateUserExerciseRequest.getWeight());
		return userExerciseRepository.save(userExercise);
	}

	private Exercise addCustomExercise(UpdateUserExerciseRequest updateUserExerciseRequest) {
		Exercise customExercise = new Exercise();
		customExercise.setName(updateUserExerciseRequest.getName());
		customExercise.setBitmap(OoushConstants.CUSTOM_EXERCISE_BITMAP_INTEGER);
		return exerciseRepository.save(customExercise);
	}
}
