package com.ooush.api.service.workouts;

import com.ooush.api.dto.mapper.ExerciseMapper;
import com.ooush.api.dto.request.SetUserWorkoutDayRequest;
import com.ooush.api.dto.response.WorkoutDayResponse;
import com.ooush.api.entity.ExerciseDay;
import com.ooush.api.entity.UserWorkoutDay;
import com.ooush.api.entity.UserExercise;
import com.ooush.api.entity.Users;
import com.ooush.api.repository.ExerciseDayRepository;
import com.ooush.api.repository.UserExerciseRepository;
import com.ooush.api.repository.UserWorkoutDayRepository;
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

	@Autowired
	private UserWorkoutDayRepository userWorkoutDayRepository;


	@Override
	public List<WorkoutDayResponse> getDashboardWorkouts() {

		List<WorkoutDayResponse> dashboardWorkoutWeek = new ArrayList<>();
		Users currentLoggedInUser = userService.getCurrentLoggedInUser();
		List<UserWorkoutDay> userWorkoutDays = userWorkoutDayRepository.findAllByUser(currentLoggedInUser);

		for (UserWorkoutDay userWorkoutDay : userWorkoutDays) {
			WorkoutDayResponse workoutDay = new WorkoutDayResponse();
			populateWorkoutDayOfWeekDetails(workoutDay, userWorkoutDay);
			workoutDay.setExercises(mapWorkoutDaysToUserExercises(currentLoggedInUser, userWorkoutDay));
			dashboardWorkoutWeek.add(workoutDay);
		}
		return dashboardWorkoutWeek;
	}

	private void populateWorkoutDayOfWeekDetails(WorkoutDayResponse workoutDay, UserWorkoutDay userWorkoutDay) {
		ExerciseDay exerciseDay = userWorkoutDay.getExerciseDay();
		workoutDay.setName(userWorkoutDay.getName());
		workoutDay.setDay(exerciseDay.getName());
		workoutDay.setDayId(exerciseDay.getDayId());
		workoutDay.setWeekday(exerciseDay.isWeekday());
	}

	private List<ExerciseMapper> mapWorkoutDaysToUserExercises(Users currentLoggedInUser, UserWorkoutDay userWorkoutDay) {
		List<ExerciseMapper> exerciseList = new ArrayList<>();
		List<UserExercise> userExercises = userExerciseRepository.findAllByUserAndExerciseDay(currentLoggedInUser, userWorkoutDay.getExerciseDay());

		for(UserExercise userExercise : userExercises) {
			ExerciseMapper exercise = new ExerciseMapper();
			exercise.setId(userExercise.getExercise().getId());
			exercise.setName(userExercise.getExercise().getName());
			exercise.setReps(userExercise.getReps());
			exercise.setWeight(userExercise.getWeight());
			exercise.setSets(userExercise.getSets());

			exerciseList.add(exercise);
		}
		return exerciseList;
	}

	@Override
	public UserWorkoutDay setUserWorkoutDayTitle(SetUserWorkoutDayRequest setWorkoutDayTitleRequest) {
		Users currentLoggedInUser = userService.getCurrentLoggedInUser();
		ExerciseDay exerciseDay = exerciseDayRepository.findByDayId(setWorkoutDayTitleRequest.getWorkoutDayId());
		UserWorkoutDay userWorkoutDay =
				userWorkoutDayRepository.findByUserAndExerciseDay(currentLoggedInUser, exerciseDay);
		if (userWorkoutDay == null) {
			userWorkoutDay = new UserWorkoutDay();
			userWorkoutDay.setExerciseDay(exerciseDay);
			userWorkoutDay.setUser(currentLoggedInUser);
		}
		userWorkoutDay.setName(setWorkoutDayTitleRequest.getName());
		return userWorkoutDayRepository.save(userWorkoutDay);
	}
}
