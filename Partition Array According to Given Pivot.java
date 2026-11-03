class Solution {
    public int[] pivotArray(int[] nums, int pivot) {
        int n = nums.length;
        int[] result = new int[n];

        int index = 0;

        // Step 1: Add elements smaller than pivot
        for (int num : nums) {
            if (num < pivot) {
                result[index++] = num;
            }
        }

        // Step 2: Add elements equal to pivot
        for (int num : nums) {
            if (num == pivot) {
                result[index++] = num;
            }
        }

        // Step 3: Add elements greater than pivot
        for (int num : nums) {
            if (num > pivot) {
                result[index++] = num;
            }
        }

        return result;
    }
}
