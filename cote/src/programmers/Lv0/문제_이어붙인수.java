package programmers.Lv0;

/**
 * 정수가 담긴 리스트 num_list가 주어집니다.
 * num_list의 홀수만 순서대로 이어 붙인 수와 짝수만 순서대로 이어 붙인 수의 합을 return하도록 solution 함수를 완성해주세요.
 * <p>
 * 제한사항
 * 2 ≤ num_list의 길이 ≤ 10
 * 1 ≤ num_list의 원소 ≤ 9
 * num_list에는 적어도 한 개씩의 짝수와 홀수가 있습니다.
 * <p>
 * <p>
 * 입출력 예
 * num_list	               result
 * [3, 4, 5, 2, 1]	        393
 * [5, 7, 8, 3]	           581
 */
public class 문제_이어붙인수 {
    public static void main(String[] args) {
        int[] num_list = {5, 7, 8, 3};
        System.out.println(solution(num_list));

    }

    public static int solution(int[] num_list) {
        int answer = 0;
        String oddStr = "";
        String evenStr = "";

        for (int num : num_list) {
            if (num % 2 != 0) {  // 홀수
                oddStr += String.valueOf(num);
            } else { // 짝수
                evenStr += Integer.toString(num);
            }
        }
        return Integer.parseInt(oddStr) + Integer.parseInt(evenStr);
    }
}
