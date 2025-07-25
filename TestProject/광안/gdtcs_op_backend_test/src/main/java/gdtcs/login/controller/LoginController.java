package gdtcs.login.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import gdtcs.login.service.LoginService;
import gdtcs.util.ClientIpExtractor;

@Controller
@RequestMapping("/api")
public class LoginController {
    protected Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public @ResponseBody Map<String, Object> loginInfo(@RequestBody Map<String, Object> param, HttpServletRequest req,
                                                       HttpServletResponse res) {
        String clientIP = ClientIpExtractor.extract(req);
        return loginService.processLogin(param, clientIP, res);
    }

    @PostMapping("/logout")
    public @ResponseBody void logout(@RequestBody Map<String, Object> param) {
        loginService.updateLoginLog(param);
    }
}