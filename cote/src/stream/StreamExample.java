package stream;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class StreamExample {
    public static void main(String[] args) {
        // Queue<Integer>를 int[]로 변환하는 예제
        Queue<Integer> queue = new LinkedList<>(Arrays.asList(2, 1, 3));
        int[] result = queue.stream().mapToInt(Integer::intValue).toArray();
        System.out.println("Queue to int[]: " + Arrays.toString(result));

        // List<Integer>를 int[]로 변환하는 예제
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        int[] listToArray = list.stream().mapToInt(Integer::intValue).toArray();
        System.out.println("List to int[]: " + Arrays.toString(listToArray));

        // 문자열 스트림을 대문자로 변환하는 예제
        List<String> words = Arrays.asList("hello", "world");
        List<String> upperCase = words.stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("Uppercase strings: " + upperCase);

        // 숫자 필터링 예제
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int[] evenNumbers = Arrays.stream(numbers)
                .filter(n -> n % 2 == 0)
                .toArray();
        System.out.println("Even numbers: " + Arrays.toString(evenNumbers));
    }
}
