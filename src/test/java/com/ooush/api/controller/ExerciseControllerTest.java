package com.ooush.api.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
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
import com.ooush.api.dto.request.ExerciseRequest;
import com.ooush.api.dto.request.SetUserWorkoutDayRequest;
import com.ooush.api.dto.request.UpdateUserExerciseRequest;
import com.ooush.api.dto.response.BitmapSearchParameterResponse;
import com.ooush.api.dto.response.ExerciseResponse;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.WorkoutDayResponse;
import com.ooush.api.entity.UserExercise;
import com.ooush.api.entity.UserWorkoutDay;
import com.ooush.api.service.exercise.ExerciseServiceImpl;

class ExerciseControllerTest {

    @Mock
    ExerciseServiceImpl mockExerciseService;

    @InjectMocks
    private ExerciseController target = new ExerciseController();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("fetchExercisesShouldReturnListOfExerciseResponsesAndHttpStatus")
    @Test
    void fetchExercisesShouldReturnListOfExerciseResponsesAndHttpStatus() {
        when(mockExerciseService.fetchExercises(any(ExerciseRequest.class))).thenReturn(new ArrayList<>(
                Collections.singletonList(new ExerciseResponse())));

        OoushResponseEntity response = target.fetchExercises(new ExerciseRequest());
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap<?, ?>) response.getBody()).get("data"), Matchers.instanceOf(ArrayList.class));
        assertThat(((List<?>) ((ImmutableMap<?, ?>) response.getBody()).get("data")).get(0), Matchers.instanceOf(ExerciseResponse.class));
    }

    @DisplayName("updateUserExerciseShouldReturn")
    @Test
    void updateUserExerciseShouldReturn() {
        when(mockExerciseService.updateUserExercise(any(UpdateUserExerciseRequest.class))).thenReturn(new UserExercise());

        OoushResponseEntity response = target.updateUserExercise(new UpdateUserExerciseRequest());
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap<?, ?>) response.getBody()).get("data"), Matchers.instanceOf(UserExercise.class));
    }

    @DisplayName("removeUserExerciseShouldReturnHttpStatus")
    @Test
    void removeUserExerciseShouldReturn() {
        when(mockExerciseService.removeUserExercise(anyInt(), anyInt())).thenReturn(Boolean.TRUE);

        OoushResponseEntity response = target.removeUserExercise(1, 1);
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap<?, ?>) response.getBody()).get("data"), Matchers.equalTo(Boolean.TRUE));
    }
}