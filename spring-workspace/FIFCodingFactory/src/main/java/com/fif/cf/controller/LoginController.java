package com.fif.cf.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fif.cf.dto.LoginDTO;
import com.fif.cf.service.LoginService;

@Controller
public class LoginController {
    
    @Autowired
    private LoginService loginService;
    
    @RequestMapping(value = "/loginMain", method = RequestMethod.GET)
    public String home() {
        System.out.println("loginMain()...");
        
        ArrayList<LoginDTO> list = loginService.list();
        
        for(LoginDTO dto : list) {
            System.out.println("ID: " + dto.getId());
            System.out.println("PW: " + dto.getPw());
            System.out.println("-------------");
        }
        
        return "login";
    }
}
