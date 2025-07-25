package gdtcs.util;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

@Component
public class CryptoUtil {

    @Value("${security.aes256.private.key}")
    private String privateKey;

    @Value("${security.aes256.public.key}")
    private String publicKey;

    public String encryptPrivateAES256(String value) throws Exception {
        return encryptAES256(getPrivateKeySpec(), value);
    }

    public String decryptPrivateAES256(String value) throws Exception {
        return decryptAES256(getPrivateKeySpec(), value);
    }

    public String encryptPublicAES256(String value) throws Exception {
        return encryptAES256(getPublicKeySpec(), value);
    }

    public String decryptPublicAES256(String value) throws Exception {
        return decryptAES256(getPublicKeySpec(), value);
    }

    private SecretKeySpec getPrivateKeySpec() {
        byte[] bytes = new byte[32];
        bytes = Arrays.copyOfRange(Base64Utils.decodeFromString(privateKey), 0, 32);

        return new SecretKeySpec(bytes, "AES");
    }

    private SecretKeySpec getPublicKeySpec() {
        byte[] bytes = new byte[32];
        bytes = Arrays.copyOfRange(Base64Utils.decodeFromString(publicKey), 0, 32);

        return new SecretKeySpec(bytes, "AES");
    }

    private String encryptAES256(SecretKeySpec spec, String value) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, spec);

        String encrypted = Base64Utils.encodeToString(cipher.doFinal(value.getBytes()));
        return encrypted;
    }

    private String decryptAES256(SecretKeySpec spec, String value) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, spec);

        String decrypted = new String(cipher.doFinal(Base64Utils.decodeFromString((value))));
        return decrypted;
    }

    public String encryptUserNo(int i) throws Exception {
        String sumUserNo = "122" + i + "134";
        return base64encoding(encryptPublicAES256(sumUserNo));
    }

    public int decryptUserNo(String value) throws Exception {
        String sumUserNo = decryptPublicAES256(base64decoding(value));
        int length = sumUserNo.length();

        // String userNo = sumUserNo.substring(3).substring(0, length - 6);
        String userNo = "0";

        return Integer.parseInt(userNo);
    }

//    public String makeEncryptedTrackingShareId() throws Exception{
//        String share_id = System.currentTimeMillis()+RandomUtil.makeText();
//        return base64encoding(encryptPublicAES256(share_id));
//    }

    public String encryptTrackingShareId(String trackingNo) throws Exception {
        String sumTrackingNo = "122" + trackingNo + "134";
        return base64encoding(encryptPublicAES256(sumTrackingNo));
    }

    public int decryptTrackingShareId(String value) throws Exception {
        String sumTrackingNo = decryptPublicAES256(base64decoding(value));
        int length = sumTrackingNo.length();

        String trackingNo = sumTrackingNo.substring(3).substring(0, length - 6);

        return Integer.parseInt(trackingNo);
    }

    public String base64encoding(String value) {
        return Base64Utils.encodeToString(value.getBytes());
    }

    public String base64decoding(String value) {
        return new String(Base64Utils.decodeFromString(value));
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }
}
