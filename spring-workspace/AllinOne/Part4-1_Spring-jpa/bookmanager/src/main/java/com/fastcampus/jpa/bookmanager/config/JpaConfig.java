package com.fastcampus.jpa.bookmanager.config;

import org.springframework.context.annotation.Bean;

import java.util.Properties;

public class JpaConfig {

	@Bean
	public Properties jpaProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.use_sql_comments", "true");
		properties.setProperty("eclipselink.logging.level.sql", "FINE");
		return properties;
	}
}
