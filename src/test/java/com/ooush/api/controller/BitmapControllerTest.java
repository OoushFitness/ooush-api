package com.ooush.api.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.google.common.collect.ImmutableMap;
import com.ooush.api.dto.response.BitmapSearchParameterResponse;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.UserSettingsResponse;
import com.ooush.api.service.bitmap.BitmapServiceImpl;

class BitmapControllerTest {

    @Mock
    BitmapServiceImpl bitmapService;

    @InjectMocks
    private BitmapController target = new BitmapController();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("fetchSearchOptionsShouldReturnBitmapSearchParameterResponse")
    @Test
    void fetchSearchOptions() {
        when(bitmapService.fetchSearchOptions()).thenReturn(new BitmapSearchParameterResponse());

        OoushResponseEntity response = target.fetchSearchOptions();
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap) response.getBody()).get("data"), Matchers.instanceOf(BitmapSearchParameterResponse.class));
    }
}