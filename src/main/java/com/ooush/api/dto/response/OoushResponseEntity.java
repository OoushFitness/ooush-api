package com.ooush.api.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class OoushResponseEntity extends ResponseEntity<Object> {

	public OoushResponseEntity(Object object) {
		super(object, HttpStatus.OK);
	}

	public OoushResponseEntity(HttpStatus httpStatus) {
		super(null, httpStatus);
	}

	public OoushResponseEntity(Object object, HttpStatus httpStatus) {
		super(object, httpStatus);
	}

}
