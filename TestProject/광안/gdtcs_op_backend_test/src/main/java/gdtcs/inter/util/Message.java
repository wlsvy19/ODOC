package gdtcs.inter.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Message {
    private static final int CONNECTION_TIMEOUT = 1_800_000; // 30분 (밀리초 단위)

    public static Map<String, Object> sendMessage(String host, int port, String icCode, String calcDate, String fileNumber) {
        Map<String, Object> result = new HashMap<>();
        try (Socket socket = new Socket()) {
            log.info("외부연계 서버에 연결중입니다. [호스트: {}, 포트: {}, 타임아웃: {}ms]", host, port, CONNECTION_TIMEOUT);

            // 서버 연결 설정 (5분 타임아웃 적용)
            socket.connect(new InetSocketAddress(host, port), CONNECTION_TIMEOUT);
            // 소켓 읽기 타임아웃 설정
            socket.setSoTimeout(CONNECTION_TIMEOUT);

            log.info("서버 연결 성공. [호스트: {}, 포트: {}]", host, port);

            byte stx = 0x02;
            byte etx = 0x03;

            /* 송수신 전문 인터페이스 참고 */
            String messageLength = "0049";
            String sendCode = "MCOL00";
            String recvCode = icCode + "XXX";
            String businessCode = "2000";
            String taskCode = "0010";
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String responseCode = "0000";

            byte[] message = new byte[55];
            int idx = 0;

            message[idx++] = stx;
            System.arraycopy(messageLength.getBytes(StandardCharsets.UTF_8), 0, message, idx, 4);
            idx += 4;
            System.arraycopy(sendCode.getBytes(StandardCharsets.UTF_8), 0, message, idx, 6);
            idx += 6;
            System.arraycopy(recvCode.getBytes(StandardCharsets.UTF_8), 0, message, idx, 6);
            idx += 6;
            System.arraycopy(businessCode.getBytes(StandardCharsets.UTF_8), 0, message, idx, 4);
            idx += 4;
            System.arraycopy(taskCode.getBytes(StandardCharsets.UTF_8), 0, message, idx, 4);
            idx += 4;
            System.arraycopy(timestamp.getBytes(StandardCharsets.UTF_8), 0, message, idx, 14);
            idx += 14;
            System.arraycopy(responseCode.getBytes(StandardCharsets.UTF_8), 0, message, idx, 4);
            idx += 4;
            System.arraycopy(calcDate.getBytes(StandardCharsets.UTF_8), 0, message, idx, 8);
            idx += 8;
            System.arraycopy(fileNumber.getBytes(StandardCharsets.UTF_8), 0, message, idx, 3);
            idx += 3;
            message[idx] = etx;

            logMessageDetails(message);

            // 전문 송수신
            try (BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
                 BufferedInputStream in = new BufferedInputStream(socket.getInputStream())) {

                // 응답 시간 측정 시작
                long startTime = System.currentTimeMillis();

                // Message 전송
                out.write(message);
                out.flush();
                log.info("전문 전송 완료. [호스트: {}, 포트: {}]", host, port);

                // 서버 응답 수신
                byte[] responseBuffer = new byte[1024];
                int bytesRead;
                StringBuilder responseData = new StringBuilder();

                while ((bytesRead = in.read(responseBuffer)) != -1) {
                    responseData.append(new String(responseBuffer, 0, bytesRead, StandardCharsets.UTF_8));
                }

                // 응답 소요 시간 ms 단위
                long endTime = System.currentTimeMillis();
                long responseTime = endTime - startTime;
                if (!responseData.isEmpty()) {
                    log.info("서버로부터 응답 데이터: {}", responseData.toString());
                    log.info("서버 응답 소요 시간: {}ms", responseTime);
                    processServerResponse(responseData.toString(), result);
                } else {
                    result.put("status", -1);
                    result.put("message", "서버로부터 응답을 받지 못했습니다.");
                    log.info("서버 응답 소요 시간: {}ms", responseTime);
                }
                // 응답 시간 결과에 포함
                result.put("responseTime", responseTime);
            } finally {
                log.info("소켓 연결을 종료합니다. [호스트: {}, 포트: {}]", host, port);
            }
        } catch (Exception e) {
            log.error("외부연계 서버 연결 실패: {}", e.getMessage(), e);
            result.put("status", -1);
            result.put("message", "외부연계서버 연결에 실패했습니다.");
        }
        return result;
    }

    private static void logMessageDetails(byte[] message) {
        String messageString = new String(message, StandardCharsets.UTF_8);
        log.info("전송 전문 (문자열): {}", messageString);

        StringBuilder byteLog = new StringBuilder();
        for (byte b : message) {
            byteLog.append(String.format("0x%02X ", b));
        }
        log.info("전송 전문 (바이트): {}", byteLog.toString().trim());
    }

    private static void processServerResponse(String response, Map<String, Object> result) {
        char responseCodeFromServer = response.charAt(42);
        if (responseCodeFromServer == '0') {
            result.put("status", 0);
            result.put("message", "정산요청이 성공적으로 처리되었습니다.");
        } else if (responseCodeFromServer == '1') {
            result.put("status", 1);
            result.put("message", "정산요청에 실패했습니다. 관리자에게 문의하세요.");
        } else {
            result.put("status", -1);
            result.put("message", "알 수 없는 응답 코드입니다.");
        }
        result.put("response", response);
    }
}