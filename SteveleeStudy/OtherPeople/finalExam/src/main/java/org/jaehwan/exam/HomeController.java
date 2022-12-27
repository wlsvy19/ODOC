package org.jaehwan.exam;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired
	private AllService service;
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "/loginaction", method = RequestMethod.POST)
	public String loginaction(
			@ModelAttribute Login login,
			HttpSession session,
			Model model
			) {
		
		System.out.println(login.getId() + " :: " + login.getPw());
		
		Login loginResult = service.findUser(login);
		System.out.println(loginResult);
		
		if(loginResult != null) {
			LoginHistory lh = new LoginHistory(login.getId(), login.getPw(), session.getId(), new Date());
			service.insertHistory(lh);
			
			List<LoginHistory> histories = service.findAllHistory();
			
			model.addAttribute("histories", histories);
			
			return "loginlist";
		}
		
		return "redirect:/";
	}
	
	@ResponseBody
	@RequestMapping(value = "/loginhistoryfield", method = RequestMethod.POST)
	public ResponseEntity<List<LoginHistory>> historyField(@RequestBody String data) {
		
		List<LoginHistory> result = service.findHistoryById(data);
		
		return ResponseEntity.ok(result);
	}
	
}
