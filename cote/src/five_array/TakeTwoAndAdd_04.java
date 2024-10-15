package five_array;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Step 4
 * 문제: 두개뽑아더하기
 * 정수배열 numbers가 주어진다.
 * numbers에서 서로 다른 인덱스에 있는 2개의 수를 뽑아 더해 만들 수 있는 모든 수를 배열에
 * 오름차순으로 담아 반환하는 solutin()함수 완성하라.

 * 제약조건 1. numbers의 길이는 2이상 100 이하.
 * 제약조건2. numbers의 모든 수는 0이상 100 이하
 */
public class TakeTwoAndAdd_04 {
    public static void main(String[] args) {
        int[] arr = { 2, 1, 3, 4, 1 };
        System.out.println("solutions: " + Arrays.toString(solutions(arr)));
    }

    private static int[] solutions(int[] org) {
        HashSet<Integer> set = new HashSet<Integer>();

        System.out.println(Arrays.toString(org));

        for (int i = 0; i < org.length - 1; i++) {
            for (int j = i + 1; j < org.length; j++) {
                set.add(org[i] + org[j]);
            }
        }
        System.out.println("solutions set: " + set);

        return set.stream().sorted().mapToInt(Integer::intValue).toArray();
    }
}
