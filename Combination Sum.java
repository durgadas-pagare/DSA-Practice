import java.util.*;

class Solution {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();

        backtrack(candidates, target, 0, new ArrayList<>(), result);

        return result;
    }

    private void backtrack(int[] candidates, int remaining, int start,
                           List<Integer> current, List<List<Integer>> result) {

        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] > remaining) {
                continue;
            }

            current.add(candidates[i]);

            // Reuse the same number by passing i again
            backtrack(candidates, remaining - candidates[i], i, current, result);

            // Backtrack: remove the last number
            current.remove(current.size() - 1);
        }
    }
}
