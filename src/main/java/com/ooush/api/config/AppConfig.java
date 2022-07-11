package com.ooush.api.config;

import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
public class AppConfig implements WebMvcConfigurer {
	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.addPathPrefix("/api", (clazz) -> {
			return clazz.getCanonicalName().startsWith("com.ooush.api.controller");
		});
	}
}
