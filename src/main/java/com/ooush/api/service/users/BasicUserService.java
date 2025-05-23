package com.ooush.api.service.users;

import com.ooush.api.dto.request.RegisterUserRequest;
import com.ooush.api.dto.request.UpdateUserSettingsRequest;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.UserSettingsResponse;
import com.ooush.api.entity.ExerciseDay;
import com.ooush.api.entity.UserSetting;
import com.ooush.api.entity.UserWorkoutDay;
import com.ooush.api.entity.Users;
import com.ooush.api.entity.enumerables.UserStatus;
import com.ooush.api.entity.enumerables.WeightDenomination;
import com.ooush.api.repository.ExerciseDayRepository;
import com.ooush.api.repository.UserRepository;
import com.ooush.api.repository.UserSettingRepository;
import com.ooush.api.repository.UserWorkoutDayRepository;
import com.ooush.api.service.appsettings.AppSettingsService;
import com.ooush.api.service.email.RegisterUserEmailService;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.ooush.api.constants.OoushConstants.VERIFICATION_CODE_EXPIRY_HOURS;
import static com.ooush.api.entity.enumerables.UserStatus.PRE_VERIFIED;

@Service("BasicUserService")
@Transactional
public class BasicUserService extends AbstractUserService implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicUserService.class);

	@Autowired
	private RegisterUserEmailService registerUserEmailService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AppSettingsService appSettingsService;

	@Autowired
	private ExerciseDayRepository exerciseDayRepository;

	@Autowired
	private UserWorkoutDayRepository userWorkoutDayRepository;

	@Autowired
	private UserSettingRepository userSettingRepository;

	@Autowired
	private LoggedInUserService loggedInUserService;

	@Override
	public Users findUserById(Integer id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public OoushResponseEntity verifyUser(String verificationCode, HttpServletResponse response) throws IOException {

		Users userToVerify = userRepository.findByVerificationCode(verificationCode);
		String redirectUrl = appSettingsService.constructWebBaseUrl() + "/login";

		if (new DateTime().isAfter(userToVerify.getCodeGenerationTime().plusHours(VERIFICATION_CODE_EXPIRY_HOURS))) {
			response.sendRedirect(redirectUrl);
		} else {
			userToVerify.setActive(true);
			userToVerify.setEmailConfirmed(true);
			userToVerify.setUserStatus(UserStatus.VERIFIED);
			userToVerify.setCodeGenerationTime(null);
			userToVerify.setIdentityVerificationTime(DateTime.now());

			Users savedUser = userRepository.save(userToVerify);
			addUserWorkoutWeek(savedUser);

			response.sendRedirect(redirectUrl + "?" + verificationCode);
			return new OoushResponseEntity(savedUser, HttpStatus.OK);
		}
		return new OoushResponseEntity(HttpStatus.BAD_REQUEST);
	}

	@Override
	public OoushResponseEntity registerUser(RegisterUserRequest registerUserRequest) {
		Users newUser = new Users();

		OoushResponseEntity errorInRequest = populateUserDetailsOnRegistrationRequest(registerUserRequest, newUser);
		if (errorInRequest != null) {
			return errorInRequest;
		}

		LOGGER.info("Service registerUser() from BasicUserService called");
		LOGGER.debug("Service registerUser() from BasicUserService called for new user {}", newUser.getEmail());

		Users savedUser = userRepository.save(newUser);

		UserSetting userSetting = new UserSetting();
		userSetting.setWeightDenomination(WeightDenomination.KG);
		userSetting.setUser(savedUser);
		userSettingRepository.save(userSetting);

		registerUserEmailService.sendRegistrationEmail(savedUser);

		return new OoushResponseEntity("A confirmation link has been sent to your email address. Please confirm your email to complete your user account registration", HttpStatus.OK);
	}

	@Override
	public OoushResponseEntity resendVerificationEmail(String verificationString) {

		Users userToVerify;
		boolean resendUsingEmail = verificationString.contains("@");
		if (resendUsingEmail) {
			userToVerify = userRepository.findPreverifiedByEmail(verificationString);
		} else {
			userToVerify = userRepository.findByVerificationCode(verificationString);
		}

		if (userToVerify != null) {
			String newVerificationCode = UUID.randomUUID().toString();

			if (newVerificationCode != null) {
				userToVerify.setCodeGenerationTime(new DateTime());
				userToVerify.setVerificationCode(newVerificationCode);
				userRepository.save(userToVerify);
			}
			registerUserEmailService.sendRegistrationEmail(userToVerify);
			return new OoushResponseEntity("A new confirmation link has been sent to your email address. Your old verification email will no longer be valid", HttpStatus.OK);

		} else {
			if (resendUsingEmail) {
				return new OoushResponseEntity("There is no pre-verified user account under this email address", HttpStatus.BAD_REQUEST);
			} else {
				return new OoushResponseEntity("A new confirmation link has already been send to your email address."
						+ " Please either check your email inbox for the newest email, register a new account"
						+ ", or alternatively use the form to re-send your verification email, using your email address", HttpStatus.BAD_REQUEST);
			}
		}
	}

	private OoushResponseEntity populateUserDetailsOnRegistrationRequest(RegisterUserRequest registerUserRequest, Users newUser) {

		String emailAddress = registerUserRequest.getEmail();
		String phoneNumber =  registerUserRequest.getPhoneNumber();
		String userName = registerUserRequest.getUserName();
		String firstName = registerUserRequest.getFirstName();
		String lastName = registerUserRequest.getLastName();
		String location = registerUserRequest.getLocation();

		if (emailAddress == null) {
			return new OoushResponseEntity("Your registration request could not be processed. An email address must be provided", HttpStatus.BAD_REQUEST);
		}

		if (firstName == null) {
			return new OoushResponseEntity("Your registration request could not be processed. You must provide your first name", HttpStatus.BAD_REQUEST);
		}

		if (lastName == null) {
			return new OoushResponseEntity("Your registration request could not be processed. You must provide your last name", HttpStatus.BAD_REQUEST);
		}

		Users existingUserByUserName = userRepository.findByUserName(userName);
		Users existingUserByEmail = userRepository.findByEmail(emailAddress);

		if (existingUserByUserName != null) {
			return new OoushResponseEntity("Your registration request could not be processed. An account exists with this user name", HttpStatus.BAD_REQUEST);
		}

		if (existingUserByEmail != null) {
			return new OoushResponseEntity("Your registration request could not be processed. An account exists with this email address", HttpStatus.BAD_REQUEST);
		}

		newUser.setEmail(emailAddress);
		newUser.setEmailConfirmed(false);
		newUser.setActive(false);
		newUser.setPasswordHash(passwordEncoder.encode(registerUserRequest.getPassword()));
		newUser.setPhoneNumber(phoneNumber);
		newUser.setPhoneNumberConfirmed(false);
		newUser.setUserName(userName);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setUserStatus(PRE_VERIFIED);
		newUser.setCodeGenerationTime(new DateTime());
		newUser.setLocation(location);
		newUser.setTestUser(false);

		String verificationCode = UUID.randomUUID().toString();
		String identityVerificationCode = UUID.randomUUID().toString();

		if (verificationCode != null) {
			newUser.setVerificationCode(verificationCode);
		}

		if (identityVerificationCode != null) {
			newUser.setIdentityVerificationCode(identityVerificationCode);
		}

		return null;
	}

	private void addUserWorkoutWeek(Users userToVerify) {
		List<ExerciseDay> exerciseWeek = exerciseDayRepository.findAll();

		for (ExerciseDay exerciseDay : exerciseWeek) {
			UserWorkoutDay userWorkoutDay = new UserWorkoutDay();
			userWorkoutDay.setUser(userToVerify);
			userWorkoutDay.setExerciseDay(exerciseDay);

			userWorkoutDayRepository.save(userWorkoutDay);
		}
	}

	@Override
	public Users findByUserName(String username) {
		return userRepository.findByUserName(username);
	}

	@Override
	public UserSettingsResponse updateUserSettings(UpdateUserSettingsRequest updateUserSettingsRequest) {
		Users currentLoggedInUser = loggedInUserService.getCurrentLoggedInUser();
		UserSetting userSettings = userSettingRepository.findByUser(currentLoggedInUser);
		if (StringUtils.isNotEmpty(updateUserSettingsRequest.getWeightDenomination()) && StringUtils.isNotBlank(updateUserSettingsRequest.getWeightDenomination())) {
			userSettings.setWeightDenomination(WeightDenomination.valueOf(updateUserSettingsRequest.getWeightDenomination().toUpperCase()));
		}
		return new UserSettingsResponse(userSettingRepository.save(userSettings));
	}

	@Override
	public UserSettingsResponse getUserSettings() {
		Users currentLoggedInUser = loggedInUserService.getCurrentLoggedInUser();
		UserSetting userSettings = userSettingRepository.findByUser(currentLoggedInUser);
		return new UserSettingsResponse(userSettings);
	}
}