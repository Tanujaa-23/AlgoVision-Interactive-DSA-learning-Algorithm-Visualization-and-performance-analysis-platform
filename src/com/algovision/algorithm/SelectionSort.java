package com.algovision.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Pure Java Implementation of Selection Sort with step recording engine.
 */
public class SelectionSort {

    public static List<AlgorithmStep> sortWithSteps(int[] arr) {
        List<AlgorithmStep> steps = new ArrayList<>();
        if (arr == null || arr.length == 0) return steps;

        int[] working = arr.clone();
        int stepNum = 0;
        int comparisons = 0;
        int swaps = 0;

        steps.add(new AlgorithmStep(working, -1, -1, "start", "Initial array state", ++stepNum, comparisons, swaps));

        int n = working.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            steps.add(new AlgorithmStep(working, i, minIdx, "pivot", "Assuming minimum element at index " + i + " (" + working[i] + ")", ++stepNum, comparisons, swaps));

            for (int j = i + 1; j < n; j++) {
                comparisons++;
                steps.add(new AlgorithmStep(working, j, minIdx, "compare", 
                    "Comparing arr[" + j + "] (" + working[j] + ") with current min arr[" + minIdx + "] (" + working[minIdx] + ")", 
                    ++stepNum, comparisons, swaps));

                if (working[j] < working[minIdx]) {
                    minIdx = j;
                    steps.add(new AlgorithmStep(working, i, minIdx, "pivot", "New minimum found at index " + minIdx + " (" + working[minIdx] + ")", ++stepNum, comparisons, swaps));
                }
            }

            if (minIdx != i) {
                int temp = working[minIdx];
                working[minIdx] = working[i];
                working[i] = temp;
                swaps++;
                steps.add(new AlgorithmStep(working, i, minIdx, "swap", 
                    "Swapping arr[" + i + "] with new minimum at index " + minIdx, 
                    ++stepNum, comparisons, swaps));
            }
        }

        steps.add(new AlgorithmStep(working, -1, -1, "complete", "Array fully sorted!", ++stepNum, comparisons, swaps));
        return steps;
    }
}
