package com.ooush.api.service.users;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ooush.api.dto.request.RegisterUserRequest;
import com.ooush.api.dto.request.UpdateUserSettingsRequest;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.UserSettingsResponse;
import com.ooush.api.entity.ExerciseDay;
import com.ooush.api.entity.LoginToken;
import com.ooush.api.entity.UserSetting;
import com.ooush.api.entity.Users;
import com.ooush.api.entity.enumerables.UserStatus;
import com.ooush.api.entity.enumerables.WeightDenomination;
import com.ooush.api.repository.ExerciseDayRepository;
import com.ooush.api.repository.UserRepository;
import com.ooush.api.repository.UserSettingRepository;
import com.ooush.api.repository.UserWorkoutDayRepository;
import com.ooush.api.service.appsettings.AppSettingsService;
import com.ooush.api.service.email.RegisterUserEmailService;

class BasicUserServiceTest {

    private final static String REGISTER_USER_SUCCESS_MESSAGE = "A confirmation link has been sent to your email address. Please confirm your email to complete your user account registration";
    private final static String USER_NAME_IN_USE_ERROR_MESSAGE = "Your registration request could not be processed. An account exists with this user name";
    private final static String EMAIL_IN_USE_ERROR_MESSAGE = "Your registration request could not be processed. An account exists with this email address";
    private final static String EMAIL_MISSING_ERROR_MESSAGE = "Your registration request could not be processed. An email address must be provided";
    private final static String FIRST_NAME_MISSING_ERROR_MESSAGE = "Your registration request could not be processed. You must provide your first name";
    private final static String LAST_NAME_MISSING_ERROR_MESSAGE = "Your registration request could not be processed. You must provide your last name";
    private final static int USER_SETTING_ID = 4;
    private final static String USER_NAME = "test_user_1@ooushfitness.com";
    private final static String PASSWORD_HASH = "$2a$08$svfVbtVTSEDcBtp1mWvYceVQLl3xH5ACBaE3bpKHdN8roN1FJMxD2";
    private final static String PASSWORD = "Liverpool1";
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

    @Mock
    UserRepository mockUserRepo;

    @Mock
    PasswordEncoder mockPasswordEncoder;

    @Mock
    AppSettingsService mockAppSettingService;

    @Mock
    MockHttpServletResponse mockHttpServletResponse;

    @Mock
    ExerciseDayRepository mockExerciseDayRepository;

    @Mock
    UserWorkoutDayRepository mockUserWorkoutDayRepository;

    @Mock
    RegisterUserEmailService mockRegisterUserEmailService;

    @InjectMocks
    private BasicUserService target = new BasicUserService();

