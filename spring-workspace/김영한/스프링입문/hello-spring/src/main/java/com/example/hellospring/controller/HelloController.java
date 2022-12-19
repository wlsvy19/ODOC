package com.example.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    // 스프링 웹개발 방식1: 정적 컨텐츠 -> 파일 그대로 화면에 내려줌
    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data", "hello");
        return "hello";
    }

    // 스프링 웹개발 방식2: MVC와 템플릿 엔진 -> 템플릿엔진을 Model, View, Controller로 나눔
    @GetMapping("hello-mvc")
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);

        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name) {
        return "hello" + name;
    }

    // 스프링 웹개발 방식3: API 방식: return으로 페이지 이름x, 데이터 자체를 넘김
    @GetMapping("hello-api")
    @ResponseBody // -> JSON 형태로 반환(jackson이 기본적으로 내장되어있음)
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }
    
    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}