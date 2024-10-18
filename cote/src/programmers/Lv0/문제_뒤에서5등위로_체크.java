package programmers.Lv0;

import java.util.*;

/**
 * 정수로 이루어진 리스트 num_list가 주어집니다.
 * num_list에서 가장 작은 5개의 수를 제외한 수들을 오름차순으로 담은 리스트를 return하도록 solution 함수를 완성해주세요.
 * <p>
 * 제한사항
 * 6 ≤ num_list의 길이 ≤ 30
 * 1 ≤ num_list의 원소 ≤ 100
 * 입출력 예
 * num_list	                                      result
 * [12, 4, 15, 46, 38, 1, 14, 56, 32, 10]	[15, 32, 38, 46, 56]
 */
public class 문제_뒤에서5등위로_체크 {
    public static void main(String[] args) {
        int[] num_list = {12, 4, 15, 46, 38, 2, 14, 56, 32, 10};
        System.out.println(solution(num_list));

    }

    public static List<Integer> solution(int[] num_list) {
        // 리스트를 ArrayList로 변환
        List<Integer> list = new ArrayList<>();
        for (int num : num_list) {
            list.add(num);
        }

        // 리스트 정렬
        Collections.sort(list);

        // 가장 작은 5개의 수를 제외한 나머지 수들을 담은 리스트
        return list.subList(5, list.size());
    }
}
