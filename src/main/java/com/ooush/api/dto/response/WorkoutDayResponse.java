package com.ooush.api.dto.response;

import com.ooush.api.dto.mapper.ExerciseMapper;

import java.util.List;

public class WorkoutDayResponse {

	private String day;
	private Integer dayId;
	private boolean weekday;
	private String name;
	private List<ExerciseMapper> exercises;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public boolean isWeekday() {
		return weekday;
	}

	public void setWeekday(boolean weekday) {
		this.weekday = weekday;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ExerciseMapper> getExercises() {
		return exercises;
	}

	public void setExercises(List<ExerciseMapper> exercises) {
		this.exercises = exercises;
	}
}
