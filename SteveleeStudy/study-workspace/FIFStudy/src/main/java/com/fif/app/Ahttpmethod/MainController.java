package com.fif.app.Ahttpmethod;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

	@RequestMapping(value = "dataHandling")
	public String home() {
		return "Ahttpmethod/main";
	}

	@RequestMapping(value = "/reqParam")
	public void reqParam(@RequestBody String userId, @RequestParam String userName) {
		System.out.println("reqParam()");
		System.out.println("inputId: " + userId);
		System.out.println("inputName: " + userName);
		
	}

	@RequestMapping(value = "httpSvReq", method=RequestMethod.POST)
	public void httpSvReq(HttpServletRequest req) {
		System.out.println("httpSvReq()");
		String id = req.getParameter("userId");
		String name = req.getParameter("userName");
		System.out.println("id: " + id);
		System.out.println("name: " + name);
	}
	
	
}
