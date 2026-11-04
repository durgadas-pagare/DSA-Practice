
import java.util.*;

class Solution {
    public List<Integer> findDisappearedNumbers(int[] nums) {

        int n = nums.length;
        boolean[] seen = new boolean[n + 1];

        // Mark numbers that are present
        for (int num : nums) {
            seen[num] = true;
        }

        // Find missing numbers
        List<Integer> result = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            if (!seen[i]) {
                result.add(i);
            }
        }

        return result;
    }
}
