package programmers.Lv0;

/**
 * 문자열에 따라 다음과 같이 두 수의 크기를 비교하려고 합니다.
 * 두 수가 n과 m이라면
 * <p>
 * ">", "=" : n >= m
 * "<", "=" : n <= m
 * ">", "!" : n > m
 * "<", "!" : n < m
 * <p>
 * 두 문자열 ineq와 eq가 주어집니다.
 * ineq는 "<"와 ">"중 하나고, eq는 "="와 "!"중 하나입니다.
 * 그리고 두 정수 n과 m이 주어질 때,
 * n과 m이 ineq와 eq의 조건에 맞으면 1을 아니면 0을 return 하도록 solution 함수를 완성해주세요.
 * <p>
 * 제한 사항
 * 1 ≤ n, m ≤ 100
 * <p>
 * 입출력 예
 * ineq	eq	n	m	result
 * "<"	"="	20	50	1
 * ">"	"!"	41	78	0
 * <p>
 * 입출력 예 #1
 * 20 <= 50은 참이기 때문에 1을 return합니다.
 * <p>
 * 입출력 예 #2
 * 41 > 78은 거짓이기 때문에 0을 return합니다.
 */
public class 문제_조건문자열 {
    public static void main(String[] args) {
        System.out.println(solution("<", "=", 20, 50));
    }

    public static int solution(String ineq, String eq, int n, int m) {
        if (ineq.equals("<") && eq.equals("=")) {
           return n <= m ? 1: 0; // 3항 연산자로 줄이기 가능
        } else if (ineq.equals("<") && eq.equals("!")) {
            if (n < m) {
                return 1;
            } else {
                return 0;
            }

        } else if (ineq.equals(">") && eq.equals("=")) {
            if (n >= m) {
                return 1;
            } else {
                return 0;
            }

        } else if (ineq.equals(">") && eq.equals("!")) {
            if (n > m) {
                return 1;
            } else {
                return 0;
            }


        }
        return 0;
    }
}