package com.ooush.api.service.authentication;

import com.ooush.api.dto.request.LoginRequest;
import com.ooush.api.dto.response.login.LoginResponse;

public interface AuthenticationService {

	LoginResponse authenticateLogin(LoginRequest loginRequest);

}
