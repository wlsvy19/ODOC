package com.fastcampus.jpa.bookmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest // mock mvc 생성
class HelloWorldControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void helloWorld() throws Exception {
		// get 요청 테스트
		mockMvc.perform(MockMvcRequestBuilders.get("/hello-world"))
				.andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("hello world!!!"));

	}
}