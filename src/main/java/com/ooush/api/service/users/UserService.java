package com.ooush.api.service.users;

import com.ooush.api.dto.request.RegisterUserRequest;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface UserService {

	Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	Users findUserById(Integer id);
	OoushResponseEntity registerUser(RegisterUserRequest registerUserRequest);
	OoushResponseEntity verifyUser(String verificationCode);
	OoushResponseEntity resendVerificationEmail(String verificationCode);

}
