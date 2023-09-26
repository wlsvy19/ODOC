package Socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSocketServer {
	public static void main(String[] args) {
		int port = 8080;

		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("서버가 시작되었습니다. 클라이언트의 연결을 기다립니다...");

			while (true) {
				Socket clientSocket = serverSocket.accept(); // 클라이언트 연결 대기

				// 클라이언트와 통신할 스트림 생성
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

				String message = in.readLine(); // 클라이언트로부터 메시지 수신
				System.out.println("클라이언트로부터 받은 메시지: " + message);

				// 클라이언트에게 응답 보내기
				out.println("Hello, Client!");

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
