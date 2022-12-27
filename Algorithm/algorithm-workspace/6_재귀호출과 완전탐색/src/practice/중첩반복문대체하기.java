package practice;

public class 중첩반복문대체하기 {
    public static void main(String[] args) {
        int total = 0;
        /* 7개의 원소중 4개를 고르는 코드 */
        for (int i = 0; i < 7; i++) {
            for (int j = i + 1; j < 7; j++) {
                for (int k = j + 1; k < 7; k++) {
                    for (int l = k + 1; l < 7; l++) {
                        System.out.print(i + ", " + j + ", " + k + ", " + l);
                        System.out.println();
                        total++;
                    }
                }
            }
        }

        System.out.println(total);

        int[] dx = { -1, -1, -1, 1, 1, 1, 0, 0 };
        int[] dy = { -1, 0, 1, -1, 0, 1, -1, 1 };
    } // end main()
} // end class
