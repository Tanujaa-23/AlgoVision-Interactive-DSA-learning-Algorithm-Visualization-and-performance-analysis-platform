package com.algovision.algorithm;

import java.util.ArrayList;
import java.util.List;

public class BinarySearch {

    public static List<AlgorithmStep> searchWithSteps(int[] arr, int target) {
        List<AlgorithmStep> steps = new ArrayList<>();
        if (arr == null) return steps;

        int stepNum = 0;
        int comparisons = 0;

        steps.add(new AlgorithmStep(arr, -1, -1, "start", "Starting Binary Search for target: " + target + " (Array must be sorted)", ++stepNum, comparisons, 0));

        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            comparisons++;

            AlgorithmStep step = new AlgorithmStep(arr, low, high, "pivot", "Current search window [" + low + ".." + high + "], calculating mid index " + mid + " (" + arr[mid] + ")", ++stepNum, comparisons, 0);
            step.setPivotIndex(mid);
            steps.add(step);

            if (arr[mid] == target) {
                AlgorithmStep foundStep = new AlgorithmStep(arr, mid, -1, "found", "Target " + target + " FOUND at index " + mid + "!", ++stepNum, comparisons, 0);
                foundStep.setPivotIndex(mid);
                steps.add(foundStep);
                return steps;
            } else if (arr[mid] < target) {
                steps.add(new AlgorithmStep(arr, mid + 1, high, "compare", "Target " + target + " is larger than mid (" + arr[mid] + "). Searching right half.", ++stepNum, comparisons, 0));
                low = mid + 1;
            } else {
                steps.add(new AlgorithmStep(arr, low, mid - 1, "compare", "Target " + target + " is smaller than mid (" + arr[mid] + "). Searching left half.", ++stepNum, comparisons, 0));
                high = mid - 1;
            }
        }

        steps.add(new AlgorithmStep(arr, -1, -1, "not_found", "Target " + target + " NOT FOUND in array.", ++stepNum, comparisons, 0));
        return steps;
    }
}
