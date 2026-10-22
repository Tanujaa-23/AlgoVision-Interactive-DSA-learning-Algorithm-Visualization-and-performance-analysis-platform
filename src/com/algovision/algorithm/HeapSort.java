package com.algovision.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Pure Java Implementation of Heap Sort with step recording engine.
 */
public class HeapSort {

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

        int n = working.length;

        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(working, n, i, steps);
        }

        steps.add(new AlgorithmStep(working, -1, -1, "pivot", "Max Heap constructed successfully!", ++stepNum, comparisons, swaps));

        // One by one extract elements from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            int temp = working[0];
            working[0] = working[i];
            working[i] = temp;
            swaps++;

            steps.add(new AlgorithmStep(working, 0, i, "swap", 
                "Extracted max element arr[0] (" + temp + ") to end at index " + i, 
                ++stepNum, comparisons, swaps));

            // call max heapify on reduced heap
            heapify(working, i, 0, steps);
        }

        steps.add(new AlgorithmStep(working, -1, -1, "complete", "Array fully sorted!", ++stepNum, comparisons, swaps));
        return steps;
    }

    private static void heapify(int[] arr, int n, int i, List<AlgorithmStep> steps) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n) {
            comparisons++;
            steps.add(new AlgorithmStep(arr, left, largest, "compare", "Comparing left child arr[" + left + "] (" + arr[left] + ") with parent arr[" + largest + "] (" + arr[largest] + ")", ++stepNum, comparisons, swaps));
            if (arr[left] > arr[largest]) {
                largest = left;
            }
        }

        if (right < n) {
            comparisons++;
            steps.add(new AlgorithmStep(arr, right, largest, "compare", "Comparing right child arr[" + right + "] (" + arr[right] + ") with current largest arr[" + largest + "] (" + arr[largest] + ")", ++stepNum, comparisons, swaps));
            if (arr[right] > arr[largest]) {
                largest = right;
            }
        }

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            swaps++;

            steps.add(new AlgorithmStep(arr, i, largest, "swap", 
                "Heapifying: Swapped arr[" + i + "] with largest child arr[" + largest + "]", 
                ++stepNum, comparisons, swaps));

            heapify(arr, n, largest, steps);
        }
    }
}
