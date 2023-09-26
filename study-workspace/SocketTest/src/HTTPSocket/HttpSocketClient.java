package HTTPSocket;

import java.io.*;
import java.net.Socket;

public class HttpSocketClient {
	public static void main(String[] args) {
		String serverHost = "localhost"; // 서버 호스트 주소
		int serverPort = 8081; // 서버 포트 번호, 서버와 동일한 포트로 변경

		try (Socket socket = new Socket(serverHost, serverPort)) {
			// 클라이언트와 서버 간의 통신을 위한 스트림 생성
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// HTTP GET 요청 보내기
			String httpRequest = "GET / HTTP/1.1\r\n" +
					"Host: " + serverHost + "\r\n" +
					"\r\n";
			out.println(httpRequest);

			// 서버로부터 받은 HTTP 응답 읽기
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				response.append(line).append("\n");
			}

			// HTTP 응답 출력
			System.out.println("서버 응답:\n" + response.toString());

			// 클라이언트 소켓과 스트림 닫기
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
