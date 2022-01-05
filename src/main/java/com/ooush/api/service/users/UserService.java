package com.ooush.api.service.users;

import com.ooush.api.dto.request.RegisterUserRequest;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.entity.Users;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserService {

	Users findUserById(Integer id);
	OoushResponseEntity registerUser(RegisterUserRequest registerUserRequest);
	void verifyUser(String verificationCode, HttpServletResponse response) throws IOException;
	OoushResponseEntity resendVerificationEmail(String verificationCode);
}
