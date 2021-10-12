package com.ooush.api.service.authentication;

import com.ooush.api.constants.OoushConstants;
import com.ooush.api.controller.AuthController;
import com.ooush.api.dto.response.login.LoginResponse;
import com.ooush.api.entity.LoginToken;
import com.ooush.api.entity.Users;
import com.ooush.api.repository.LoginTokenRepository;
import com.ooush.api.repository.UserRespository;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("AuthenticationService")
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserRespository userRespository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LoginTokenRepository loginTokenRepository;

	@Override
	public LoginResponse authenticateLogin(String userName, String password) {

		LoginResponse loginResponse = new LoginResponse();
		Users userToAuthenticate = userRespository.findByUserName(userName);

		if (userToAuthenticate == null) {
			LOGGER.info("Login Failed: Username not found");
			loginResponse.setSuccess(OoushConstants.LOGIN_FAILURE);
			loginResponse.setLoginMessage(OoushConstants.LOGIN_MESSAGE_FAILURE_USERNAME_NOT_FOUND);
		} else {
			processAuthentication(loginResponse, userToAuthenticate, password);
		}

		return loginResponse;
	}

	private void processAuthentication(LoginResponse loginResponse, Users userToAuthenticate, String password) {
		if (passwordEncoder.matches(password, userToAuthenticate.getPasswordHash())) {
			LOGGER.info("Login Successful");
			assignLoginResponseDetails(loginResponse, userToAuthenticate);
			setLoginAttemptsToZero(userToAuthenticate.getUsersId());
		} else {
			LOGGER.info("Login Failed: Password incorrect");
			loginResponse.setSuccess(OoushConstants.LOGIN_FAILURE);
			loginResponse.setLoginMessage(OoushConstants.LOGIN_MESSAGE_FAILURE_PASSWORD_INCORRECT);

			incrementLoginAttempts(userToAuthenticate);
		}
	}

	private void setLoginAttemptsToZero(Integer userId) {
		Users userToAdjust = userRespository.findById(userId).orElse(null);
		if (userToAdjust != null) {
			userToAdjust.setLoginAttempts(0);
			userRespository.save(userToAdjust);
		}
	}

	private void incrementLoginAttempts(Users user) {
		user.setLoginAttempts(user.getLoginAttempts() + 1);
		userRespository.save(user);
	}

	private void assignLoginResponseDetails(LoginResponse loginResponse, Users user) {
		loginResponse.setSuccess(OoushConstants.LOGIN_SUCCESS);
		loginResponse.setLoginMessage(OoushConstants.LOGIN_MESSAGE_SUCCESS);
		loginResponse.setUserId(user.getUsersId());
		loginResponse.setEmail(user.getEmail());
		loginResponse.setUserName(user.getUserName());
		loginResponse.setFirstName(user.getFirstName());
		loginResponse.setLastName(user.getLastName());
		loginResponse.setLocation(user.getLocation());

		String loginToken = UUID.randomUUID().toString();
		loginResponse.setToken(loginToken);

		saveLoginToken(loginToken, user);
	}

	private void saveLoginToken(String loginToken, Users user) {
		LoginToken userLoginToken = new LoginToken();
		userLoginToken.setToken(loginToken);
		userLoginToken.setUsersId(user.getUsersId());
		userLoginToken.setExpiry(new DateTime().plusHours(OoushConstants.LOGIN_TOKEN_EXPIRY_HOURS));

		loginTokenRepository.save(userLoginToken);
	}

}
