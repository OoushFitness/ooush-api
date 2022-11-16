package com.ooush.api.dto.request;

public class UpdateUserExerciseRequest {

    private Integer exerciseId;
    private Integer exerciseDayId;
    private String name;
    private Integer weight;
    private Integer reps;
    private Integer sets;

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getExerciseDayId() {
        return exerciseDayId;
    }

    public void setExerciseDayId(Integer exerciseDayId) {
        this.exerciseDayId = exerciseDayId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
