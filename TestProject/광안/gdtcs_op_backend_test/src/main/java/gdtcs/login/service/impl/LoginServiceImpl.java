package gdtcs.login.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import gdtcs.login.mapper.LoginMapper;
import gdtcs.login.service.LoginService;
import gdtcs.util.CookieUtil;
import gdtcs.util.SecurityUtil;
import gdtcs.util.TokenUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service("loginService")
public class LoginServiceImpl extends EgovAbstractServiceImpl implements LoginService {

    protected Log log = LogFactory.getLog(this.getClass());

    @Resource(name = "loginMapper")
    private LoginMapper loginMapper;

    @Resource
    private TokenUtil tokenUtil;

    @Override
    public Map<String, Object> processLogin(Map<String, Object> param, String clientIP, HttpServletResponse response) {
        Map<String, Object> retMap = new HashMap<>();

        try {
            String encodedPw = SecurityUtil.encryptSHA256((String) param.get("pw"));
            param.put("pw", encodedPw);

            Map<String, Object> loginInfo = selectLoginInfo(param);

            if (loginInfo != null) {
                String accessToken = tokenUtil.generateJwtToken(loginInfo);
                // 토큰->쿠키에넣어 9시간 유지, 9시간 지나면 재로그인
                CookieUtil.addCookie(response, "gdtcs-auth-token", accessToken, 3600 * 9);

                retMap.put("code", "SUCCESS");
                retMap.put("accessToken", accessToken);
                retMap.put("loginTime", getCurrentTime());
                retMap.put("IP", clientIP);
                retMap.putAll(loginInfo);

                // 로그인 이력 insert start
                Map<String, Object> logParam = new HashMap<>();
                logParam.put("IC_CODE", param.get("icCode"));

                String loginTime = (String) retMap.get("loginTime");
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(loginTime, inputFormatter);
                String loginDate = dateTime.toLocalDate().toString(); // yyyy-MM-dd 형식
                String loginTm = dateTime.toLocalTime().toString();   // HH:mm:ss 형식

                logParam.put("LOGIN_DATE", loginDate);
                logParam.put("LOGIN_TM", loginTm);


                logParam.put("WORKER_NO", param.get("workerNo"));
                logParam.put("LOGIN_DIV", "1");
                logParam.put("IP_ADDR", clientIP);
                loginMapper.insertLoginLog(logParam);
                // 로그인 이력 insert end

                log.info(String.format(
                        "✅ [LOGIN SUCCESS] User: '%s' (ID: %s) logged in from IP [%s] at [%s]",
                        loginInfo.get("WORKER_NM"), loginInfo.get("WORKER_NO"), clientIP, getCurrentTime()
                ));
            } else {
                retMap.put("message", "아이디 또는 비밀번호를 확인해주세요.");
                retMap.put("code", "NOUSER");
                log.warn(String.format(
                        "❌ [LOGIN FAILED] 사용자 정보를 찾을 수 없습니다. 로그인 시도 ID: [%s] IP: [%s] 시간: [%s]",
                        param.get("workerNo"), clientIP, getCurrentTime()
                ));
            }

        } catch (Exception e) {
            retMap.put("code", "FAIL");
            retMap.put("message", "서버 에러로 인해 로그인에 실패했습니다. 문제가 지속되면 관리자에게 문의하세요.");
            log.error(String.format("🚨 [LOGIN ERROR] Exception occurred during login process. IP: [%s], Time: [%s]", clientIP, getCurrentTime()), e);
        }

        return retMap;
    }

    @Override
    public Map<String, Object> selectLoginInfo(Map<String, Object> param) {
        return loginMapper.selectLoginInfo(param);
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date());
    }

    @Override
    public void updateLoginLog(Map<String, Object> param) {
        String icCode = (String) param.get("IC_CODE");
        String workerNo = (String) param.get("WORKER_NO");

        String loginTime = (String) param.get("LOGIN_TIME");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(loginTime, inputFormatter);
        String loginDate = dateTime.toLocalDate().toString(); // yyyy-MM-dd 형식
        String loginTm = dateTime.toLocalTime().toString();   // HH:mm:ss 형식

        Map<String, Object> updateParam = new HashMap<>();
        updateParam.put("IC_CODE", icCode);
        updateParam.put("WORKER_NO", workerNo);
        updateParam.put("LOGIN_DATE", loginDate);
        updateParam.put("LOGIN_TM", loginTm);

        loginMapper.updateLoginLog(updateParam);
    }

    /**
     * 1시간 주기로 9시간 지난 데이터 LOGIN_DIV = '2' 로 업데이트
     */
    @Scheduled(cron = "0 0 * * * *") // 1시간 주기로 실행(초, 분, 시, 일, 월, 요일)
    @Transactional
    public void cleanOldLoginLogs() {
        try {
            log.info("[SCHEDULER] Old login logs cleanup process started.");

            loginMapper.updateOldLoginLogs();

            log.info("[SCHEDULER] Old login logs cleanup process finished successfully.");
        } catch (Exception e) {
            log.error("[SCHEDULER ERROR] Exception occurred during cleanup process.", e);
        }
    }
}