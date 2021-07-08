package com.fif.app.Csessioncookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SessionCookieController {

    @RequestMapping(value = "session")
    public String home(HttpServletRequest req, Model model) {
        System.out.println("session home()");
        
        HttpSession session = req.getSession();
        System.out.println("Session ID: " + session.getId());
        
        String id = "세션";
        
        session.setAttribute("sessionId", id);
        
        //model.addAttribute("mySession" ,session.getAttribute("sessionId"));
        
        return "Csessioncookie/home";
    }
}
