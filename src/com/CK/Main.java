package com.CK;

public class Main {

    public static void main(String[] args) {
        int[] nums = {7,2,5,10,8};
        System.out.println(new Solution().splitArray(nums,3));
    }
}

class Solution {
    public int splitArray(int[] nums, int m) {
        if (nums.length == 0) return 0;
        int n = nums.length;
        int[][] sum = new int[nums.length][nums.length];
        int[][] visited = new int[n][m+1];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) {
                    sum[i][j] = nums[i];
                } else if (i == 0) {
                    sum[i][j] = sum[i][j - 1] + nums[j];
                } else if (j >= i) {
                    sum[i][j] = sum[i - 1][j] - nums[i - 1];
                }
            }
        }
        return subMatrixMax(sum, 0, m, n,visited);
    }

    private int subMatrixMax(int[][] sum, int start, int m, int n,int[][] visited) {
        if (m == 1)
            return sum[start][n - 1];

        if (visited[start][m] != 0) {
            return visited[start][m];
        }

        int minRes = Integer.MAX_VALUE;
        for (int i = start; i < n - m + 1; i++) {
            int tempRes = Math.max(sum[start][i], subMatrixMax(sum, i + 1, m - 1, n,visited));
            minRes = Math.min(minRes, tempRes);
        }

        visited[start][m] = minRes;
        return minRes;
    }
}

// DP
class Solution2 {
    public int splitArray(int[] nums, int m) {
        int n = nums.length;
        int[][] f = new int[n + 1][m + 1];
        int[] sub = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                f[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int i = 0; i < n; i++) {
            sub[i + 1] = sub[i] + nums[i];
        }
        f[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                for (int k = 0; k < i; k++) {
                    f[i][j] = Math.min(f[i][j], Math.max(f[k][j - 1], sub[i] - sub[k]));
                }
            }
        }
        return f[n][m];
    }
}

//Binary Search
class Solution3 {
    public int splitArray(int[] nums, int m) {
        int max = 0; long sum = 0;
        for (int num : nums) {
            max = Math.max(num, max);
            sum += num;
        }
        if (m == 1) return (int)sum;
        //binary search
        long l = max; long r = sum;
        while (l <= r) {
            long mid = (l + r)/ 2;
            if (valid(mid, nums, m)) {
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return (int)l;
    }
    public boolean valid(long target, int[] nums, int m) {
        int count = 1;
        long total = 0;
        for(int num : nums) {
            total += num;
            if (total > target) {
                total = num;
                count++;
                if (count > m) {
                    return false;
                }
            }
        }
        return true;
    }
}

//DFS + Memo
class Solution4 {
    public int splitArray(int[] nums, int m) {
        int n = nums.length;
        int[] presum = new int[n+1];
        presum[0] = 0;

        for (int i = 1; i <= n; i++) {
            presum[i] += nums[i-1] + presum[i-1];
        }

        int[][] visited = new int[n][m+1];
        return dfs(0, m, nums, presum, visited);
    }

    private int dfs(int start, int m, int[] nums, int[] presum, int[][] visited) {
        if (m == 1) {
            return presum[nums.length] - presum[start];
        }

        if (visited[start][m] != 0) {
            return visited[start][m];
        }

        int maxSum = Integer.MAX_VALUE;

        for (int i = start; i < nums.length-1; i++) {
            int l = presum[i+1] - presum[start];
            int rightIntervalMax = dfs(i+1, m-1, nums, presum, visited);
            maxSum = Math.min(maxSum, Math.max(l, rightIntervalMax));

        }

        visited[start][m] = maxSum;
        return maxSum;
    }
}