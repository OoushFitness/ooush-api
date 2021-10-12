package com.ooush.api.controller;

import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.OoushResponseMap;
import com.ooush.api.service.authentication.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = { "/auth" })
public class AuthController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthenticationService authenticationService;

	@RequestMapping(value = "/login/{userName}/{password}", method = RequestMethod.GET)
	public OoushResponseEntity authenticateLogin(@PathVariable String userName, @PathVariable String password) {
		LOGGER.info("Resource /auth/authenticateLogin/ GET called");
		LOGGER.debug("Resource /auth/authenticateLogin/{}/{} GET called", userName, password);
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(authenticationService.authenticateLogin(userName, password)).construct());
	}

}
