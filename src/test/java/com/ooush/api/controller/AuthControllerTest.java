package com.ooush.api.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;

import com.google.common.collect.ImmutableMap;
import com.ooush.api.dto.request.LoginRequest;
import com.ooush.api.dto.response.BitmapSearchParameterResponse;
import com.ooush.api.dto.response.LoginResponse;
import com.ooush.api.dto.response.LogoutResponse;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.VerifyResponse;
import com.ooush.api.security.TokenUtils;
import com.ooush.api.service.authentication.AuthenticationService;

import liquibase.pro.packaged.A;

class AuthControllerTest {

    @Mock
    AuthenticationService mockAuthenticationService;

    @Mock
    TokenUtils mockTokenUtils;

    @Mock
    MockHttpServletRequest mockHttpServletRequest;

    @InjectMocks
    private AuthController target = new AuthController();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        this.mockHttpServletRequest = new MockHttpServletRequest();
    }

    @DisplayName("authenticateLoginShouldReturnHttpStatusOKAndLoginResponseIfLoginSuccessful")
    @Test
    void authenticateLoginShouldReturnHttpStatusOKAndLoginResponseIfLoginSuccessful() {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setSuccess(true);
        when(mockAuthenticationService.authenticateLogin(any(LoginRequest.class))).thenReturn(loginResponse);

        OoushResponseEntity response = target.authenticateLogin(new LoginRequest());
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap) response.getBody()).get("data"), Matchers.instanceOf(LoginResponse.class));
    }

    @DisplayName("authenticateLoginShouldReturnHttpStatusUnauthorisedAndLoginResponseIfLoginFailure")
    @Test
    void authenticateLoginShouldReturnHttpStatusUnauthorisedAndLoginResponseIfLoginFailure() {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setSuccess(false);
        when(mockAuthenticationService.authenticateLogin(any(LoginRequest.class))).thenReturn(loginResponse);

        OoushResponseEntity response = target.authenticateLogin(new LoginRequest());
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.UNAUTHORIZED));
        assertThat(((ImmutableMap) response.getBody()).get("data"), Matchers.instanceOf(LoginResponse.class));
    }

    @DisplayName("logoutShouldReturnHttpStatusOkAndLogoutResponse")
    @Test
    void logoutShouldReturnHttpStatusOkAndLogoutResponse() {
        when(mockAuthenticationService.logout(any(HttpServletRequest.class))).thenReturn(new LogoutResponse());

        OoushResponseEntity response =  target.logout(mockHttpServletRequest);
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap) response.getBody()).get("data"), Matchers.instanceOf(LogoutResponse.class));
    }

    @DisplayName("verifyShouldReturnHttpStatusOkAndVerifyResponseIfVerifySuccessful")
    @Test
    void verifyShouldReturnHttpStatusOkAndVerifyResponseIfVerifySuccessful() {
        VerifyResponse verifyResponse = new VerifyResponse();
        verifyResponse.setSuccess(true);
        when(mockAuthenticationService.verify()).thenReturn(verifyResponse);

        OoushResponseEntity response = target.verify();
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap) response.getBody()).get("data"), Matchers.instanceOf(VerifyResponse.class));
    }

    @DisplayName("verifyShouldReturnHttpStatusUnauthorizedAndVerifyResponseIfVerifyUnsuccessful")
    @Test
    void verifyShouldReturnHttpStatusUnauthorizedAndVerifyResponseIfVerifyUnsuccessful() {
        VerifyResponse verifyResponse = new VerifyResponse();
        verifyResponse.setSuccess(false);
        when(mockAuthenticationService.verify()).thenReturn(verifyResponse);

        OoushResponseEntity response = target.verify();
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.UNAUTHORIZED));
        assertThat(((ImmutableMap) response.getBody()).get("data"), Matchers.instanceOf(VerifyResponse.class));
    }
}