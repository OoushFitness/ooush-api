package com.ooush.api.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.common.collect.ImmutableMap;
import com.ooush.api.dto.request.RegisterUserRequest;
import com.ooush.api.dto.request.UpdateUserSettingsRequest;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.OoushResponseMap;
import com.ooush.api.dto.response.UserSettingsResponse;
import com.ooush.api.entity.UserSetting;
import com.ooush.api.entity.enumerables.WeightDenomination;
import com.ooush.api.repository.UserRespository;
import com.ooush.api.repository.UserSettingRepository;
import com.ooush.api.service.email.RegisterUserEmailService;
import com.ooush.api.service.users.BasicUserService;

class UserControllerTest {

    private static final String REGISTER_USER_SUCCESS_MESSAGE
            = "A confirmation link has been sent to your email address. Please confirm your email to complete your user account registration";
    private static final String VERIFICATION_CODE = "660163a5-e141-4e86-9410-e27b2d0b41fe";

    @Mock
    BasicUserService mockBasicUserService;

    @Mock
    UserSettingRepository mockUserSettingRepository;

    @Mock
    RegisterUserEmailService mockRegisterUserEmailService;

    @Mock
    MockHttpServletResponse mockHttpServletResponse;

    @Mock
    UserRespository mockUserRepository;

    @InjectMocks
    private UserController target = new UserController();

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        this.mockHttpServletResponse = new MockHttpServletResponse();
    }

    @DisplayName("registerUserShouldReturnSuccess")
    @Test
    void registerUserShouldReturnSuccess() {
        when(mockBasicUserService.registerUser(any(RegisterUserRequest.class))).thenReturn(new OoushResponseEntity(REGISTER_USER_SUCCESS_MESSAGE, HttpStatus.OK));

        OoushResponseEntity response = target.registerUser(new RegisterUserRequest());
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap) response.getBody()).get("data"), Matchers.instanceOf(OoushResponseEntity.class));
        assertThat(((OoushResponseEntity) ((ImmutableMap) response.getBody()).get("data")).getBody(), Matchers.equalTo(REGISTER_USER_SUCCESS_MESSAGE));
    }

    @DisplayName("getUserSettingsShouldReturnUserSettings")
    @Test
    void getUserSettingsShouldReturnUserSettings() {
        when(mockBasicUserService.getUserSettings()).thenReturn(new UserSettingsResponse(new UserSetting(
                WeightDenomination.KG)));

        OoushResponseEntity response = target.getUserSettings();
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap) response.getBody()).get("data"), Matchers.instanceOf(UserSettingsResponse.class));
    }

    @DisplayName("verifyUserShouldReturnHttpStatus")
    @Test
    void verifyUserShouldReturnHttpStatus() throws IOException {
        when(mockBasicUserService.verifyUser(VERIFICATION_CODE, mockHttpServletResponse)).thenReturn(new OoushResponseEntity(HttpStatus.OK));

        OoushResponseEntity response = target.verifyUser(VERIFICATION_CODE, mockHttpServletResponse);
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
    }

    @DisplayName("updateUserSettingsShouldReturnUserSettings")
    @Test
    void updateUserSettingsShouldReturnUserSettings() {
        when(mockBasicUserService.updateUserSettings(any(UpdateUserSettingsRequest.class))).thenReturn(new UserSettingsResponse(new UserSetting(WeightDenomination.KG)));

        OoushResponseEntity response = target.updateUserSettings(new UpdateUserSettingsRequest());
        assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        assertThat(((ImmutableMap) response.getBody()).get("data"), Matchers.instanceOf(UserSettingsResponse.class));
    }
}