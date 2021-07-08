package com.fif.app.Dmybatis;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fif.app.finalexam.UserDTO;

@Controller
public class MBController {

    @RequestMapping(value = "/mbhome", method = RequestMethod.GET)
    public String mbhome() {
        System.out.println("login()");
        return "finalexam/login";
    } // end mbhome()

    @RequestMapping(value = "mbLoginAction", method = RequestMethod.POST)
    public void loginAction(@RequestBody UserDTO dto, HttpServletRequest req) throws Exception {
        System.out.println("mbLoginAction()");
        System.out.println(dto.getId() + ", " + dto.getPw());

        // stevelee, 1234zz
    } // loginAction()
}
