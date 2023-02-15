package com.ooush.api.controller;

import com.ooush.api.dto.request.ExerciseRequest;
import com.ooush.api.dto.request.UpdateUserExerciseRequest;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.OoushResponseMap;
import com.ooush.api.service.exercise.ExerciseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/exercise"})
public class ExerciseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExerciseController.class);

	@Autowired
	ExerciseServiceImpl exerciseService;

	@PostMapping(value = "/fetch")
	public OoushResponseEntity fetchExercises(@RequestBody ExerciseRequest exerciseRequest) {
		LOGGER.info("Resource /exercise/fetch/ POST called");
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(exerciseService.fetchExercises(exerciseRequest)).construct());
	}

	@PostMapping(value = "/update-user-exercise")
	public OoushResponseEntity updateUserExercise(@RequestBody UpdateUserExerciseRequest updateUserExerciseRequest) {
		LOGGER.info("Resource /exercise/update-user-exercise POST called");
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(exerciseService.updateUserExercise(updateUserExerciseRequest)).construct());
	}

	@DeleteMapping(value = "/remove-user-exercise/{exerciseId}/{exerciseDayId}")
	public OoushResponseEntity removeUserExercise(@PathVariable Integer exerciseId, @PathVariable Integer exerciseDayId) {
		LOGGER.info("Resource /exercise/remove-user-exercise/ DELETE called");
		LOGGER.debug("Resource /exercise/remove-user-exercise/{}/{} DELETE called", exerciseId, exerciseDayId);
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(exerciseService.removeUserExercise(exerciseId, exerciseDayId)).construct());
	}

}
