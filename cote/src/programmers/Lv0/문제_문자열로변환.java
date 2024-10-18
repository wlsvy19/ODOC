package programmers.Lv0;

/**
 * 정수 n이 주어질 때, n을 문자열로 변환하여 return하도록 solution 함수를 완성해주세요.
 * <p>
 * 제한사항
 * 1 ≤ n ≤ 10000
 * <p>
 * 입출력 예
 * n	result
 * 123	"123"
 * 2573	"2573"
 */
public class 문제_문자열로변환 {
    public static void main(String[] args) {
        int n = 123;
    }

    public String solution(int n) {
        String answer = Integer.toString(n);
        return answer;
    }
}
