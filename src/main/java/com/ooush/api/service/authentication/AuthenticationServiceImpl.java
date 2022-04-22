package com.ooush.api.service.authentication;

import com.ooush.api.constants.OoushConstants;
import com.ooush.api.controller.AuthController;
import com.ooush.api.dto.request.LoginRequest;
import com.ooush.api.dto.response.LoginResponse;
import com.ooush.api.dto.response.LogoutResponse;
import com.ooush.api.dto.response.VerifyResponse;
import com.ooush.api.entity.LoginToken;
import com.ooush.api.entity.Users;
import com.ooush.api.repository.LoginTokenRepository;
import com.ooush.api.repository.UserRespository;
import com.ooush.api.security.TokenUtils;
import com.ooush.api.service.users.UserService;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("AuthenticationService")
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final String LOG_MESSAGE_TEMPLATE = "Login attempt, result : [{}], login type : [{}], username : [{}]";
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserRespository userRespository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private LoginTokenRepository loginTokenRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	@Qualifier("BasicUserService")
	private UserService userService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenUtils tokenUtils;

	@Override
	public LoginResponse authenticateLogin(LoginRequest loginRequest) {

		LoginResponse loginResponse = new LoginResponse();
		Users userToAuthenticate = userRespository.findByUserName(loginRequest.getUserName());

		if (userToAuthenticate == null) {
			LOGGER.info("Login Failed: Username not found");
			loginResponse.setSuccess(OoushConstants.LOGIN_FAILURE);
			loginResponse.setLoginMessage(OoushConstants.LOGIN_MESSAGE_FAILURE_USERNAME_NOT_FOUND);
		} else {
			processAuthentication(loginResponse, userToAuthenticate, loginRequest);
		}
		return loginResponse;
	}

	@Override
	public LogoutResponse logout() {
		return null;
	}

	@Override
	public VerifyResponse verify() {
		Users currentLoggedInUser = userService.getCurrentLoggedInUser();
		LoginToken loginToken = loginTokenRepository.findLoginTokenByUsers(currentLoggedInUser);
		VerifyResponse verifyResponse = new VerifyResponse();
		if(loginToken != null) {
			LOGGER.info("Verification Successful");
			verifyResponse.setSuccess(OoushConstants.VERIFICATION_SUCCESS);
			assignLoginOrVerifyResponseDetails(verifyResponse, currentLoggedInUser);
		} else {
			LOGGER.info("Verification Unsuccessful");
			verifyResponse.setSuccess(OoushConstants.VERIFICATION_FAILURE);
		}
		return verifyResponse;
	}

	private void processAuthentication(LoginResponse loginResponse, Users userToAuthenticate, LoginRequest loginRequest) {
		if (passwordEncoder.matches(loginRequest.getPassword(), userToAuthenticate.getPasswordHash())) {
			LOGGER.info("Login Successful");

			loginResponse.setSuccess(OoushConstants.LOGIN_SUCCESS);
			loginResponse.setLoginMessage(OoushConstants.LOGIN_MESSAGE_SUCCESS);

			assignLoginOrVerifyResponseDetails(loginResponse, userToAuthenticate);

			setLoginAttemptsToZero(userToAuthenticate.getUsersId());

			final Authentication authentication;
			// Perform the authentication
			try {
				final TokenFactory tokenFactory = new UsernamePasswordTokenFactory();

				authentication = authenticationManager.authenticate(tokenFactory.getToken(loginRequest));
			}
			catch (DisabledException e) {
				LOGGER.warn("Disabled user {} login denied", loginRequest.getUserName());
				throw e;
			}
			catch (BadCredentialsException e) {
				LOGGER.info(LOG_MESSAGE_TEMPLATE, false, loginRequest.getClass().getSimpleName(), loginRequest.getUserName());
				LOGGER.debug("Error authenticating", e);
				throw new BadCredentialsException("Incorrect login credentials");
			}
			catch (Exception e) {
				LOGGER.error("Exception during doProcessAuthentication", e);
				throw e;
			}

			// Reload password after authentication so we can generate a token
			try {
				userDetailsService.loadUserByUsername(loginRequest.getUserName());
			}
			catch (UsernameNotFoundException e) {
				LOGGER.info(LOG_MESSAGE_TEMPLATE, false, loginRequest.getClass().getSimpleName(), loginRequest.getUserName());
				throw new BadCredentialsException("Incorrect login credentials");
			}

			SecurityContextHolder.getContext().setAuthentication(authentication);

			// No need to confirm identity
			final String token = tokenUtils.getUserToken(userToAuthenticate.getUserName());
			loginResponse.setToken(token);
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

	private void assignLoginOrVerifyResponseDetails(LoginResponse loginResponse, Users user) {
		loginResponse.setUserId(user.getUsersId());
		loginResponse.setEmail(user.getEmail());
		loginResponse.setUserName(user.getUserName());
		loginResponse.setFirstName(user.getFirstName());
		loginResponse.setLastName(user.getLastName());
		loginResponse.setLocation(user.getLocation());
	}

	private void saveLoginToken(String loginToken, Users user) {
		LoginToken userLoginToken = new LoginToken();
		userLoginToken.setToken(loginToken);
		userLoginToken.setUsers(user);
		userLoginToken.setExpiry(new DateTime().plusHours(OoushConstants.LOGIN_TOKEN_EXPIRY_HOURS));

		loginTokenRepository.save(userLoginToken);
	}

	private interface TokenFactory<L extends LoginRequest> {
		AbstractAuthenticationToken getToken(L loginRequest);
	}

	private static class UsernamePasswordTokenFactory implements TokenFactory<LoginRequest> {
		@Override
		public UsernamePasswordAuthenticationToken getToken(LoginRequest loginRequest) {
			return new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword());
		}
	}

}
