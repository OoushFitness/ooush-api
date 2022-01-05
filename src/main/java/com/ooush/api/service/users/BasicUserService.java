package com.ooush.api.service.users;

import com.ooush.api.dto.request.RegisterUserRequest;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.entity.Users;
import com.ooush.api.entity.enumerables.UserStatus;
import com.ooush.api.repository.UserRespository;
import com.ooush.api.service.appsettings.AppSettingsService;
import com.ooush.api.service.email.RegisterUserEmailService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static com.ooush.api.constants.OoushConstants.VERIFICATION_CODE_EXPIRY_HOURS;
import static com.ooush.api.entity.enumerables.UserStatus.PRE_VERIFIED;

@Service("BasicUserService")
@Transactional
public class BasicUserService implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasicUserService.class);

	@Autowired
	private RegisterUserEmailService registerUserEmailService;

	@Autowired
	private UserRespository userRespository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AppSettingsService appSettingsService;

	@Override
	public Users findUserById(Integer id) {
		return userRespository.findById(id).orElse(null);
	}

	@Override
	public void verifyUser(String verificationCode, HttpServletResponse response) throws IOException {

		Users userToVerify = userRespository.findByVerificationCode(verificationCode);
		String redirectUrl = appSettingsService.constructWebBaseUrl() + "/login";

		if (new DateTime().isAfter(userToVerify.getCodeGenerationTime().plusHours(VERIFICATION_CODE_EXPIRY_HOURS))) {
			response.sendRedirect(redirectUrl);
		}

		userToVerify.setActive(true);
		userToVerify.setEmailConfirmed(true);
		userToVerify.setUserStatus(UserStatus.VERIFIED);
		userToVerify.setCodeGenerationTime(null);

		Users savedUser = userRespository.save(userToVerify);

		response.sendRedirect(redirectUrl + "?" + verificationCode);
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

		Users savedUser = userRespository.save(newUser);

		registerUserEmailService.sendRegistrationEmail(savedUser);

		return new OoushResponseEntity("A confirmation link has been sent to your email address. Please confirm your email to complete your user account registration", HttpStatus.OK);
	}

	@Override
	public OoushResponseEntity resendVerificationEmail(String verificationString) {

		Users userToVerify;
		boolean resendUsingEmail = verificationString.contains("@");
		if (resendUsingEmail) {
			userToVerify = userRespository.findPreverifiedByEmail(verificationString);
		} else {
			userToVerify = userRespository.findByVerificationCode(verificationString);
		}

		if (userToVerify != null) {
			String newVerificationCode = UUID.randomUUID().toString();

			if (newVerificationCode != null) {
				userToVerify.setCodeGenerationTime(new DateTime());
				userToVerify.setVerificationCode(newVerificationCode);
				userRespository.save(userToVerify);
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

		Users existingUserByUserName = userRespository.findByUserName(userName);
		Users existingUserByEmail = userRespository.findByEmail(emailAddress);

		if (existingUserByUserName != null) {
			return new OoushResponseEntity("Your registration request could not be processed. An account exists with this user name", HttpStatus.BAD_REQUEST);
		}

		if (existingUserByEmail != null) {
			return new OoushResponseEntity("Your registration request could not be processed. An account exists with this email address", HttpStatus.BAD_REQUEST);
		}

		newUser.setEmail(emailAddress);
		newUser.setEmailConfirmed(false);
		newUser.setActive(false);
		newUser.setPasswordHash(passwordEncoder.encode("testPassword123"));
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

}