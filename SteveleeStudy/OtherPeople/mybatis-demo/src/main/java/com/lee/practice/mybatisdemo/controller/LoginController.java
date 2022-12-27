package com.lee.practice.mybatisdemo.controller;

import com.lee.practice.mybatisdemo.dto.User;
import com.lee.practice.mybatisdemo.service.UserService;
import com.lee.practice.mybatisdemo.utils.HttpSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {

        return "index";
    }

    @PostMapping("/loginAction")
    public String loginAction(@RequestParam Map<String, String> info, HttpSession session) {

        User user = userService.findUser(info.get("id"));

        if (!user.matchId(info.get("id"))) {
            System.out.println("아이디가 틀렸습니다.");
            return "redirect:/login";
        }

        if (!user.matchPassword(info.get("pw"))) {
            System.out.println("비밀번호가 틀렸습니다.");
            return "redirect:/login";
        }


        session.setAttribute("sessionedUser", user);

        return "redirect:/loginlist";

    }

    @GetMapping("/loginlist")
    public String loginList(Model model, HttpSession session) {

        User sessinedUser = HttpSessionUtils.getUserFromSession(session);

        List<User> userList = userService.getUserHistory();

        for(User user : userList) {
            System.out.println(user.toString());
        }

        model.addAttribute("userList", userList);

        return "/loginlist";
    }

}
