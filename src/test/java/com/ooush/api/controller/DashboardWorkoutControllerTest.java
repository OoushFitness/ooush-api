package com.ooush.api.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.google.common.collect.ImmutableMap;
import com.ooush.api.dto.request.SetUserWorkoutDayRequest;
import com.ooush.api.dto.response.BitmapSearchParameterResponse;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.WorkoutDayResponse;
import com.ooush.api.entity.UserWorkoutDay;
import com.ooush.api.service.workouts.WorkoutServiceImpl;

class DashboardWorkoutControllerTest {

    @Mock
    WorkoutServiceImpl workoutService;

    @InjectMocks
    private DashboardWorkoutController target = new DashboardWorkoutController();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("getDashboardWorkoutsShouldReturnListOfWorkoutDayResponsesAndHttpStatus")
    @Test
    void getDashboardWorkoutsShouldReturnListOfWorkoutDayResponsesAndHttpStatus() {
        when(workoutService.getDashboardWorkouts()).thenReturn(new ArrayList<>(
                Collections.singletonList(new WorkoutDayResponse())));

        OoushResponseEntity response = target.getDashboardWorkouts();
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap<?, ?>) response.getBody()).get("data"), Matchers.instanceOf(ArrayList.class));
        assertThat(((List<?>) ((ImmutableMap<?, ?>) response.getBody()).get("data")).get(0), Matchers.instanceOf(WorkoutDayResponse.class));
    }

    @DisplayName("setWorkoutDayTitleShouldReturnUserWorkoutDayResponseAndHttpStatus")
    @Test
    void setWorkoutDayTitleShouldReturnUserWorkoutDayResponseAndHttpStatus() {
        when(workoutService.setUserWorkoutDayTitle(any(SetUserWorkoutDayRequest.class))).thenReturn(new UserWorkoutDay());

        OoushResponseEntity response = target.setWorkoutDayTitle(new SetUserWorkoutDayRequest());
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap<?, ?>) response.getBody()).get("data"), Matchers.instanceOf(UserWorkoutDay.class));
    }
}