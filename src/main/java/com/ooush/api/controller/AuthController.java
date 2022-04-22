package com.ooush.api.controller;

import com.ooush.api.dto.request.LoginRequest;
import com.ooush.api.dto.response.LoginResponse;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.OoushResponseMap;
import com.ooush.api.dto.response.VerifyResponse;
import com.ooush.api.service.authentication.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/auth" })
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthenticationService authenticationService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public OoushResponseEntity authenticateLogin(@RequestBody LoginRequest loginRequest) {
		LOGGER.info("Resource /auth/login/ POST called");
		LOGGER.debug("Resource /auth/login/ POST called for userName: {}", loginRequest.getUserName());
		LoginResponse loginResponse = authenticationService.authenticateLogin(loginRequest);
		HttpStatus responseStatus = loginResponse.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(loginResponse).construct(), responseStatus);
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public OoushResponseEntity logout() {
		LOGGER.info("Resource /auth/logout/ POST called");
		LOGGER.debug("Resource /auth/logout/ POST called");
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(authenticationService.logout()).construct());
	}

	@RequestMapping(value = "/verify", method = RequestMethod.GET)
	public OoushResponseEntity verify() {
		LOGGER.info("Resource /auth/verify/ POST called");
		LOGGER.debug("Resource /auth/verify/ POST called");
		VerifyResponse verifyResponse = authenticationService.verify();
		HttpStatus responseStatus = verifyResponse.isSuccess() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(verifyResponse).construct(), responseStatus);
	}

}
