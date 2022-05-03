package com.ooush.api.controller;

import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.OoushResponseMap;
import com.ooush.api.service.workouts.WorkoutServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/workouts"})
public class WorkoutController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkoutController.class);

	@Autowired
	private WorkoutServiceImpl workoutService;

	@GetMapping(value = "/get-dashboard-workouts")
	public OoushResponseEntity getDashboardWorkouts() {
		LOGGER.info("Resource /workouts/get-dashboard-workouts GET called");
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(workoutService.getDashboardWorkouts()).construct());
	}

}
