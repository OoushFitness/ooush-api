package com.ooush.api.service.workouts;


import com.ooush.api.dto.request.SetUserWorkoutDayRequest;
import com.ooush.api.dto.response.WorkoutDayResponse;
import com.ooush.api.entity.UserWorkoutDay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface WorkoutService {

	Logger LOGGER = LoggerFactory.getLogger(WorkoutService.class);

	List<WorkoutDayResponse> getDashboardWorkouts();
	UserWorkoutDay setUserWorkoutDayTitle(SetUserWorkoutDayRequest setWorkoutDayTitleRequest);

}
