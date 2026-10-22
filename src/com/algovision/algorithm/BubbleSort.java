package com.algovision.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Pure Java Implementation of Bubble Sort with step recording engine.
 */
public class BubbleSort {

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
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                steps.add(new AlgorithmStep(working, j, j + 1, "compare", 
                    "Comparing arr[" + j + "] (" + working[j] + ") and arr[" + (j + 1) + "] (" + working[j + 1] + ")", 
                    ++stepNum, comparisons, swaps));

                if (working[j] > working[j + 1]) {
                    // Swap working[j] and working[j+1]
                    int temp = working[j];
                    working[j] = working[j + 1];
                    working[j + 1] = temp;
                    swaps++;
                    swapped = true;

                    steps.add(new AlgorithmStep(working, j, j + 1, "swap", 
                        "Swapped arr[" + j + "] (" + working[j + 1] + ") and arr[" + (j + 1) + "] (" + working[j] + ")", 
                        ++stepNum, comparisons, swaps));
                }
            }
            if (!swapped) break;
        }

        steps.add(new AlgorithmStep(working, -1, -1, "complete", "Array fully sorted!", ++stepNum, comparisons, swaps));
        return steps;
    }
}
