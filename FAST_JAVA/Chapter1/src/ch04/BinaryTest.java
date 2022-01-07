package ch04;

public class BinaryTest {
    public static void main(String[] args) {
        int num = 10; // 정수
        int binaryNum = 0B1010; // 0B뒤에 나오는 숫자는 Binary 즉, 이진수.
        int octalNum = 012; // 0뒤에 나오는 숫자는 8진수.
        int hexadecimalNum = 0XA; // 0X뒤에 나오는 숫자는 16진수.

        System.out.println(num);
        System.out.println(binaryNum);
        System.out.println(octalNum);
        System.out.println(hexadecimalNum);
    }
}
