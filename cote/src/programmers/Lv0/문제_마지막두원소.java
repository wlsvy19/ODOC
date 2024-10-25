package programmers.Lv0;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * 정수 리스트 num_list가 주어질 때,
 * 마지막 원소가 그전 원소보다 크면 마지막 원소에서 그전 원소를 뺀 값을,
 * 마지막 원소가 그전 원소보다 크지 않다면 마지막 원소를 두 배한 값을 추가하여
 * return하도록 solution 함수를 완성해주세요.
 *
 * 제한사항
 * 2 ≤ num_list의 길이 ≤ 10
 * 1 ≤ num_list의 원소 ≤ 9
 *
 * 입출력 예
 * num_list	             result
 * [2, 1, 6]	       [2, 1, 6, 5]
 * [5, 2, 1, 7, 5]	 [5, 2, 1, 7, 5, 10]
 */

public class 문제_마지막두원소 {
    public static void main(String[] args) {
        //int[] num_list = {2, 1, 6};
        //int[] num_list = {5, 2, 1, 7, 5};
        int[] num_list = {5, 2, 1, 7, 5,1,2,3,5,6};


        System.out.println("결과?: " + Arrays.toString(solution(num_list)));
    }

    public static int[] solution(int[] num_list) {
        int[] answer = new int[num_list.length + 1];
        int last = 0;

        if (num_list[num_list.length - 1] > num_list[num_list.length - 2]) {
            last = num_list[num_list.length - 1] - num_list[num_list.length - 2];
        } else {
            last = 2 * num_list[num_list.length - 1];
        }
        ArrayList<Integer> arrList = new ArrayList<>();

        for (int a : num_list) {
            arrList.add(a);
        }
        arrList.add(last);

        answer = arrList.stream().mapToInt(Integer::intValue).toArray();
        return answer;
    }
}
