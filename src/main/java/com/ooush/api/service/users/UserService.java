package com.ooush.api.service.users;

import com.ooush.api.dto.request.RegisterUserRequest;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserService {

	Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	Users findUserById(Integer id);

	Users findUserByUserName(String userName);

	OoushResponseEntity registerUser(RegisterUserRequest registerUserRequest);

	Users getCurrentLoggedInUser();

	void verifyUser(String verificationCode, HttpServletResponse response) throws IOException;

	OoushResponseEntity resendVerificationEmail(String verificationCode);

	static UserDetails getLoggedInUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			try {
				return (UserDetails) authentication.getPrincipal();
			}
			catch(ClassCastException e) {
				LOGGER.error("Failed to cast authentication principal to UserDetails: {}", authentication.getPrincipal());
			}
		}
		return null;
	}

	Users findByUserName(String username);
}
