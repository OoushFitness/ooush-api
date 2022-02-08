package com.ooush.api.controller;

import com.ooush.api.dto.request.LoginRequest;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.OoushResponseMap;
import com.ooush.api.service.authentication.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(authenticationService.authenticateLogin(loginRequest)).construct());
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
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(authenticationService.verify()).construct());
	}

}
