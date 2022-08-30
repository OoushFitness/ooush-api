package com.ooush.api.dto.request;

public class SetUserWorkoutDayRequest {

    private Integer workoutDayId;
    private String name;

    public Integer getWorkoutDayId() {
        return workoutDayId;
    }

    public void setWorkoutDayId(Integer workoutDayId) {
        this.workoutDayId = workoutDayId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
