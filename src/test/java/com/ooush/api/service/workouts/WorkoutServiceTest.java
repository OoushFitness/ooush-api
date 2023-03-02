package com.ooush.api.service.workouts;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.jdbc.Work;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class WorkoutServiceTest {

    @InjectMocks
    private WorkoutServiceImpl target = new WorkoutServiceImpl();

    @BeforeEach
    void setUp() {
    }

    @Test
    void getDashboardWorkouts() {
        target.getDashboardWorkouts();
    }

    @Test
    void setUserWorkoutDayTitle() {
    }
}