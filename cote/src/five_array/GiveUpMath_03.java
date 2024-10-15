package five_array;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Step 3
 * 문제: 수포자의 답안 찍기
 */
public class GiveUpMath_03 {
    public static void main(String[] args) {
        int[] answers = { 1,3,2,4,2 };

        System.out.println(Arrays.toString(solution(answers)));
    }

    private static int[] solution(int[] answers) {
        int[] person1 = { 1, 2, 3, 4, 5 };
        int[] person2 = { 2, 1, 2, 3, 2, 4, 2, 5 };
        int[] person3 = { 3, 3, 1, 1, 2, 2, 4, 4, 5, 5 };

        int[] scores = new int[3];

        // 각 수포자의 점수 계산
        for (int i = 0; i < answers.length; i++) {
            if (answers[i] == person1[i % person1.length])
                scores[0]++;
            if (answers[i] == person2[i % person2.length])
                scores[1]++;
            if (answers[i] == person3[i % person3.length])
                scores[2]++;
        }

        // 최고 점수 찾기
        int maxScore = Math.max(scores[0], Math.max(scores[1], scores[2]));

        // 최고 점수를 받은 사람들 리스트 생성
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (scores[i] == maxScore) {
                list.add(i + 1);
            }
        }

        // 결과 배열 생성
        int[] result = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }

        return result;

    }
}
