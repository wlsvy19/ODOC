package six_stack;

import java.util.Stack;

/**
 * Step 1
 * 문제: 올바른 괄호
 * 괄호가 바르게 짝지어졌다는 것은 '(' 문자로 열렸으면 반드시 짝지어서 ')' 문자로 닫아야 한다는 뜻입니다.
 * 예를들어 "()()" 또는 "(())()" 는 올바른 괄호입니다.
 * ")()(" 또는 "(()(" 는 올바르지 않은 괄호입니다.
 * '(' 또는 ')' 로만 이루어진 문자열 s가 주어졌을 때, 문자열 s가 올바른 괄호이면 true 를 반환하고 아니면 false를 반환하는 solution 함수를 완성해주세요.
 * 제약조건1. 문자열 s의 길이: 100000 이하의 자연수
 * 제약조건2. 문자열 s는 '(' 또는 ')' 로만 이루어져 있습니다.
 */
public class CorrectParentheses_01 {
    public static void main(String[] args) {
        String s = "(())((()))()()()((()))((((()))))";
        System.out.println(solution(s));

    }

    private static boolean solution(String s) {
        Stack<Character> stack = new Stack<>();
        stack.push('9');
        System.out.println("peek: " + stack.peek());


        Stack<String> strStack = new Stack<>();
        strStack.push("hello");
        System.out.println(strStack.peek());


//        for (char c : s.toCharArray()) {
//            // 여는 괄호는 스택에 넣기
//            if (c == '(') {
//                stack.push(c);
//
//                // 닫는 괄호는 스택에서 제거하기
//            } else if (c == ')') {
//                // 스택이 비어 있으면 올바르지 않은 괄호
//                if (stack.isEmpty()) {
//                    return false;
//                }
//                stack.pop();
//            }
//        }
//        // 스택이 비어 있으면 모든 괄호의 짝이 맞는 것
//        System.out.println(stack.peek());
        return stack.isEmpty();

    }
}
