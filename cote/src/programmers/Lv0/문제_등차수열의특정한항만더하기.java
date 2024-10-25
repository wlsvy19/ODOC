package programmers.Lv0;

import java.util.Arrays;

/**
 * 두 정수 a, d와 길이가 n인 boolean 배열 included가 주어집니다.

 * 첫째항이 a, 공차가 d인 등차수열에서   included[i]가 i + 1항을 의미할 때,
 * 이 등차수열의 1항부터 n항까지 included가 true인 항들만 더한 값을 return
 * 하는 solution 함수를 작성해 주세요.
 *
 * 제한사항
 * 1 ≤ a ≤ 100
 * 1 ≤ d ≤ 100
 * 1 ≤ included의 길이 ≤ 100
 * included에는 true가 적어도 하나 존재합니다.
 *
 *
 * 입출력 예
 * a	d	                 included	                                                         result
 * 3	4	         [true, false, false, true, true]	                                          37
 * 7	1	[false, false, false, true, false, false, false]	                                 10
 */

public class 문제_등차수열의특정한항만더하기 {
    public static void main(String[] args) {
        int a = 7; // 첫 째항
        int d = 1; // 공차
        boolean[] included = {false, false, false, true, false, false, false};

        // [3, 7, 11, 15, 19]
        System.out.println(solution(a, d, included));
    }

    public static int solution(int a, int d, boolean[] included) {
        int answer = 0;
        int[] intArr = new int[included.length];
        intArr[0] = a;
        for(int i = 1; i < intArr.length; i++) {
            intArr[i] = intArr[i - 1] + d;
        }
        System.out.println(Arrays.toString(intArr));

        for(int i = 0; i < included.length; i++){
            if(included[i]) {
                answer += intArr[i];
            }
        }
        return answer;
    }
}
