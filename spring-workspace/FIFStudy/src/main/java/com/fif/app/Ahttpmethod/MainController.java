package com.fif.app.Ahttpmethod;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

	@RequestMapping(value = "dataHandling")
	public String home() {
		return "Ahttpmethod/main";
	}

	@RequestMapping(value = "reqParam", method = RequestMethod.POST)
	public void reqParam(@RequestParam String id, @RequestParam String name, @RequestParam String age) {
		System.out.println("reqParam()");
		System.out.println("inputId: " + id);
		System.out.println("inputName: " + name);
		System.out.println("inputAge: " + age);
	}

	@RequestMapping(value = "httpSvReq", method = RequestMethod.POST)
	public void httpSvReq(HttpServletRequest req) {
		System.out.println("httpSvReq()");
		String id = req.getParameter("userId");
		String name = req.getParameter("userName");
		System.out.println("id: " + id);
		System.out.println("name: " + name);
	}

	@RequestMapping(value = "dtoObject", method = RequestMethod.POST)
	public void dtoObject(@RequestParam String person) {
		System.out.println(person);
		
//		
//		System.out.println("Person ID: " + person.getId());
//		System.out.println("Person name: " + person.getName());
//		System.out.println("Person age: " + person.getAge());
	}

}
