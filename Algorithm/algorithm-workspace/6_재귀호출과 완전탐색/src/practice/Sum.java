package practice;

public class Sum {
    public static void main(String[] args) {
        Sum sum = new Sum();

        System.out.println(sum.sum(10));
        System.out.println(sum.recursiveSum(1000));
    }

    public int sum(int n) {
        int ret = 0;
        for (int i = 0; i <= n; i++) {
            ret += i;
        }

        return ret;
    }

    /* 재귀호출 */
    public int recursiveSum(int n) {
        if (n == 1) {
            return 1;
        }

        return n + recursiveSum(n - 1);
    }
}
