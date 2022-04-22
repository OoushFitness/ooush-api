package com.ooush.api.dto.response;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public class OoushResponseMap {

	private final Builder<String, Object> entries = ImmutableMap.builder();

	public OoushResponseMap addData(final Object data) {

		int dataSize = data == null ? 0 : 1;

		if (data != null && data instanceof List<?>) {
			dataSize = ((List<?>) data).size();
		}

		return addEntry("data", data).addEntry("size", dataSize);
	}

	public OoushResponseMap addEntry(final String key, final Object value) {
		if (value != null) {
			entries.put(key, value);
		}
		return this;
	}

	public OoushResponseMap addStatusCode(final HttpStatus httpStatus) {
		entries.put("statusCode", httpStatus.toString());
		return this;
	}

	public Map<String, Object> construct() {
		return entries.build();
	}

	public static OoushResponseMap createSuccessResponseMap() {
		return new OoushResponseMap().addStatusCode(HttpStatus.OK);
	}

	public static OoushResponseMap createResponseMap(final Object data) {
		return new OoushResponseMap().addData(data);
	}
}
