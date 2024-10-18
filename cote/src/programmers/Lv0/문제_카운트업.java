package programmers.Lv0;

/**
 * 정수 start_num와 end_num가 주어질 때,
 * start_num부터 end_num까지의 숫자를 차례로 담은 리스트를 return하도록 solution 함수를 완성해주세요.
 * <p>
 * 제한사항
 * 0 ≤ start_num ≤ end_num ≤ 50
 * <p>
 * 입출력 예
 * start_num	             end_num	             result
 * 3	                       10	            [3, 4, 5, 6, 7, 8, 9, 10]
 */
public class 문제_카운트업 {
    public static void main(String[] args) {
        System.out.println(solution(3, 10));
    }

    public static int[] solution(int start_num, int end_num) {
        int[] answer = new int[end_num - start_num + 1];

        for (int i = 0; i < answer.length; i++) {
            answer[i] = start_num;
            start_num++;
        }


        return answer;
    }
}
