package com.algovision.algorithm;

import java.util.ArrayList;
import java.util.List;

public class LinearSearch {

    public static List<AlgorithmStep> searchWithSteps(int[] arr, int target) {
        List<AlgorithmStep> steps = new ArrayList<>();
        if (arr == null) return steps;

        int stepNum = 0;
        int comparisons = 0;

        steps.add(new AlgorithmStep(arr, -1, -1, "start", "Starting Linear Search for target: " + target, ++stepNum, comparisons, 0));

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            steps.add(new AlgorithmStep(arr, i, -1, "compare", "Checking index " + i + ": arr[" + i + "] (" + arr[i] + ") == target (" + target + ")?", ++stepNum, comparisons, 0));

            if (arr[i] == target) {
                steps.add(new AlgorithmStep(arr, i, -1, "found", "Target " + target + " FOUND at index " + i + "!", ++stepNum, comparisons, 0));
                return steps;
            }
        }

        steps.add(new AlgorithmStep(arr, -1, -1, "not_found", "Target " + target + " NOT FOUND in array.", ++stepNum, comparisons, 0));
        return steps;
    }
}
