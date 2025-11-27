package _1_문자열;

import java.util.Scanner;

/*
설명
한 개의 문자열을 입력받고, 특정 문자를 입력받아 해당 특정문자가 입력받은 문자열에 몇 개 존재하는지 알아내는 프로그램을 작성하세요.
대소문자를 구분하지 않습니다.문자열의 길이는 100을 넘지 않습니다.

입력
첫 줄에 문자열이 주어지고, 두 번째 줄에 문자가 주어진다.
문자열은 영어 알파벳으로만 구성되어 있습니다.

출력
첫 줄에 해당 문자의 개수를 출력한다.


예시 입력 1
Computercooler
c

예시 출력 1
2
 */
public class _1_문자찾기 {
    public int solution(String str, char t) {
        int answer = 0;

        str = str.toUpperCase();
        t = Character.toUpperCase(t); // 클래스에 있는 메서드 사용
        // System.out.println(str + " " + t);

        /*
        for(int i=0; i<str.length(); i++) {
            if(str.charAt(i) == t)  answer++;
        }
         */

        // 향상된 for문 for each 사용
        for (char x : str.toCharArray()) { // * 문자배열을 순회
            if (x == t) answer++;
        }

        return answer;
    }

    public static void main(String[] args) {
        _1_문자찾기 T = new _1_문자찾기();
        Scanner kb = new Scanner(System.in);

        String str = kb.next(); // 문자열 1개를 읽음
        char c = kb.next().charAt(0); // 문자열 읽은 후 0번째 문자 가져옴

        System.out.print(T.solution(str, c));
    }
}
