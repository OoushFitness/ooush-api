package com.ooush.api.dto.mapper;

public class ExerciseMapper {

	private String name;
	private Integer exerciseId;
	private Integer weight;
	private Integer reps;
	private Integer sets;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(Integer exerciseId) {
		this.exerciseId = exerciseId;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Integer getReps() {
		return reps;
	}

	public void setReps(Integer reps) {
		this.reps = reps;
	}

	public Integer getSets() {
		return sets;
	}

	public void setSets(Integer sets) {
		this.sets = sets;
	}
}
