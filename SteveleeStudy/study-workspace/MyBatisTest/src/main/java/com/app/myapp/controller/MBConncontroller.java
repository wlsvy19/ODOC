package com.app.myapp.controller;

import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.app.myapp.dto.UserDTO;
import com.app.myapp.service.UserService;

@Controller
public class MBConncontroller {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String mbConHome(Model model) {
        ArrayList<UserDTO> list = userService.list();

        model.addAttribute("list", list);
        return "userlist";
    } // mbConHome()
}
