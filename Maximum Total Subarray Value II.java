import java.util.PriorityQueue;

class Solution {

    static class SparseTable {
        int[][] maxTable;
        int[][] minTable;
        int[] log;
        int n;

        SparseTable(int[] nums) {
            n = nums.length;

            int levels = 32 - Integer.numberOfLeadingZeros(n);
            maxTable = new int[n][levels];
            minTable = new int[n][levels];
            log = new int[n + 1];

            for (int i = 2; i <= n; i++) {
                log[i] = log[i / 2] + 1;
            }

            for (int i = 0; i < n; i++) {
                maxTable[i][0] = nums[i];
                minTable[i][0] = nums[i];
            }

            for (int j = 1; j < levels; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    maxTable[i][j] = Math.max(
                        maxTable[i][j - 1],
                        maxTable[i + (1 << (j - 1))][j - 1]
                    );

                    minTable[i][j] = Math.min(
                        minTable[i][j - 1],
                        minTable[i + (1 << (j - 1))][j - 1]
                    );
                }
            }
        }

        int rangeValue(int left, int right) {
            int j = log[right - left + 1];

            int maximum = Math.max(
                maxTable[left][j],
                maxTable[right - (1 << j) + 1][j]
            );

            int minimum = Math.min(
                minTable[left][j],
                minTable[right - (1 << j) + 1][j]
            );

            return maximum - minimum;
        }
    }

    public long maxTotalValue(int[] nums, int k) {
        int n = nums.length;
        SparseTable st = new SparseTable(nums);

        // Each entry contains {value, left, right}.
        PriorityQueue<int[]> pq = new PriorityQueue<>(
            (a, b) -> Integer.compare(b[0], a[0])
        );

        // Start with the largest subarray for each left endpoint.
        for (int left = 0; left < n; left++) {
            int value = st.rangeValue(left, n - 1);
            pq.offer(new int[]{value, left, n - 1});
        }

        long answer = 0;

        for (int count = 0; count < k; count++) {
            int[] current = pq.poll();

            int value = current[0];
            int left = current[1];
            int right = current[2];

            answer += value;

            // Generate the next smaller subarray
            // with the same left endpoint.
            if (right > left) {
                int nextValue = st.rangeValue(left, right - 1);

                pq.offer(new int[]{
                    nextValue, left, right - 1
                });
            }
        }

        return answer;
    }
}
