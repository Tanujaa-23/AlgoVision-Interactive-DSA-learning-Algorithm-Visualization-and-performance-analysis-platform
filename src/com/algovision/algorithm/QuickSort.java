package com.algovision.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Pure Java Implementation of Quick Sort with step recording engine.
 */
public class QuickSort {

    private static int stepNum = 0;
    private static int comparisons = 0;
    private static int swaps = 0;

    public static List<AlgorithmStep> sortWithSteps(int[] arr) {
        List<AlgorithmStep> steps = new ArrayList<>();
        if (arr == null || arr.length == 0) return steps;

        int[] working = arr.clone();
        stepNum = 0;
        comparisons = 0;
        swaps = 0;

        steps.add(new AlgorithmStep(working, -1, -1, "start", "Initial array state", ++stepNum, comparisons, swaps));
        quickSort(working, 0, working.length - 1, steps);
        steps.add(new AlgorithmStep(working, -1, -1, "complete", "Array fully sorted!", ++stepNum, comparisons, swaps));

        return steps;
    }

    private static void quickSort(int[] arr, int low, int high, List<AlgorithmStep> steps) {
        if (low < high) {
            int pi = partition(arr, low, high, steps);
            quickSort(arr, low, pi - 1, steps);
            quickSort(arr, pi + 1, high, steps);
        }
    }

    private static int partition(int[] arr, int low, int high, List<AlgorithmStep> steps) {
        int pivot = arr[high];
        AlgorithmStep pStep = new AlgorithmStep(arr, high, -1, "pivot", "Selected pivot arr[" + high + "] = " + pivot, ++stepNum, comparisons, swaps);
        pStep.setPivotIndex(high);
        steps.add(pStep);

        int i = low - 1;
        for (int j = low; j < high; j++) {
            comparisons++;
            AlgorithmStep cStep = new AlgorithmStep(arr, j, high, "compare", 
                "Comparing element arr[" + j + "] (" + arr[j] + ") with pivot (" + pivot + ")", 
                ++stepNum, comparisons, swaps);
            cStep.setPivotIndex(high);
            steps.add(cStep);

            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                swaps++;
                AlgorithmStep sStep = new AlgorithmStep(arr, i, j, "swap", 
                    "Swapped arr[" + i + "] and arr[" + j + "] (smaller than pivot)", 
                    ++stepNum, comparisons, swaps);
                sStep.setPivotIndex(high);
                steps.add(sStep);
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        swaps++;
        AlgorithmStep sStep = new AlgorithmStep(arr, i + 1, high, "swap", 
            "Placed pivot " + pivot + " into correct sorted position at index " + (i + 1), 
            ++stepNum, comparisons, swaps);
        sStep.setPivotIndex(i + 1);
        steps.add(sStep);

        return i + 1;
    }
}
