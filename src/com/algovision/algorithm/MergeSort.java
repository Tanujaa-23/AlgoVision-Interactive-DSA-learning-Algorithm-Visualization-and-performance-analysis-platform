package com.algovision.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Pure Java Implementation of Merge Sort with step recording engine.
 */
public class MergeSort {

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
        mergeSort(working, 0, working.length - 1, steps);
        steps.add(new AlgorithmStep(working, -1, -1, "complete", "Array fully sorted!", ++stepNum, comparisons, swaps));

        return steps;
    }

    private static void mergeSort(int[] arr, int left, int right, List<AlgorithmStep> steps) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            steps.add(new AlgorithmStep(arr, left, right, "pivot", "Dividing sub-array range [" + left + ".." + right + "] at mid index " + mid, ++stepNum, comparisons, swaps));

            mergeSort(arr, left, mid, steps);
            mergeSort(arr, mid + 1, right, steps);
            merge(arr, left, mid, right, steps);
        }
    }

    private static void merge(int[] arr, int left, int mid, int right, List<AlgorithmStep> steps) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            comparisons++;
            steps.add(new AlgorithmStep(arr, left + i, mid + 1 + j, "compare", 
                "Comparing left sub-array element (" + L[i] + ") with right sub-array element (" + R[j] + ")", 
                ++stepNum, comparisons, swaps));

            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            swaps++;
            steps.add(new AlgorithmStep(arr, k, -1, "overwrite", "Placed element " + arr[k] + " at index " + k, ++stepNum, comparisons, swaps));
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            swaps++;
            steps.add(new AlgorithmStep(arr, k, -1, "overwrite", "Copied remaining left element " + L[i] + " to index " + k, ++stepNum, comparisons, swaps));
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            swaps++;
            steps.add(new AlgorithmStep(arr, k, -1, "overwrite", "Copied remaining right element " + R[j] + " to index " + k, ++stepNum, comparisons, swaps));
            j++;
            k++;
        }
    }
}
