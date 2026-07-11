package main;

import java.io.*;
import java.net.*;

public class SocketServer {
    public static void main(String[] args) {
        int port = 9998;
        System.out.println("서버 시작 중... 포트: " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("서버가 준비되었습니다. 클라이언트 연결 대기 중...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("클라이언트 연결 수락: " + clientSocket.getInetAddress());

                    // 클라이언트로부터 데이터 읽기
                    InputStream in = clientSocket.getInputStream();
                    BufferedInputStream bufferedIn = new BufferedInputStream(in);
                    byte[] buffer = new byte[1024];
                    int bytesRead = bufferedIn.read(buffer);

                    if (bytesRead > 0) {
                        // 원본 데이터를 문자열로 변환 (STX와 ETX 포함)
                        String fullData = new String(buffer, 0, bytesRead, "US-ASCII");

                        // 원본 데이터를 문자열 형태로 출력
                        System.out.println("수신된 전문 (문자열, STX/ETX 포함):");
                        System.out.println(fullData);

                        // STX와 ETX를 제거하고 순수 데이터를 추출
                        String readableData = fullData.replace("\u0002", "").replace("\u0003", "");

                        // 사람이 읽을 수 있는 데이터 출력
                        System.out.println("수신된 데이터 (문자열):");
                        System.out.println(readableData);

                        // 원본 데이터를 바이트 형태로 출력
                        System.out.println("수신된 전문 (바이트, STX/ETX 포함):");
                        for (int i = 0; i < bytesRead; i++) {
                            System.out.printf("0x%02X ", buffer[i]);
                        }
                        System.out.println();

                        // STX와 ETX를 제외한 데이터를 바이트 형태로 출력
                        System.out.println("수신된 데이터 (바이트):");
                        for (int i = 0; i < bytesRead; i++) {
                            if (buffer[i] != 0x02 && buffer[i] != 0x03) {
                                System.out.printf("0x%02X ", buffer[i]);
                            }
                        }
                        System.out.println();
                    }

                    // 데이터 처리 후 응답 생성
                    String responseCode = "0000"; // 성공 응답 코드
                    byte[] response = prepareResponse(responseCode);

                    // 클라이언트로 응답 전송
                    OutputStream out = clientSocket.getOutputStream();
                    BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
                    bufferedOut.write(response);
                    bufferedOut.flush();
                    System.out.println("응답 데이터 전송 완료.");
                } catch (IOException e) {
                    System.out.println("클라이언트 처리 중 오류 발생: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("서버 소켓 생성 중 오류 발생: " + e.getMessage());
        }
    }

    private static byte[] prepareResponse(String responseCode) {
        byte stx = 0x02;
        byte etx = 0x03;
        String messageLength = "0010"; // 응답 길이
        String responseMessage = messageLength + responseCode;

        byte[] response = new byte[responseMessage.length() + 2];
        response[0] = stx;
        System.arraycopy(responseMessage.getBytes(), 0, response, 1, responseMessage.length());
        response[response.length - 1] = etx;

        return response;
    }
}