    Users testUser;
    RegisterUserRequest registerUserRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = createTestUser();
        when(mockLoggedInUserService.getCurrentLoggedInUser()).thenReturn(testUser);
    }

    @Nested
    @DisplayName("Existing User Queries")
    class ExistingUserQueries {
        UserSetting userSettings;

        @BeforeEach
        void setUp() {
            userSettings = new UserSetting(WeightDenomination.KG);
            userSettings.setUser(testUser);
            userSettings.setId(USER_SETTING_ID);
            when(mockUserSettingsRepo.findByUser(testUser)).thenReturn(userSettings);
        }

        @Test
        void shouldFindCorrectUserById() {
            Optional<Users> optionalUserToFind = Optional.of(createTestUser());
            when(mockUserRepo.findById(USER_ID)).thenReturn(optionalUserToFind);
            Users foundUser = target.findUserById(USER_ID);
            assertThat(foundUser.getUsersId(), Matchers.equalTo(testUser.getUsersId()));
        }

        @DisplayName("shouldFindCorrectUserByUserName")
        @Test
        void shouldFindCorrectUserByUserName() {
            when(mockUserRepo.findByUserName(USER_NAME)).thenReturn(createTestUser());
            Users foundUser = target.findByUserName(USER_NAME);
            assertThat(foundUser.getUsersId(), Matchers.equalTo(testUser.getUsersId()));
        }

        @DisplayName("shouldReturnCorrectUserSettings")
        @Test
        void shouldReturnCorrectUserSettings() {
            UserSettingsResponse userSettingsResponse = target.getUserSettings();
            assertThat(userSettingsResponse.getWeightDenomination(), Matchers.equalTo(userSettings.getWeightDenomination().getLabel()));
        }

        @DisplayName("shouldUpdateUserSettings")
        @Test
        void shouldUpdateUserSettings() {
            UpdateUserSettingsRequest updateUserSettingsRequest = new UpdateUserSettingsRequest();
            updateUserSettingsRequest.setWeightDenomination(WeightDenomination.LBS.getLabel());
            when(mockUserSettingsRepo.save(userSettings)).thenReturn(userSettings);

            UserSettingsResponse response = target.updateUserSettings(updateUserSettingsRequest);
            assertThat(response.getWeightDenomination(), Matchers.equalTo(updateUserSettingsRequest.getWeightDenomination()));
        }
    }

    @Nested
    @DisplayName("RegisterUser")
    class RegisterUser {

        @BeforeEach
        void setUp() {
            registerUserRequest = new RegisterUserRequest();
            registerUserRequest.setPhoneNumber(PHONE_NUMBER);
            registerUserRequest.setUserName(USER_NAME);
            registerUserRequest.setLocation(LOCATION);
            registerUserRequest.setEmail(USER_NAME);
            registerUserRequest.setUserName(USER_NAME);
            registerUserRequest.setFirstName(FIRST_NAME);
            registerUserRequest.setLastName(LAST_NAME);
            registerUserRequest.setPassword(PASSWORD);
        }

        @Test
        @DisplayName("shouldReturnBadRequestIfEmailAddressAbsent")
        void shouldReturnBadRequestIfEmailAddressAbsent() {
            registerUserRequest.setEmail(null);

            OoushResponseEntity response = target.registerUser(registerUserRequest);
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.BAD_REQUEST));
            assertThat(response.getBody(), Matchers.equalTo(EMAIL_MISSING_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("shouldReturnBadRequestIfFirstNameAddressAbsent")
        void shouldReturnBadRequestIfFirstNameAddressAbsent() {
            registerUserRequest.setFirstName(null);

            OoushResponseEntity response = target.registerUser(registerUserRequest);
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.BAD_REQUEST));
            assertThat(response.getBody(), Matchers.equalTo(FIRST_NAME_MISSING_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("shouldReturnBadRequestIfLastNameAddressAbsent")
        void shouldReturnBadRequestIfLastNameAddressAbsent() {
            registerUserRequest.setLastName(null);

            OoushResponseEntity response = target.registerUser(registerUserRequest);
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.BAD_REQUEST));
            assertThat(response.getBody(), Matchers.equalTo(LAST_NAME_MISSING_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("shouldReturnBadRequestIfUserNameInUse")
        void shouldReturnBadRequestIfUserNameInUse() {
            when(mockUserRepo.findByUserName(USER_NAME)).thenReturn(createTestUser());

            OoushResponseEntity response = target.registerUser(registerUserRequest);
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.BAD_REQUEST));
            assertThat(response.getBody(), Matchers.equalTo(USER_NAME_IN_USE_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("shouldReturnBadRequestIfEmailAddressNameInUse")
        void shouldReturnBadRequestIfEmailAddressInUse() {
            when(mockUserRepo.findByEmail(USER_NAME)).thenReturn(createTestUser());

            OoushResponseEntity response = target.registerUser(registerUserRequest);
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.BAD_REQUEST));
            assertThat(response.getBody(), Matchers.equalTo(EMAIL_IN_USE_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("shouldSuccessfullyRegisterUser")
        void shouldSuccessfullyRegisterUser() {
            when(mockPasswordEncoder.encode(anyString())).thenReturn(PASSWORD_HASH);
            when(mockUserRepo.save(any(Users.class))).thenReturn(createTestUser());

            OoushResponseEntity response = target.registerUser(registerUserRequest);
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
            assertThat(response.getBody(), Matchers.equalTo(REGISTER_USER_SUCCESS_MESSAGE));
        }
    }

    @Nested
    @DisplayName("Verification Email Tests")
    class VerificationEmailTests {

        private final static String PRE_VERIFIED_ACCOUNT_NOT_FOUND_ERROR_MESSAGE = "There is no pre-verified user account under this email address";
        private final static String VERIFICATION_CODE_ALREADY_SENT_ERROR_MESSAGE = "A new confirmation link has already been send to your email address."
                + " Please either check your email inbox for the newest email, register a new account"
                + ", or alternatively use the form to re-send your verification email, using your email address";
        private final static String VERIFICATION_EMAIL_RESEND_SUCCESS_MESSAGE = "A new confirmation link has been sent to your email address. Your old verification email will no longer be valid";
        @Test
        @DisplayName("shouldReturnErrorIfNoPreVerifiedAccountFoundWhenSearchingByEmail")
        void shouldReturnErrorIfNoPreVerifiedAccountFoundWhenSearchingByEmail() {
            when(mockUserRepo.findPreverifiedByEmail(USER_NAME)).thenReturn(null);

            OoushResponseEntity response = target.resendVerificationEmail(USER_NAME);
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.BAD_REQUEST));
            assertThat(response.getBody(), Matchers.equalTo(PRE_VERIFIED_ACCOUNT_NOT_FOUND_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("shouldReturnErrorIfNoPreVerifiedAccountFoundWhenSearchingByVerificationCode")
        void shouldReturnErrorIfNoPreVerifiedAccountFoundWhenSearchingByVerificationCode() {
            when(mockUserRepo.findByVerificationCode(VERIFICATION_CODE)).thenReturn(null);

            OoushResponseEntity response = target.resendVerificationEmail(VERIFICATION_CODE);
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.BAD_REQUEST));
            assertThat(response.getBody(), Matchers.equalTo(VERIFICATION_CODE_ALREADY_SENT_ERROR_MESSAGE));
        }

        @Test
        @DisplayName("shouldReSendVerificationEmailWhenSearchingByEmail")
        void shouldReSendVerificationEmailWhenSearchingByEmail() {
            when(mockUserRepo.findPreverifiedByEmail(USER_NAME)).thenReturn(createTestUser());

            OoushResponseEntity response = target.resendVerificationEmail(USER_NAME);
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
            assertThat(response.getBody(), Matchers.equalTo(VERIFICATION_EMAIL_RESEND_SUCCESS_MESSAGE));
        }

        @Test
        @DisplayName("shouldReSendVerificationEmailWhenSearchingByVerificationCode")
        void shouldReSendVerificationEmailWhenSearchingByVerificationCode() {
            when(mockUserRepo.findByVerificationCode(VERIFICATION_CODE)).thenReturn(createTestUser());

            OoushResponseEntity response = target.resendVerificationEmail(VERIFICATION_CODE);
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
            assertThat(response.getBody(), Matchers.equalTo(VERIFICATION_EMAIL_RESEND_SUCCESS_MESSAGE));
        }
    }

    @Nested
    @DisplayName("Verify User Tests")
    class VerifyUserTests {
        private final DateTime VALID_CODE_GENERATION_TIME = new DateTime(Instant.parse("2123-03-01T11:30:19.00Z").toEpochMilli());

        @BeforeEach
        void setUp() {
            testUser.setActive(false);
            testUser.setEmailConfirmed(false);
            testUser.setUserStatus(UserStatus.PRE_VERIFIED);
            testUser.setIdentityVerificationTime(null);
        }

        @Test
        @DisplayName("shouldReturnBadRequestIfVerificationCodeExpired")
        void shouldReturnBadRequestIfVerificationCodeExpired() throws IOException {
            when(mockUserRepo.findByVerificationCode(VERIFICATION_CODE)).thenReturn(testUser);

            OoushResponseEntity response = target.verifyUser(VERIFICATION_CODE, mockHttpServletResponse);
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.BAD_REQUEST));
        }

        @Test
        @DisplayName("shouldVerifyPreVerifiedUserWhenVerificationCodeWithinExpiryThreshold")
        void shouldVerifyPreVerifiedUserWhenVerificationCodeWithinExpiryThreshold() throws IOException {
            testUser.setCodeGenerationTime(VALID_CODE_GENERATION_TIME);
            when(mockUserRepo.findByVerificationCode(VERIFICATION_CODE)).thenReturn(testUser);
            when(mockExerciseDayRepository.findAll()).thenReturn(createUserWorkoutWeek());
            when(mockUserRepo.save(any(Users.class))).thenReturn(testUser);

            OoushResponseEntity response = target.verifyUser(VERIFICATION_CODE, mockHttpServletResponse);
            Users responseUser = (Users) response.getBody();
            assertTrue(responseUser.isActive());
            assertTrue(responseUser.isEmailConfirmed());
            assertThat(responseUser.getUserStatus(), Matchers.equalTo(UserStatus.VERIFIED));
            assertNull(responseUser.getCodeGenerationTime());
            assertNotNull(responseUser.getIdentityVerificationTime());
            assertThat(response.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
        }
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

    private List<ExerciseDay> createUserWorkoutWeek() {
        List<ExerciseDay> userWorkoutWeek = new ArrayList<>();
        userWorkoutWeek.add(new ExerciseDay(1, true, "Monday", 0));
        userWorkoutWeek.add(new ExerciseDay(2, true, "Tuesday", 1));
        userWorkoutWeek.add(new ExerciseDay(3, true, "Wednesday", 2));
        userWorkoutWeek.add(new ExerciseDay(4, true, "Thursday", 3));
        userWorkoutWeek.add(new ExerciseDay(5, true, "Friday", 4));
        userWorkoutWeek.add(new ExerciseDay(6, true, "Saturday", 5));
        userWorkoutWeek.add(new ExerciseDay(7, true, "Sunday", 6));

        return userWorkoutWeek;
    }
}