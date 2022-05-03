package com.ooush.api.service.workouts;

import com.ooush.api.dto.mapper.ExerciseMapper;
import com.ooush.api.dto.response.WorkoutDayResponse;
import com.ooush.api.entity.ExerciseDay;
import com.ooush.api.entity.UserExercise;
import com.ooush.api.entity.Users;
import com.ooush.api.repository.ExerciseDayRepository;
import com.ooush.api.repository.UserExerciseRepository;
import com.ooush.api.service.users.BasicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WorkoutServiceImpl implements WorkoutService {

	@Autowired
	private BasicUserService userService;

	@Autowired
	private ExerciseDayRepository exerciseDayRepository;

	@Autowired
	private UserExerciseRepository userExerciseRepository;

	@Override
	public List<WorkoutDayResponse> getDashboardWorkouts() {

		List<WorkoutDayResponse> workoutDays = new ArrayList<>();
		Users currentLoggedInUser = userService.getCurrentLoggedInUser();
		List<ExerciseDay> exerciseDays = exerciseDayRepository.findAll();

		for (ExerciseDay exerciseDay : exerciseDays) {
			WorkoutDayResponse workoutDay = new WorkoutDayResponse();
			populateWorkoutDayOfWeekDetails(workoutDay, exerciseDay);
			workoutDay.setExercises(mapWorkoutDaysToUserExercises(currentLoggedInUser, exerciseDay));
			workoutDays.add(workoutDay);
		}
		return workoutDays;
	}

	private void populateWorkoutDayOfWeekDetails(WorkoutDayResponse workoutDay, ExerciseDay exerciseDay) {
		workoutDay.setDay(exerciseDay.getName());
		workoutDay.setDayId(exerciseDay.getDayId());
		workoutDay.setWeekday(exerciseDay.isWeekday());
	}

	private List<ExerciseMapper> mapWorkoutDaysToUserExercises(Users currentLoggedInUser, ExerciseDay exerciseDay) {
		List<ExerciseMapper> exerciseList = new ArrayList<>();
		List<UserExercise> userExercises = userExerciseRepository.findAllByUserAndExerciseDay(currentLoggedInUser, exerciseDay);

		for(UserExercise userExercise : userExercises) {
			ExerciseMapper exercise = new ExerciseMapper();
			exercise.setName(userExercise.getExercise().getName());
			exercise.setReps(userExercise.getReps());
			exercise.setWeight(userExercise.getWeight());

			exerciseList.add(exercise);
		}
		return exerciseList;
	}
}
