package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA256 알고리즘을 적용한 암호화
 */
public class SecurityUtil {
    public static String encryptSHA256(String str) {
        String retStr = "";
        try {
            // SHA-256 인스턴스 생성
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte[] byteData = sh.digest();

            // 바이트 데이터를 16진수 문자열로 변환
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(String.format("%02x", byteData[i]));
            }
            retStr = sb.toString();
        } catch (NoSuchAlgorithmException ae) {
            retStr = null;
        }
        return retStr;
    }
}
