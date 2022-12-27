package chap01;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("수 입력");
        int num = sc.nextInt();

        Queue<Integer> queue = new LinkedList<>();

        for (int i = 1; i <= num; i++) {
            queue.add(i); // 큐에 1부터 입력한 수까지 삽입
        }

        while (queue.size() > 1) {
            // remove: 큐의 맨 앞 삭제
            // add: 큐 맨뒤 삽입

            queue.remove(); // 제일 위에 있는 카드 버림 -> 234

            queue.add(queue.remove()); // 버려진 원소를 큐 맨뒤에 삽입 342
        }
        System.out.println(queue.remove());

   
    }
}
