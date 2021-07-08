package com.fif.app.finalexam;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FinalExamController {
    @Inject
    private UserService service;
    
    @RequestMapping(value = "/final", method = RequestMethod.GET)
    public String login() {
        System.out.println("login()");
        return "finalexam/login";
    } // end login

    @RequestMapping(value = "loginAction", method = RequestMethod.POST)
    public void loginAction(@RequestBody UserDTO dto, HttpServletRequest req) throws Exception {
        System.out.println("loginAction()");
        System.out.println(dto.getId() + ", " + dto.getPw());
        
        HttpSession session = req.getSession();
        String id = dto.getId();
        session.setAttribute("sessionId", id);
        
        
        List<UserDTO> userList = service.selectUser();
        System.out.println("userList...");
        for(UserDTO user : userList) {
            System.out.println("ID: " + user.getId());
            System.out.println("PW: " + user.getPw());
            
        }
        // stevelee, 1234zz
        
        
    }
}
