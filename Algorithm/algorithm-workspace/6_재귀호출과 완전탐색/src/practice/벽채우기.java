package practice;

import java.util.Scanner;

public class 벽채우기 {
    // NxN 보드
    static int N;

    // 최대 100x100
    static int[][] BOARD = new int[100][100];

    public static void main(String[] args) {
        Scanner sc = null;
        try {
            sc = new Scanner(System.in);
            System.out.print("N사각형 입력: ");
            N = sc.nextInt();

            System.out.println("사각형 채우기");
            int count = 0;
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < N; ++j) {// 5입력했으면 총 25번 입력 할거야
                    count++;
                    if (count < 5) {
                        BOARD[i][j] = sc.nextInt(); // 0또는 1을 좌표에 입력
                        break;
                    }
                }
            }

            int scRow = 0;
            int scColumn = 0;

            // 위치 설정 ex) 11 이면 (1,1) 에서 시
            scRow = sc.nextInt();
            scColumn = sc.nextInt();

            // fill메소드를 호출하여 인자로 입력한 값을 넘김
            fill(scRow, scColumn);

            // 5
            // 00000
            // 00011
            // 00010
            // 11110
            // 00000
            // 11 시작점

            // 출력부분
            for (int i = 0; i < N; ++i) {
                for (int j = 0; j < N; ++j) {
                    System.out.print(BOARD[i][j] + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            if (sc != null) {
                try {
                    sc.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    static void fill(int row, int column) {
        // 행과 열이 0보다 작거나, 행과 열이 경계를 뚫어버린 경우
        if (row < 0 || row > N - 1 || column < 0 || column > N - 1) {
            return;
        }

        // 보드의 숫자가 1인경
        if (BOARD[row][column] != 0) {
            return;
        }
        /* 위까지 base case */

        // 보드를 1로 채움
        BOARD[row][column] = 1;

        /* 재귀호출 부분 */

        // 위
        fill(row + 1, column);

        // 아래
        fill(row - 1, column);

        // 왼쪽
        fill(row, column - 1);

        // 오른쪽
        fill(row, column + 1);

    }

}
