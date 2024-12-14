import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketServer {
    public static void main(String[] args) {
        int port = 33333;

        System.out.println("Server is starting... Port: " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is ready. Waiting for client connection...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Client connection accepted: " + clientSocket.getInetAddress());

                    // 클라이언트로부터 데이터 읽기
                    InputStream in = clientSocket.getInputStream();
                    BufferedInputStream bufferedIn = new BufferedInputStream(in);
                    byte[] buffer = new byte[1024];
                    int bytesRead = bufferedIn.read(buffer);

                    if (bytesRead > 0) {
                        // 원본 데이터를 문자열로 변환 (STX와 ETX 포함)
                        String fullData = new String(buffer, 0, bytesRead, StandardCharsets.US_ASCII);

                        // 원본 데이터를 문자열 형태로 출력
                        System.out.println("Received message (string, including STX/ETX):");
                        System.out.println(fullData);

                        // STX와 ETX를 제거하고 순수 데이터를 추출
                        String readableData = fullData.replace("\u0002", "").replace("\u0003", "");

                        // 사람이 읽을 수 있는 데이터 출력
                        System.out.println("Received data (string):");
                        System.out.println(readableData);

                        // 원본 데이터를 바이트 형태로 출력
                        System.out.println("Received message (bytes, including STX/ETX):");
                        for (int i = 0; i < bytesRead; i++) {
                            System.out.printf("0x%02X ", buffer[i]);
                        }
                        System.out.println();

                        // STX와 ETX를 제외한 데이터를 바이트 형태로 출력
                        System.out.println("Received data (bytes):");
                        for (int i = 0; i < bytesRead; i++) {
                            if (buffer[i] != 0x02 && buffer[i] != 0x03) {
                                System.out.printf("0x%02X ", buffer[i]);
                            }
                        }
                        System.out.println();
                    }

                    // 데이터 처리 후 응답 생성
                    String responseCode = "0001"; // 성공 응답 코드
                    byte[] response = prepareResponse(responseCode);

                    // 클라이언트로 응답 전송
                    OutputStream out = clientSocket.getOutputStream();
                    BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
                    bufferedOut.write(response);
                    bufferedOut.flush();
                    System.out.println("Response data sent.");

                    // 서버에서 클라이언트로 보낸 응답 데이터 출력
                    System.out.println("Sent response (bytes):");
                    for (byte b : response) {
                        System.out.printf("0x%02X ", b);
                    }
                    System.out.println();

                    // 응답 데이터를 문자열로 변환하여 출력
                    String responseString = new String(response, StandardCharsets.US_ASCII);
                    System.out.println("Sent response (string):");
                    System.out.println(responseString);

                } catch (IOException e) {
                    System.out.println("Error occurred while processing the client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error occurred while creating the server socket: " + e.getMessage());
        }
    }

    private static byte[] prepareResponse(String responseCode) {
        byte stx = 0x02;
        byte etx = 0x03;

        // 응답 메시지 길이
        String messageLength = "0038"; // 응답 길이

        // 시스템 구분 코드 (Send) 및 (Receive)
        String sendCode = "MCOL00";
        String recvCode = "XXX999";

        // 업무 구분 코드 및 작업 구분 코드
        String businessCode = "2000";
        String taskCode = "0020";

        // 송수신 일시
        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());

        // 응답 코드
        String responseMessage = messageLength + sendCode + recvCode + businessCode + taskCode + timestamp + responseCode;

        // 바이트 배열로 응답 메시지 준비
        byte[] response = new byte[responseMessage.length() + 2];  // +2는 STX와 ETX를 위한 공간

        response[0] = stx;
        System.arraycopy(responseMessage.getBytes(StandardCharsets.US_ASCII), 0, response, 1, responseMessage.length());
        response[response.length - 1] = etx;

        return response;
    }
}
