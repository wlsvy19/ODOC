package programmers.Lv0;

/**
 * 정수 배열 numLog가 주어집니다.
 * 처음에 numLog[0]에서 부터 시작해 "w", "a", "s", "d"로 이루어진 문자열을 입력으로 받아
 * 순서대로 다음과 같은 조작을 했다고 합시다.
 *
 * "w" : 수에 1을 더한다.
 * "s" : 수에 1을 뺀다.
 * "d" : 수에 10을 더한다.
 * "a" : 수에 10을 뺀다.
 * 그리고 매번 조작을 할 때마다 결괏값을 기록한 정수 배열이 numLog입니다.
 * 즉, numLog[i]는 numLog[0]로부터 총 i번의 조작을 가한 결과가 저장되어 있습니다.
 *
 * 주어진 정수 배열 numLog에 대해 조작을 위해 입력받은 문자열을 return 하는 solution 함수를 완성해 주세요.
 *
 * 제한사항
 * 2 ≤ numLog의 길이 ≤ 100,000
 * -100,000 ≤ numLog[0] ≤ 100,000
 * 1 ≤ i ≤ numLog의 길이인 모든 i에 대해 |numLog[i] - numLog[i - 1]|의 값은 1 또는 10입니다.
 *
 * numLog	                                            result
 * [0, 1, 0, 10, 0, 1, 0, 10, 0, -1, -2, -1]     "w s d a w s d a s s w"
 */

public class 문제_수조작하기2 {
    public static void main(String[] args) {
        int[] numLog = {0, 1, 0, 10, 0, 1, 0, 10, 0, -1, -2, -1};
        System.out.println(solution(numLog));

    }
    public static String solution(int[] numLog) {
        StringBuilder result = new StringBuilder();

        for (int i = 1; i < numLog.length; i++) {
            int diff = numLog[i] - numLog[i - 1];

            switch (diff) {
                case 1:
                    result.append('w');
                    break;
                case -1:
                    result.append('s');
                    break;
                case 10:
                    result.append('d');
                    break;
                case -10:
                    result.append('a');
                    break;
            }
        }
        return result.toString();
    }

    //    public static String solution(int[] numLog) {
//        numLog[0] = 0;
//
//        String result = "wsdawsdassw";
//        for (int i = 1; i < numLog.length; i++) {
//            if (result.charAt(i) == 'w') {
//                numLog[i-1] = numLog[i] + 1;
//            } else if (result.charAt(i) == 's') {
//                numLog[i-1] = numLog[i] - 1;
//            } else if (result.charAt(i) == 'd') {
//                numLog[i-1] = numLog[i] + 10;
//            } else if (result.charAt(i) == 'a') {
//                numLog[i-1] = numLog[i] - 10;
//            }
//        }
//        System.out.println("numLog: " + numLog);
//
//        return "공백";
//    }
}
