package com.algovision.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Pure Java Implementation of Insertion Sort with step recording engine.
 */
public class InsertionSort {

    public static List<AlgorithmStep> sortWithSteps(int[] arr) {
        List<AlgorithmStep> steps = new ArrayList<>();
        if (arr == null || arr.length == 0) return steps;

        int[] working = arr.clone();
        int stepNum = 0;
        int comparisons = 0;
        int swaps = 0;

        steps.add(new AlgorithmStep(working, -1, -1, "start", "Initial array state", ++stepNum, comparisons, swaps));

        int n = working.length;
        for (int i = 1; i < n; i++) {
            int key = working[i];
            int j = i - 1;

            steps.add(new AlgorithmStep(working, i, -1, "pivot", "Selected key arr[" + i + "] = " + key + " for insertion", ++stepNum, comparisons, swaps));

            while (j >= 0) {
                comparisons++;
                steps.add(new AlgorithmStep(working, j, j + 1, "compare", 
                    "Comparing key (" + key + ") with arr[" + j + "] (" + working[j] + ")", 
                    ++stepNum, comparisons, swaps));

                if (working[j] > key) {
                    working[j + 1] = working[j];
                    swaps++;
                    steps.add(new AlgorithmStep(working, j, j + 1, "swap", 
                        "Shifted arr[" + j + "] (" + working[j] + ") right to index " + (j + 1), 
                        ++stepNum, comparisons, swaps));
                    j--;
                } else {
                    break;
                }
            }
            working[j + 1] = key;
            steps.add(new AlgorithmStep(working, j + 1, -1, "overwrite", "Inserted key " + key + " into position " + (j + 1), ++stepNum, comparisons, swaps));
        }

        steps.add(new AlgorithmStep(working, -1, -1, "complete", "Array fully sorted!", ++stepNum, comparisons, swaps));
        return steps;
    }
}
