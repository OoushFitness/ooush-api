package com.ooush.api.service.authentication;

import com.ooush.api.dto.request.LoginRequest;
import com.ooush.api.dto.response.LoginResponse;
import com.ooush.api.dto.response.LogoutResponse;
import com.ooush.api.dto.response.VerifyResponse;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {

	LoginResponse authenticateLogin(LoginRequest loginRequest);

	LogoutResponse logout(HttpServletRequest request);

	VerifyResponse verify();

}
