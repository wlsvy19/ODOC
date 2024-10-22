package nine_tree;

import java.util.Arrays;

/**
 * 이진 트리를 표현한 리스트 nodes를 인자로 받습니다.
 * 예를들어 nodes가 [1,2,3,4,5,6,7] 이면 다음과 같은 트리를 표현한 것입니다.
 *                        1
 *                   2         3
 *                 4   5     6   7
 * 해당 이진 트리에 대하여 전위, 중위, 후위 순회 결과를 반환하는 solution 함수를 구현하세요
 *
 * 제약조건
 * 입력 노드값의 개수는 1개 이상 1000개 이하이다.
 * 노드값은 정수형이며 중복되지 않는다.
 *
 * 입출력예
 * nodes                                          return
 * [1,2,3,4,5,6,7]              ["1 2 4 5 3 6 7" , "4 2 5 1 6 3 7" , "4 5 2 6 7 3 1"]
 */
public class TreeTraversal_01 {
    public static void main(String[] args) {
        int[] nodes = {1, 2, 3, 4, 5, 6, 7};
        System.out.println(Arrays.toString(solution(nodes)));
    }

    public static String[] solution(int[] nodes) {
        String[] result = new String[3];
        result[0] = preorder(nodes, 0).trim();
        result[1] = inorder(nodes, 0).trim();
        result[2] = postorder(nodes, 0).trim();
        return result;
    }

    public static String preorder(int[] nodes, int idx) {
        if (idx >= nodes.length) { // idx 가 범위를 벗어나면 빈 문자열 반환
            return "";
        }

        // 루트 -> 왼쪽 -> 오른쪽 재귀 호출하여 결과를 이어 붙임
        return nodes[idx] + " " + preorder(nodes, 2 * idx + 1) + preorder(nodes, 2 * idx + 2);
    }

    public static String inorder(int[] nodes, int idx) {
        if (idx >= nodes.length) { // idx 가 범위를 벗어나면 빈 문자열 반환
            return "";
        }
        // 왼쪽 -> 루트 -> 오른쪽 재귀 호출하여 결과를 이어 붙임
        return inorder(nodes, 2 * idx + 1) + nodes[idx] + " " + inorder(nodes, 2 * idx + 2);
    }

    public static String postorder(int[] nodes, int idx) {
        if (idx >= nodes.length) {
            return "";
        }
        return postorder(nodes, 2 * idx + 1) + postorder(nodes, 2 * idx + 2) + nodes[idx] + " ";
    }

}
