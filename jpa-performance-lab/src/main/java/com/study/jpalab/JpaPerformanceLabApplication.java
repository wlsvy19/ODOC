package com.study.jpalab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/*
@SpringBootApplication
public class JpaPerformanceLabApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaPerformanceLabApplication.class, args);
	}

}

*/
@SpringBootApplication
public class JpaPerformanceLabApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(JpaPerformanceLabApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(JpaPerformanceLabApplication.class, args);
	}
}