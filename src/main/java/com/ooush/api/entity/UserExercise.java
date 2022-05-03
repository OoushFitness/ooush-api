package com.ooush.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "userexercise")
public class UserExercise implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "UserId", nullable = false)
	private Users user;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ExerciseId", nullable = false)
	private Exercise exercise;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ExerciseDayId", nullable = false)
	private ExerciseDay exerciseDay;

	@Column(name = "Weight", nullable = false)
	private Integer weight;

	@Column(name = "Reps", nullable = false)
	private Integer reps;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	public ExerciseDay getExerciseDay() {
		return exerciseDay;
	}

	public void setExerciseDay(ExerciseDay exerciseDay) {
		this.exerciseDay = exerciseDay;
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
}
