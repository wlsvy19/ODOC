package com.fastcampus.jpa.bookmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
	// localhost:8090/hello-world
	@GetMapping("hello-world")
	public String helloWorld() {
		return "hello world!!!";
	}
}
