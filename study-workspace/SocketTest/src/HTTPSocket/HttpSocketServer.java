package HTTPSocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class HttpSocketServer {
	public static void main(String[] args) {
		int port = 8081; // 원하는 포트 번호로 변경 가능

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("HTTP 서버가 시작되었습니다. 포트 " + port + "에서 대기 중...");

			while (true) {
				Socket clientSocket = serverSocket.accept(); // 클라이언트 연결 대기

				// 클라이언트와 통신할 스트림 생성
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

				// HTTP 요청 메시지 읽기
				StringBuilder request = new StringBuilder();
				String line;
				while ((line = in.readLine()) != null && !line.isEmpty()) {
					request.append(line).append("\r\n");
				}

				System.out.println("클라이언트 요청:\n" + request.toString());

				// HTTP 응답 생성
				String response = "HTTP/1.1 200 OK\r\n" +
						"Server: SimpleHTTPServer\r\n" +
						"Date: " + new Date() + "\r\n" +
						"Content-Type: text/html\r\n" +
						"Content-Length: 12\r\n" +
						"\r\n" +
						"Hello, World!";

				// HTTP 응답 보내기
				out.print(response);
				out.flush();

				// 클라이언트 소켓과 스트림 닫기
				in.close();
				out.close();
				clientSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
