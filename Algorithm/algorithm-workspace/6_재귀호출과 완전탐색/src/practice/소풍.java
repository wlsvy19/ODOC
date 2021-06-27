package practice;

public class 소풍 {
    int n;
    boolean areFriends[][];

    public static void main(String[] args) {
        소풍 pic = new 소풍();
        System.out.println(pic.countPairings(null));
    }

    public int countPairings(boolean taken[]) {
        // taken[i] = i 번째 학생이 짝을 이미 찾았으면 true, 아니면 false
        int firstFree = -1;

        for (int i = 0; i < n; i++) {
            if (!taken[i]) {
                firstFree = i;
                break;
            }
        }

        // 기저사례: 모든 학생이 짝을 찾았으면 한 가지 방법을 찾았으니 종료한다.
        if (firstFree == -1) {
            return 1;
        }

        int ret = 0;
        for (int pairWith = firstFree + 1; pairWith < n; ++pairWith) {
            if (!taken[pairWith] && areFriends[firstFree][pairWith]) {
                taken[firstFree] = taken[pairWith] = true;
                ret += countPairings(taken);
                taken[firstFree] = taken[pairWith] = false;
            }
        }

        return ret;
    }
}
