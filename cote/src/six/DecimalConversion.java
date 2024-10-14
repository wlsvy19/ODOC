package six;

import java.util.Stack;

/**
 * Step 2
 * 진수변환
 * 10진수를 입력 받아 2진수로 변환해 반환하는 solution() 함수 구현
 * 제약조건: decimal은 1이상 10억 미만의 자연수
 */
public class DecimalConversion {
    public static void main(String[] args) {
        System.out.println(solution(136));
    }

    private static String solution(int num) {
        Stack<Integer> stack = new Stack<>();

        for(int i=0; i<num; i++) {
            if(num % 2 == 0) {
                stack.push(1);
            } else {
                stack.push(0);
            }
        }


        return stack.stream()
                .map(String::valueOf) // 각 요소를 문자열로 변환
                .reduce("", (a, b) -> a + b); // 스트림을 이어붙여서 반
    }
}
