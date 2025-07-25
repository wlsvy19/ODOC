package gdtcs.util;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
	protected Log log = LogFactory.getLog(this.getClass());
	
    @Resource
    private TokenUtil tokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
    	log.debug("URL: " + request.getRequestURI());
    	
        try {
            String token = request.getHeader("Authorization");
            log.debug(token);
            // 영상 API에서 JWT 인증 이슈를 해결하기 위해 사용하는 임시 코드입니다. Vue.js 기반 영상 컴포넌트 개발이 필요합니다.
            //if(request.getRequestURI().startsWith("/api/video")) {
            if(request.getMethod().equals("GET")) {
                if(token == null || token == "") {
                	log.debug(token);
                	token = request.getHeader("cookie");
                }
                token = token.replace("gdtcs-auth-token=", "");
                token = "Bearer " + token;
                log.debug(token);
            }
            
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (tokenUtil.isValidToken(token)) {
                	log.debug("토큰 값이 일치합니다. " + token);
                    return true;
                } else {
                	log.debug("토큰 값은 있으나 일치하지 않습니다. " + token);
                	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    return false;
                }
            } else {
            	log.debug("유효한 토큰 값이 아닙니다. " + token);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                return false;
            }
        } catch (Exception e) {
        	log.debug("사용자 인증 처리 중 오류가 발생하였습니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }
    }
}
