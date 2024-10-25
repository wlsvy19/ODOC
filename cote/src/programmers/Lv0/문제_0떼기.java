package programmers.Lv0;

/**
 * 정수로 이루어진 문자열 n_str이 주어질 때,
 * n_str의 가장 왼쪽에 처음으로 등장하는 0들을 뗀 문자열을 return하도록 solution 함수를 완성해주세요.
 * <p>
 * 제한사항
 * 2 ≤ n_str ≤ 10
 * n_str이 "0"으로만 이루어진 경우는 없습니다.
 * <p>
 * 입출력 예
 * n_str	result
 * "0010"	"10"
 * "854020"	"854020"
 */

public class 문제_0떼기 {
    public static void main(String[] args) {
        String n_str = "00010";
        System.out.println("solution(n_str): " + solution(n_str));
    }

    public static String solution(String n_str) {
        int index = 0;

        // for 문을 사용하여 왼쪽에서 처음 등장하는 '0'의 인덱스를 찾기
        for (index = 0; index < n_str.length(); index++) {
            if (n_str.charAt(index) != '0') {
                break; // '0'이 아닌 문자를 찾으면 루프를 종료
            }
        }

        // '0'이 아닌 첫 번째 문자부터 시작하는 서브 문자열 반환
        return n_str.substring(index);
    }
}
