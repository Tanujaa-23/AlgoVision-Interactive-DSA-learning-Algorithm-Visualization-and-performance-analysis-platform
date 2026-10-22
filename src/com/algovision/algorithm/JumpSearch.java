package com.algovision.algorithm;

import java.util.ArrayList;
import java.util.List;

public class JumpSearch {

    public static List<AlgorithmStep> searchWithSteps(int[] arr, int target) {
        List<AlgorithmStep> steps = new ArrayList<>();
        if (arr == null || arr.length == 0) return steps;

        int stepNum = 0;
        int comparisons = 0;
        int n = arr.length;
        int step = (int) Math.floor(Math.sqrt(n));
        int prev = 0;

        steps.add(new AlgorithmStep(arr, -1, -1, "start", "Starting Jump Search for target " + target + " with block size m = √" + n + " = " + step, ++stepNum, comparisons, 0));

        while (arr[Math.min(step, n) - 1] < target) {
            comparisons++;
            int currentCheck = Math.min(step, n) - 1;
            steps.add(new AlgorithmStep(arr, prev, currentCheck, "pivot", "Jumping: arr[" + currentCheck + "] (" + arr[currentCheck] + ") < target (" + target + "). Jumping forward.", ++stepNum, comparisons, 0));

            prev = step;
            step += (int) Math.floor(Math.sqrt(n));
            if (prev >= n) {
                steps.add(new AlgorithmStep(arr, -1, -1, "not_found", "Target " + target + " NOT FOUND in array.", ++stepNum, comparisons, 0));
                return steps;
            }
        }

        steps.add(new AlgorithmStep(arr, prev, Math.min(step, n) - 1, "compare", "Found block containing target! Performing linear search backwards from index " + prev, ++stepNum, comparisons, 0));

        while (arr[prev] < target) {
            comparisons++;
            steps.add(new AlgorithmStep(arr, prev, -1, "compare", "Linear step: arr[" + prev + "] (" + arr[prev] + ") < target (" + target + ")", ++stepNum, comparisons, 0));
            prev++;
            if (prev == Math.min(step, n)) {
                steps.add(new AlgorithmStep(arr, -1, -1, "not_found", "Target " + target + " NOT FOUND in array.", ++stepNum, comparisons, 0));
                return steps;
            }
        }

        comparisons++;
        if (arr[prev] == target) {
            steps.add(new AlgorithmStep(arr, prev, -1, "found", "Target " + target + " FOUND at index " + prev + "!", ++stepNum, comparisons, 0));
            return steps;
        }

        steps.add(new AlgorithmStep(arr, -1, -1, "not_found", "Target " + target + " NOT FOUND in array.", ++stepNum, comparisons, 0));
        return steps;
    }
}
