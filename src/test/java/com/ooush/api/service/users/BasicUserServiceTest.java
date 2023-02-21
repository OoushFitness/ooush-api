package com.ooush.api.service.users;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ooush.api.dto.response.UserSettingsResponse;
import com.ooush.api.entity.LoginToken;
import com.ooush.api.entity.UserSetting;
import com.ooush.api.entity.Users;
import com.ooush.api.entity.enumerables.UserStatus;
import com.ooush.api.entity.enumerables.WeightDenomination;
import com.ooush.api.repository.UserSettingRepository;

class BasicUserServiceTest {

    private final static int USER_SETTING_ID = 4;
    private final static String USER_NAME = "test_user_1@ooushfitness.com";
    private final static String PASSWORD_HASH = "$2a$08$svfVbtVTSEDcBtp1mWvYceVQLl3xH5ACBaE3bpKHdN8roN1FJMxD2";
    private final static String LOGIN_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0X3VzZXJfMUBvb3VzaGZpdG5lc3MuY29tIiwidHlwZSI6InVzZXIiLCJleHAiOjE2NzcyNTM5NDR9.8tBTl_INYi7Ufv6jWw9f3Gjib92Bg_e0q2Xwaiqgt-rilMLcndGhvRzwAw1yQpdiv5POQVPkBcLIpEZMBih1iQ";
    private final static List<LoginToken> LOGIN_TOKENS = new ArrayList<>();
    private final static int USER_ID = 1;
    private final static boolean ACTIVE = true;
    private final static boolean EMAIL_CONFIRMED = true;
    private final static String PHONE_NUMBER = "+447846967190";
    private final static boolean PHONE_NUMBER_CONFIRMED = false;
    private final static String FIRST_NAME = "Testy";
    private final static String LAST_NAME = "McTest";
    private final static UserStatus USER_STATUS = UserStatus.VERIFIED;
    private final static String VERIFICATION_CODE = "660163a5-e141-4e86-9410-e27b2d0b41fe";
    private final static DateTime CODE_GENERATION_TIME = new DateTime(Instant.parse("2018-03-01T11:30:19.00Z").toEpochMilli());
    private final static String PASSWORD_RESET_VERIFICATION_CODE = "a2ac4b3e-b7d3-4da4-b7bb-91a37847970f";
    private final static DateTime PASSWORD_RESET_CODE_GENERATION_TIME = new DateTime(Instant.parse("2018-03-01T13:06:06.00Z").toEpochMilli());
    private final static String LOCATION = "Leeds";
    private final static boolean IS_TEST_USER = false;
    private final static DateTime IDENTITY_VERIFICATION_TIME = new DateTime(Instant.parse("2118-01-01T02:13:37.00Z").toEpochMilli());
    private final static String IDENTITY_VERIFICATION_CODE = "6b509ec6-7ff1-43e2-a4fb-85017b4a1768";
    private final static boolean IDENTITY_CONFIRMED = true;
    private final static int LOGIN_ATTEMPTS = 0;

    @Mock
    UserSettingRepository mockUserSettingsRepo;

    @Mock
    LoggedInUserService mockLoggedInUserService;

    @InjectMocks
    private BasicUserService target = new BasicUserService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Users user = createTestUser();
        when(mockLoggedInUserService.getCurrentLoggedInUser()).thenReturn(user);
    }

    @Test
    void findUserById() {
    }

    @Test
    void findByUserName() {
    }

    @DisplayName("shouldReturnCorrectUserSettings")
    @Test
    void shouldReturnCorrectUserSettings() {
        Users user = createTestUser();
        UserSetting userSettings = new UserSetting(WeightDenomination.KG);
        userSettings.setUser(user);
        userSettings.setId(USER_SETTING_ID);
        when(mockUserSettingsRepo.findByUser(user)).thenReturn(userSettings);

        UserSettingsResponse userSettingsResponse = target.getUserSettings();
        assertThat(userSettingsResponse.getWeightDenomination(), Matchers.equalTo(userSettings.getWeightDenomination().getLabel()));
    }

    private Users createTestUser() {
        Users user = new Users();
        user.setUserName(USER_NAME);
        user.setActive(ACTIVE);
        user.setPasswordHash(PASSWORD_HASH);
        user.setUsersId(USER_ID);
        user.setTestUser(IS_TEST_USER);
        user.setIdentityVerificationTime(IDENTITY_VERIFICATION_TIME);
        user.setEmail(USER_NAME);
        user.setPasswordHash(PASSWORD_HASH);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setCodeGenerationTime(CODE_GENERATION_TIME);
        user.setEmailConfirmed(EMAIL_CONFIRMED);
        user.setLocation(LOCATION);
        user.setIdentityConfirmed(IDENTITY_CONFIRMED);
        user.setLoginAttempts(LOGIN_ATTEMPTS);
        user.setIdentityVerificationCode(IDENTITY_VERIFICATION_CODE);
        user.setUserStatus(USER_STATUS);
        user.setPasswordResetVerificationCode(PASSWORD_RESET_VERIFICATION_CODE);
        user.setPasswordResetCodeGenerationTime(PASSWORD_RESET_CODE_GENERATION_TIME);
        user.setVerificationCode(VERIFICATION_CODE);
        user.setPhoneNumber(PHONE_NUMBER);
        user.setPhoneNumberConfirmed(PHONE_NUMBER_CONFIRMED);

        LoginToken loginToken = new LoginToken();
        loginToken.setToken(LOGIN_TOKEN);
        LOGIN_TOKENS.add(loginToken);
        user.setLoginTokens(LOGIN_TOKENS);

        return user;
    }
}