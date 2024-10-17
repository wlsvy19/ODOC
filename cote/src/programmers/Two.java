package programmers;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * [9로 나눈 나머지]
 * 음이 아닌 정수를 9로 나눈 나머지는 그 정수의 각 자리 숫자의 합을 9로 나눈 나머지와 같은 것이 알려져 있습니다.
 * 이 사실을 이용하여 음이 아닌 정수가 문자열 number로 주어질 때, 이 정수를 9로 나눈 나머지를 return 하는 solution 함수를 작성해주세요.
 * <p>
 * 제한사항
 * 1 ≤ number의 길이 ≤ 100,000
 * number의 원소는 숫자로만 이루어져 있습니다.
 * number는 정수 0이 아니라면 숫자 '0'으로 시작하지 않습니다.
 * <p>
 * number                      result
 * "123"                         6
 * "78720646226947352489"        2
 */


// 1. 문자열을 하나하나 자른다.
// 2. 하나하나의 잘린 문자열 들을 int로 바꾼 후 배열에 넣는다
// 3. 배열을 순회하며 각 인덱스의 값을 더한다.
// 4. 더한 값을 % 9 해서 리턴 한다
public class Two {
    public static void main(String[] args) {
        String number = "78720646226947352489";
        System.out.println(solution(number));


    }

    public static int solution(String number) {
        int answer = 0;
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            sum += Integer.parseInt(String.valueOf(number.charAt(i)));
        }
        answer = sum % 9;
        return answer;
    }
}
