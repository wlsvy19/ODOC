package Socket;

import java.io.*;
import java.net.Socket;

public class SimpleSocketClient {
	public static void main(String[] args) throws IOException {
		String serverAddress = "localhost"; // 서버 주소
		int serverPort = 8080; // 서버 포트 번호

		try (Socket socket = new Socket(serverAddress, serverPort)) {
			// 서버와 통신할 스트림 생성
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			// 서버에 메시지 보내기
			out.println("Hello, Server!");

			// 서버로부터 응답 받기
			String response = in.readLine();
			System.out.println("서버로부터 받은 응답: " + response);

			// 클라이언트 소켓과 스트림 닫기
			out.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
