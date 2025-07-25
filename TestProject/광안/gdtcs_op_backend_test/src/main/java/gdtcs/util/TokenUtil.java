package gdtcs.util;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TokenUtil {
    private final CryptoUtil cryptoUtil;
    private final String SECRET_KEY = "gdtcs123223222SecretKey";
    private final String DATA_KEY = "data";
    private final int ACCESS_TOKEN_TIME = 1000 * 60 * 60 * 9; // 9시간 세션
    private final int ACCESS_TOKEN_TIME_REBUILD = 1000 * 60 * 60 * 1; // 1시간 이하 토큰 REBUILD

    public String generateJwtToken(Map<String, Object> loginInfo) {
        return Jwts.builder().setSubject(String.valueOf(loginInfo.get("workerNo"))).setHeader(createHeader())
                .setClaims(createClaims(loginInfo))
                .setExpiration(createExpireDate(ACCESS_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, createSigningKey(SECRET_KEY))
                .compact();
    }

    public Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "ACCESS_TOKEN");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    public Map<String, Object> createClaims(Map<String, Object> loginInfo) {
        Map<String, Object> claims = new HashMap<>();
        try {
            claims.put(DATA_KEY, cryptoUtil.encryptUserNo((Integer) loginInfo.get("workerNo")));
        } catch (Exception e) {
            claims.put(DATA_KEY, "");
        }
        return claims;
    }

    private Date createExpireDate(long expireDate) {
        long curTime = System.currentTimeMillis();
        return new Date(curTime + expireDate);
    }

    private Key createSigningKey(String key) {
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(key);
        return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public boolean isValidToken(String token) throws Exception {
        try {
            Claims accessClaims = getClaimsFormToken(token);
            this.rebuildToken(accessClaims);
            return true;
        } catch (ExpiredJwtException e) {
            throw new Exception("Token Expired UserID - " + e.getClaims().getSubject());
        } catch (JwtException e) {
            throw new Exception("Token Tampered - " + e);
        } catch (NullPointerException e) {
            throw new Exception("Token is null - " + e);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private Claims getClaimsFormToken(String token) {
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).parseClaimsJws(token)
                .getBody();
    }

    private void rebuildToken(Claims accessClaims) {
        try {
            Date expDate = accessClaims.getExpiration();
            Date nowDate = new Date();
            long term = expDate.getTime() - nowDate.getTime();
            if (term < ACCESS_TOKEN_TIME_REBUILD) {
                int workerNo = cryptoUtil.decryptUserNo(String.valueOf(accessClaims.get(DATA_KEY)));
                Map<String, Object> loginInfo = new HashMap<>();
                loginInfo.put("workerNo", workerNo);

                String newAccessToken = this.generateJwtToken(loginInfo);
                HttpServletResponse response = ContextUtils.getResponse();
                CookieUtil.addCookie(response, "gdtcs-auth-token", newAccessToken, -1);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int getAccessTokenWorkerNo(String token) {
        try {
            Claims accessClaims = getClaimsFormToken(token);
            return cryptoUtil.decryptUserNo(String.valueOf(accessClaims.get(DATA_KEY)));
        } catch (Exception e) {
            // 로그 출력 또는 예외 처리
        }
        return 0;
    }
}
