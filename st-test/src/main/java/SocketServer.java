import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketServer {

    private static final Logger log = LoggerFactory.getLogger(SocketServer.class);

    public static void main(String[] args) {
        int port = 33333;

        log.info("서버를 시작합니다... 포트: {}", port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("서버 준비 완료. 클라이언트 연결을 기다리는 중...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    log.info("클라이언트 연결이 승인되었습니다: {}", clientSocket.getInetAddress());
                    log.info("클라이언트와 연결되었습니다.");

                    // 클라이언트로부터 데이터 읽기
                    InputStream in = clientSocket.getInputStream();
                    BufferedInputStream bufferedIn = new BufferedInputStream(in);
                    byte[] buffer = new byte[1024];
                    int bytesRead = bufferedIn.read(buffer);

                    if (bytesRead > 0) {
                        String fullData = new String(buffer, 0, bytesRead, StandardCharsets.US_ASCII);
                        log.info("수신한 메시지(문자열, STX/ETX 포함):\n{}", fullData);

                        String readableData = fullData.replace("\u0002", "").replace("\u0003", "");
                        log.info("수신한 데이터(문자열):\n{}", readableData);

                        // 데이터의 마지막 3자리 추출 (전문 번호)
                        String fileNumber = readableData.substring(readableData.length() - 3);

                        // 전문 전송 시작 로그 출력
                        logTransactionStart(fileNumber);

                        // 전문 처리 (로직은 필요에 따라 handleTransactionLogic 메서드에서 구현 가능)
                        handleTransactionLogic(fileNumber);

                        // 전문 전송 종료 로그 출력
                        logTransactionEnd(fileNumber);
                    }

                    // 응답 처리
                    String responseCode = "0000"; // 성공 응답 코드
                    byte[] response = prepareResponse(responseCode);

                    // 클라이언트로 응답 전송
                    OutputStream out = clientSocket.getOutputStream();
                    BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
                    bufferedOut.write(response);
                    bufferedOut.flush();
                    log.info("응답 데이터를 전송하였습니다.");

                    // 송신 데이터의 바이트 포맷 로그 출력
                    StringBuilder responseByteData = new StringBuilder();
                    for (byte b : response) {
                        responseByteData.append(String.format("0x%02X ", b));
                    }
                    log.info("송신한 응답 데이터(바이트):");
                    log.info(responseByteData.toString());

                    // 송신 데이터를 문자열로 출력
                    String responseString = new String(response, StandardCharsets.US_ASCII);
                    log.info("송신한 응답 데이터(문자열):\n{}", responseString);

                } catch (IOException e) {
                    log.error("클라이언트를 처리하는 동안 오류가 발생했습니다: {}", e.getMessage());
                } finally {
                    log.info("클라이언트와의 연결이 종료됩니다.");
                }
            }
        } catch (IOException e) {
            log.error("서버 소켓 생성 중 오류가 발생했습니다: {}", e.getMessage());
        }
    }

    /**
     * 전문 전송 시작 로그 출력
     * @param fileNumber 전문 코드
     */
    private static void logTransactionStart(String fileNumber) {
        switch (fileNumber) {
            case "802":
                log.info("---------------------------------------- 선불카드 전문 전송 시작 ----------------------------------------");
                break;

            case "484":
                log.info("---------------------------------------- 후불정산 전문 전송 시작 ----------------------------------------");
                break;

            case "499":
                log.info("---------------------------------------- 일마감명세 전문 전송 시작 ----------------------------------------");
                break;

            default:
                log.warn("유효하지 않은 파일 번호입니다: {}", fileNumber);
                break;
        }
    }

    /**
     * 전문 전송 종료 로그 출력
     * @param fileNumber 전문 코드
     */
    private static void logTransactionEnd(String fileNumber) {
        switch (fileNumber) {
            case "802":
                log.info("---------------------------------------- 선불카드 전문 종료 ----------------------------------------");
                break;

            case "484":
                log.info("---------------------------------------- 후불정산 전문 종료 ----------------------------------------");
                break;

            case "499":
                log.info("---------------------------------------- 일마감명세 전문 종료 ----------------------------------------");
                break;

            default:
                log.warn("유효하지 않은 파일 번호입니다: {}", fileNumber);
                break;
        }
    }

    /**
     * 전문 처리 로직
     * @param fileNumber 전문 코드
     */
    private static void handleTransactionLogic(String fileNumber) {
        // 실제 전문 처리 로직 추가 가능 (현재는 로그만 출력)
        switch (fileNumber) {
            case "802":
                log.info("선불카드 전문 처리 중...");
                break;

            case "484":
                log.info("후불정산 전문 처리 중...");
                break;

            case "499":
                log.info("일마감명세 전문 처리 중...");
                break;

            default:
                log.warn("알 수 없는 파일 번호 처리 중: {}", fileNumber);
                break;
        }
    }

    /**
     * 응답 데이터 생성
     * @param responseCode 응답 코드
     * @return 응답 데이터 바이트 배열
     */
    private static byte[] prepareResponse(String responseCode) {
        byte stx = 0x02;
        byte etx = 0x03;

        String messageLength = "0038"; // 메시지 길이
        String sendCode = "MCOL00";
        String recvCode = "XXX999";
        String businessCode = "2000";
        String taskCode = "0020";

        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

        String responseMessage = messageLength + sendCode + recvCode + businessCode + taskCode + timestamp + responseCode;

        byte[] response = new byte[responseMessage.length() + 2]; // STX와 ETX 포함
        response[0] = stx;
        System.arraycopy(responseMessage.getBytes(StandardCharsets.US_ASCII), 0, response, 1, responseMessage.length());
        response[response.length - 1] = etx;

        return response;
    }
}