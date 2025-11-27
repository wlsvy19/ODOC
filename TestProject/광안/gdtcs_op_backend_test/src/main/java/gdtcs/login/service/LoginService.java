package gdtcs.login.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public interface LoginService {

    Map<String, Object> selectLoginInfo(Map<String, Object> param);

    Map<String, Object> processLogin(Map<String, Object> param, String clientIP, HttpServletResponse response);

    void updateLoginLog(Map<String, Object> param);
}