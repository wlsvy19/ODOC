package seven_queue;

import java.util.ArrayDeque;
import java.util.Queue;

public class QueueExercise {
    public static void main(String[] args) {
        Queue<Integer> queue = new ArrayDeque<>();

        queue.add(1);
        queue.add(2);
        queue.add(3);
        int first = 0;
        // 큐의 맨 앞 데이터를 제거 하면서 반환
        if (!queue.isEmpty()) {
            first = queue.poll();
        }

        System.out.println("first1 = " + first);

        queue.add(4);
        queue.add(5);
        if (!queue.isEmpty()) {
            first = queue.poll();
        }

        System.out.println("first2 = " + first);
        System.out.println("큐: " + queue );
    }
}
