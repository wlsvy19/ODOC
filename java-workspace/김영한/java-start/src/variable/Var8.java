package variable;

// 숫자 타입
public class Var8 {
	public static void main(String[] args) {
		// 정수: 4가지
		byte b = 127; // -128 ~ 127, 1바이트
		short s = 32767; // -32,768 ~ 32767, 2바이트
		int i = 2147483647 ;; //약 21억 -2,147,483,648 ~ 2,147,483,647, 4바이트
		// long 은 대소문자 l 을 넣어줘야함 1이랑 비슷해서 대문자 L 권장, 약 900경
		long l = 9223372036854775807L; // -9,223,372,036,854,775,808 ~ 9,223,372,036,854,775,807 , 8바이트

		// 실수: 2가지
		// float 은 f 꼭 붙여
		float f = 10.0f; // 정밀도가 낮음, 4바이트
		double d = 10.0; // 권장, 8바이트

		// 실무에서 자주 사용하는 타입
		// int, double, String, boolean, byte(파일다룰때, 바이트 단위로 파일전송 )
	}
}
