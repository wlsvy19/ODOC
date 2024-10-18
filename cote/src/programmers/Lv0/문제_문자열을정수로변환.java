package programmers.Lv0;

/**
 * 숫자로만 이루어진 문자열 n_str이 주어질 때, n_str을 정수로 변환하여 return하도록 solution 함수를 완성해주세요.
 * <p>
 * 제한사항
 * 1 ≤ n_str ≤ 5
 * n_str은 0부터 9까지의 정수 문자로만 이루어져 있습니다.
 * 입출력 예
 * n_str	result
 * "10"	      10
 * "8542"	8542
 */
public class 문제_문자열을정수로변환 {
    public static void main(String[] args) {

    }

    public int solution(String n_str) {
        int answer = Integer.parseInt(n_str);
        return answer;
    }
}
