package six_stack;

import java.util.Stack;

/**
 * Step 2
 * 진수변환
 * 10진수를 입력 받아 2진수로 변환해 반환하는 solution() 함수 구현
 * 제약조건: decimal은 1이상 10억 미만의 자연수
 */
public class DecimalConversion_02 {
    public static void main(String[] args) {
        System.out.println(solution(50));
    }

    private static String solution(int num) {
        Stack<Integer> stack = new Stack<>();

//        while (num > 0) {
//            int reminder = num % 2;
//            stack.push(reminder);
//            num = num / 2;
//        }

        for(; num > 0 ; num = num/2){
            stack.push(num % 2);
        }

        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }

        return sb.toString();
    }

}