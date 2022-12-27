package chap06;

import java.util.Scanner;

public class BubbleSort {
    public static void main(String[] args) {
        Scanner num = new Scanner(System.in);

        System.out.println("버블정렬 버전1");
        System.out.print("요소의 개수: ");
        int nx = num.nextInt();
        int x[] = new int[nx];

        for (int i = 0; i < nx; i++) {
            System.out.print("x[" + i + "]=");
            x[i] = num.nextInt(); // 배열에 값 입력
        }

        BubbleSort bs = new BubbleSort();
        bs.bubbleSort(x, nx);

        System.out.println("오름차순 정렬 후");
        for (int i = 0; i < nx; i++) {
            System.out.println("x[" + i + "]=" + x[i]);
        }

        num.close();
    } // main()

    // 버블 정렬 메소드
    public void bubbleSort(int x[], int nx) {
        for (int i = 0; i < nx - 1; i++) {
            for (int j = nx - 1; j > i; j--) {
                if (x[j - 1] > x[j]) {
                    BubbleSort bs = new BubbleSort();
                    bs.swap(x, j - 1, j);
                }
            }
        }
    } // bubbleSort()

    public void swap(int[] x, int idx1, int idx2) {
        int temp = x[idx1];
        x[idx1] = x[idx2];
        x[idx2] = temp;
    } // swap()
}
