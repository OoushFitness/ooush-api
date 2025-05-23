package com.ooush.api.controller;

import com.ooush.api.dto.request.RegisterUserRequest;
import com.ooush.api.dto.request.UpdateUserSettingsRequest;
import com.ooush.api.dto.response.OoushResponseEntity;
import com.ooush.api.dto.response.OoushResponseMap;
import com.ooush.api.service.users.BasicUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = { "/users" })
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private BasicUserService basicUserService;

	@RequestMapping(value = "/registerUser", method = RequestMethod.POST, produces = "application/json")
	public OoushResponseEntity registerUser(@RequestBody RegisterUserRequest registerUserRequest) {
		LOGGER.info("Resource /users/registerUser POST called");
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(basicUserService.registerUser(registerUserRequest)).construct());
	}

	@RequestMapping(value = "/verifyUser/{verificationCode}", method = RequestMethod.GET)
	public OoushResponseEntity verifyUser(@PathVariable String verificationCode, HttpServletResponse response) throws IOException {
		LOGGER.info("Resource /users/verifyUser/ GET called");
		LOGGER.debug("Resource /users/verifyUser/{} GET called", verificationCode);
		return basicUserService.verifyUser(verificationCode, response);
	}

	@RequestMapping(value = "/resendVerificationEmail/{verificationString}", method = RequestMethod.POST)
	public OoushResponseEntity resendVerificationEmail(@PathVariable String verificationString) {
		LOGGER.info("Resource /users/resendVerificationEmail/ POST called");
		LOGGER.debug("Resource /users/resendVerificationEmail/{} POST called", verificationString);
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(basicUserService.resendVerificationEmail(verificationString)).construct());
	}

	@RequestMapping(value = "/update-user-settings", method = RequestMethod.POST)
	public OoushResponseEntity updateUserSettings(@RequestBody UpdateUserSettingsRequest updateUserSettingsRequest) {
		LOGGER.info("Resource /users/update-user-settings POST called");
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(basicUserService.updateUserSettings(updateUserSettingsRequest)).construct());
	}

	@RequestMapping(value = "/get-user-settings", method = RequestMethod.GET)
	public OoushResponseEntity getUserSettings() {
		LOGGER.info("Resource /users/get-user-settings GET called");
		return new OoushResponseEntity(OoushResponseMap.createResponseMap(basicUserService.getUserSettings()).construct());
	}

}
