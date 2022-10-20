package com.ooush.api.constants;

public class OoushConstants {

	// Authentication constants
	public static final Integer VERIFICATION_CODE_EXPIRY_HOURS = 24;
	public static final boolean VERIFICATION_SUCCESS = true;
	public static final boolean VERIFICATION_FAILURE = false;

	// Login constants
	public static final String LOGIN_MESSAGE_SUCCESS = "Login Successful";
	public static final String LOGIN_MESSAGE_FAILURE_USERNAME_NOT_FOUND = "Login Failure: Username not found";
	public static final String LOGIN_MESSAGE_FAILURE_PASSWORD_INCORRECT = "Login Failure: Password incorrect";
	public static final Integer LOGIN_TOKEN_EXPIRY_HOURS = 24;
	public static final boolean LOGIN_SUCCESS = true;
	public static final boolean LOGIN_FAILURE = false;

	// Bitmap positions
	public static final Integer BITMAP_POSITION_BODY_AREA_UPPER = 1;
	public static final Integer BITMAP_POSITION_BODY_AREA_LOWER = 2;
	public static final Integer BITMAP_POSITION_PUSH_PULL_PUSH = 3;
	public static final Integer BITMAP_POSITION_PUSH_PULL_PULL = 4;
	public static final Integer BITMAP_POSITION_BODY_PART_ARMS = 5;
	public static final Integer BITMAP_POSITION_BODY_PART_CHEST = 6;
	public static final Integer BITMAP_POSITION_BODY_PART_SHOULDERS = 7;
	public static final Integer BITMAP_POSITION_BODY_PART_BACK = 8;
	public static final Integer BITMAP_POSITION_BODY_PART_LEGS = 9;
	public static final Integer BITMAP_POSITION_CUSTOM_EXERCISE = 10;


	// Exercise constants
	public static final Long CUSTOM_EXERCISE_BITMAP_INTEGER = Long.valueOf(512);

}
