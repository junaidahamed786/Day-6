class Solution {
    int ans = Integer.MAX_VALUE;
    int subsetSize;

    public int minimumIncompatibility(int[] nums, int k) {
        int n = nums.length;
        if (k == n) return 0;

        // Early exit: Pigeonhole principle for duplicates
        int[] count = new int[n + 1];
        for (int x : nums) {
            count[x]++;
            if (count[x] > k) return -1;
        }

        // Sorting is critical for the running sum optimization
        Arrays.sort(nums);
        subsetSize = n / k;

        // State trackers for k sets
        int[] size = new int[k];
        int[] min = new int[k];
        int[] max = new int[k];
        boolean[][] hasVal = new boolean[k][n + 1]; 

        backtrack(nums, 0, size, min, max, hasVal, 0);

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }

    private void backtrack(int[] nums, int i, int[] size, int[] min, int[] max, boolean[][] hasVal, int currentIncomp) {
        // Base Case: All elements successfully placed
        if (i == nums.length) {
            ans = Math.min(ans, currentIncomp);
            return;
        }

        //  If the running incompatibility is already worse than our best so far, stop.
        if (currentIncomp >= ans) return;

        int val = nums[i];

        // buckets
        for (int j = 0; j < size.length; j++) {
            //  Don't overfill buckets, don't allow duplicate values in the same bucket
            if (size[j] == subsetSize || hasVal[j][val]) continue;

            // Identical empty buckets. 
            // If the previous bucket is empty, placing the first element in *this* bucket 
            // is effectively the exact same state. Skip to avoid redundant permutations.
            if (j > 0 && size[j] == 0 && size[j - 1] == 0) continue;

            // Backup state for backtracking
            int oldMin = min[j];
            int oldMax = max[j];

            int newIncomp = currentIncomp;
            
            // Calculate new incompatibility
            if (size[j] == 0) {
                min[j] = val;
                max[j] = val;
                // A single element has 0 incompatibility, so newIncomp doesn't change
            } else {
                // Since `nums` is sorted ascending, `val` is strictly >= `oldMax`.
                // The bucket's incompatibility increases by the difference between the new max and old max.
                newIncomp += (val - oldMax);
                max[j] = val;
            }

            // Apply changes
            size[j]++;
            hasVal[j][val] = true;

            // Recurse
            backtrack(nums, i + 1, size, min, max, hasVal, newIncomp);

            // Backtrack (undo changes)
            size[j]--;
            hasVal[j][val] = false;
            min[j] = oldMin;
            max[j] = oldMax;

            // If we just started a brand new bucket with this 'val',
            // there's no point in trying to start a different brand new bucket 
            // with the same 'val' in this loop.
            if (size[j] == 0) break;
        }
    }
}