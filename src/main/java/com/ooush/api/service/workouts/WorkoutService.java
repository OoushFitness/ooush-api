package com.ooush.api.service.workouts;


import com.ooush.api.dto.response.WorkoutDayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public interface WorkoutService {

	Logger LOGGER = LoggerFactory.getLogger(WorkoutService.class);

	List<WorkoutDayResponse> getDashboardWorkouts();

}
