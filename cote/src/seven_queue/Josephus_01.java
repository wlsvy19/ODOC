package seven_queue;

/*
 [요세푸스 문제]
 N명의 사람이 원 형태로 서있습니다. 각 사람은 1부터 N까지 번호표를 갖고 있습니다.
 그리고 임의의 숫자 K가 주어졌을 때 다음과 같이 사람을 없앱니다.
 1) 1번 번호표를 가진 사람을 기준으로 K번째 사람을 없앱니다.
 2) 없앤 사람 다음 사람을 기준으로 하고 다시 K번째 사람을 없앱니다.

 N과 K가 주어질 때 마지막에 살아있는 사람의 번호를 반환하는 solution() 구현

 제약조건) N과 K는 1이상 1000이하의 자연수입니다.
 N=5
 K=2
 return=3
 */


import java.util.ArrayDeque;
import java.util.Deque;

public class Josephus_01 {

    public static void main(String[] args) {
        System.out.println(solution(5, 3));  // 출력: 3
    }

    public static int solution(int N, int K) {
        // 1부터 N까지의 사람을 ArrayDeque에 추가
        Deque<Integer> deque = new ArrayDeque<>();
        for (int i = 1; i <= N; i++) {
            deque.addLast(i);
        }

        while (deque.size() > 1) {
            // K-1번 앞의 사람을 뒤로 이동시킴
            for (int i = 0; i < K - 1; i++) {
                deque.addLast(deque.pollFirst()); // 앞에서 빼서 뒤로 넣음
                deque.addLast(deque.removeFirst()); // 앞에서 빼서 뒤로 넣음


            }
            // K번째 사람 제거
            deque.removeFirst(); // 제거
        }

        // 마지막에 남은 사람을 반환
        return deque.peekFirst();  // 또는 deque.getFirst()
    }
}
