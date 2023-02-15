package com.ooush.api.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;

import com.ooush.api.dto.request.LoginRequest;
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

    @DisplayName("authenticateLoginShouldReturnHttpStatus")
    @Test
    void authenticateLoginShouldReturnHttpStatus() {
        target.authenticateLogin(new LoginRequest());
    }

    @DisplayName("logoutShouldReturnHttpStatus")
    @Test
    void logoutShouldReturnHttpStatus() {
        target.logout(mockHttpServletRequest);
    }

    @DisplayName("verifyShouldReturnHttpStatus")
    @Test
    void verifyShouldReturnHttpStatus() {
        target.verify();
    }
}