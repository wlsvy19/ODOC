package programmers.Lv0;

/**
 * 정수 리스트 num_list와 찾으려는 정수 n이 주어질 때,
 * num_list안에 n이 있으면 1을 없으면 0을 return하도록 solution 함수를 완성해주세요.
 * <p>
 * 제한사항
 * 3 ≤ num_list의 길이 ≤ 100
 * 1 ≤ num_list의 원소 ≤ 100
 * 1 ≤ n ≤ 100
 * <p>
 * 입출력 예
 * num_list	                n	                 result
 * [1, 2, 3, 4, 5]	        3	                    1
 * [15, 98, 23, 2, 15]	    20	                    0
 */
public class 문제_정수찾기 {
    public static void main(String[] args) {
        int[] num_list = {1, 2, 3, 4, 5};
        int n = 3;

        System.out.println(solution(num_list, n));
    }

    public static int solution(int[] num_list, int n) {
        int answer = 0;

        for (int num : num_list) {
            if (num == n) {
                answer = 1;
                break;
            }
        }

        return answer;
    }
}
