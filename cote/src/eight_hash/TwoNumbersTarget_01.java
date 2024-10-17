package eight_hash;

import java.util.HashSet;

/**
 * [두 개의 수로 특정값 만들기]
 * n개의 양의 정수로 이루어진 배열 arr와 정수 target이 주어졌을 때
 * 이 중에서 합이 target인 두 수가 arr에 있는지 찾고, 있으면 true, 없으면 false를 반환하는 solution() 작성
 * <p>
 * 제약조건
 * 1. n은 2이상 10000 이하의 자연수
 * 2. arr의 각 원소는 1 이상 10000 이하의 자연수
 * 3. arr의 원소 중 중복되는 원소는 없음
 * 4. target은 1 이상 20000 이하의 자연수
 * <p>
 * arr             target          return
 * [1,2,3,4,8]       6              true
 * [2,3,5,9]         10            false
 */
public class TwoNumbersTarget_01 {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 8};
        int target = 6;
        System.out.println(solution(arr, target));
    }

    // 무작정 더하기 - 시간복잡도 O(n^2)
//    private static boolean solution(int[] arr, int target) {
//        boolean answer = false;
//
//        for (int i = 0; i < arr.length; i++) {
//            for (int j = i + 1; j < arr.length; j++) {
//
//                if (arr[i] + arr[j] == target) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    private static boolean solution(int[] arr, int target) {
        HashSet<Integer> hashSet = new HashSet<>();

        for (int i : arr) {
            if (hashSet.contains(target - i)) {
                return true;
            }
            hashSet.add(i);
        }
        return false;
    }
}
