package elevent_graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * 깊이 우선 탐색으로 모든 그래프의 노드를 순회하는 solution() 함수를 작성하세요.
 * 시작 노드는 start로 주어집니다. graph는 [출발 노드, 도착 노드] 쌍들이 들어있는 리스트 입니다.
 * 반환값은 그래프의 시작 노드부터 모든 노드를 깊이 우선 탐색으로 진행한 순서대로 노드가 저장된 리스트 입니다.
 *
 * 제약조건
 * 노드의 최대 개수는 100개를 넘지 않습니다.
 * 시작 노드부터 시작해서 모든 노드를 방문할 수 있는 경로가 항상 있습니다.
 * 그래프의 노드는 문자열 입니다.
 *
 *
 *                     graph                                      start      n              return
 * [['1','2'],['2','3'],['3','4'],['4','5']]                       '1'       5       ['1','2','3','4','5']
 * [['1','2'],['1','3'],['2','4'],['2','5'],['3','6'],['5',''6]]   '1'       6       ['1','2','4','5','6','3']
 */
public class DFS {
    // 인접 리스트를 저장할 ArrayList 배열
    private static ArrayList<Integer>[] adjList;

    // 방문 여부를 저장할 배열
    private static boolean[] visited;
    private static ArrayList<Integer> answer;


    public static void main(String[] args) {
        // int[][] graph = {{1, 2}, {2, 3}, {3, 4}, {4, 5}};
        int[][] graph = {
                {1, 2},
                {1, 3},
                {2, 4},
                {2, 5},
                {3, 6},
                {5, 6}
        };
        int start = 1;
        int n = 6;
        System.out.println(Arrays.toString(solution(graph, start, n)));
    }

    private static int[] solution(int[][] graph, int start, int n) {
        // 1. 인접 리스트 초기화
        adjList = new ArrayList[n + 1];
        for (int i = 0; i < adjList.length; i++) {
            adjList[i] = new ArrayList<>();
        }

        // 2. 그래프를 인접 리스트로 변환
        for (int[] edge : graph) {
            adjList[edge[0]].add(edge[1]);
        }

        visited = new boolean[n + 1];
        answer = new ArrayList<>();
        dfs(start);

        return answer.stream().mapToInt(Integer::intValue).toArray();
    }

    // DFS 탐색 메서드
    private static void dfs(int now) {
        visited[now] = true;  // 4. 현재 노드를 방문 했음을 저장
        answer.add(now); // 5. 현재 노드를 결과 리스트에 추가
        for (int next : adjList[now]) {
            if (!visited[next]) {
                dfs(next);
            }
        }
    }
}
