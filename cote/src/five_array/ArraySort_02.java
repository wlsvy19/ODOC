package five_array;

import java.util.Arrays;

/**
 * Step 2
 * 버블정렬은 N^2 의 시간 복잡도를 가져서 오래 걸림
 * sort() 메서드는 NlogN 시간 복잡도를 가짐
 *
 */
public class ArraySort_02 {
    public static void main(String[] args) {
        int[] arr = new int[100000];
        long start = System.currentTimeMillis();
        int[] bubble = bubbleSort(arr);
        long end = System.currentTimeMillis();

        // 버블정렬 코드 시간 측정
        System.out.println((end - start) / 1000.0 + " 초");

        start = System.currentTimeMillis();
        int[] sort = doSort(arr);
        end = System.currentTimeMillis();

        // 정렬 API 코드 시간 측정
        System.out.println((end - start) / 1000.0 + " 초");

        System.out.println(Arrays.equals(bubble, sort));

    }

    private static int[] bubbleSort(int[] org) {
        int[] arr = org.clone();
        int n = arr.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if(arr[j] > arr[j + 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }

        return arr;
    }

    private static int[] doSort(int[] org) {
        int[] clone = org.clone();
        Arrays.sort(clone);
        return clone;
    }
}
