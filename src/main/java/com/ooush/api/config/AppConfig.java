package com.ooush.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;

public class AppConfig implements WebMvcConfigurer {

	@Autowired
	private DataSource dataSource;

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.addPathPrefix("/api", (clazz) -> {
			return clazz.getCanonicalName().startsWith("com.ooush.api.controller");
		});
	}
}
