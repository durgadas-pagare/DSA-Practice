
import java.util.*;

class Solution {

    class Fenwick {
        int n;
        long[] count;
        long[] sum;

        Fenwick(int n) {
            this.n = n;
            count = new long[n + 1];
            sum = new long[n + 1];
        }

        void add(int i, long value) {
            for (; i <= n; i += i & -i) {
                count[i]++;
                sum[i] += value;
            }
        }

        long queryCount(int i) {
            long result = 0;
            for (; i > 0; i -= i & -i) {
                result += count[i];
            }
            return result;
        }

        long querySum(int i) {
            long result = 0;
            for (; i > 0; i -= i & -i) {
                result += sum[i];
            }
            return result;
        }

        int findKth(long k) {
            int index = 0;

            for (int bit = Integer.highestOneBit(n);
                 bit > 0; bit >>= 1) {

                int next = index + bit;

                if (next <= n && count[next] < k) {
                    index = next;
                    k -= count[next];
                }
            }

            return index + 1;
        }
    }

    public long minOperations(int[] nums, int x, int k) {

        int n = nums.length;

        // Coordinate compression
        int[] sorted = nums.clone();
        Arrays.sort(sorted);

        int m = 0;

        for (int value : sorted) {
            if (m == 0 || sorted[m - 1] != value) {
                sorted[m++] = value;
            }
        }

        Fenwick bit = new Fenwick(m);

        // Calculate the cost of every window
        int windows = n - x + 1;
        long[] cost = new long[windows];

        for (int i = 0; i < x; i++) {
            int rank = Arrays.binarySearch(
                sorted, 0, m, nums[i]
            ) + 1;

            bit.add(rank, nums[i]);
        }

        for (int left = 0; left < windows; left++) {

            // Find median
            int medianRank = bit.findKth((x + 1L) / 2);
            long median = sorted[medianRank - 1];

            // Calculate sum of elements <= median
            long leftCount = bit.queryCount(medianRank);
            long leftSum = bit.querySum(medianRank);

            // Calculate total sum and count
            long totalSum = bit.querySum(m);
            long totalCount = x;

            long rightCount = totalCount - leftCount;
            long rightSum = totalSum - leftSum;

            // Minimum operations for this window
            cost[left] =
                median * leftCount - leftSum
                + rightSum - median * rightCount;

            // Slide the window
            if (left + x < n) {

                int removeRank = Arrays.binarySearch(
                    sorted, 0, m, nums[left]
                ) + 1;

                int addRank = Arrays.binarySearch(
                    sorted, 0, m, nums[left + x]
                ) + 1;

                // Remove outgoing element
                bit.addRemove(removeRank, nums[left]);

                // Add incoming element
                bit.add(addRank, nums[left + x]);
            }
        }

        // Dynamic Programming
        long INF = Long.MAX_VALUE / 4;

        long[][] dp = new long[n + 1][k + 1];

        for (int i = 0; i <= n; i++) {
            Arrays.fill(dp[i], INF);
            dp[i][0] = 0;
        }

        for (int j = 1; j <= k; j++) {

            for (int i = x; i <= n; i++) {

                // Do not select a window ending at i - 1
                dp[i][j] = dp[i - 1][j];

                // Select the window [i - x ... i - 1]
                if (dp[i - x][j - 1] != INF) {

                    dp[i][j] = Math.min(
                        dp[i][j],
                        dp[i - x][j - 1] + cost[i - x]
                    );
                }
            }
        }

        return dp[n][k];
    }
}
