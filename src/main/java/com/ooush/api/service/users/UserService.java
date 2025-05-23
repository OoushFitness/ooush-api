package com.ooush.api.service.users;

import com.ooush.api.dto.request.RegisterUserRequest;
import com.ooush.api.dto.request.UpdateUserSettingsRequest;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.UserSettingsResponse;
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

	OoushResponseEntity registerUser(RegisterUserRequest registerUserRequest);

	OoushResponseEntity verifyUser(String verificationCode, HttpServletResponse response) throws IOException;

	OoushResponseEntity resendVerificationEmail(String verificationCode);

	Users findByUserName(String username);

	UserSettingsResponse updateUserSettings(UpdateUserSettingsRequest updateUserSettingsRequest);

	UserSettingsResponse getUserSettings();
}
