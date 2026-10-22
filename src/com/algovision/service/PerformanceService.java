package com.algovision.service;

import java.util.*;

public class PerformanceService {

    public static class MetricResult {
        private String algorithmName;
        private int inputSize;
        private double executionTimeMs;
        private long comparisons;
        private long swaps;

        public MetricResult(String algorithmName, int inputSize, double executionTimeMs, long comparisons, long swaps) {
            this.algorithmName = algorithmName;
            this.inputSize = inputSize;
            this.executionTimeMs = executionTimeMs;
            this.comparisons = comparisons;
            this.swaps = swaps;
        }

        public String getAlgorithmName() { return algorithmName; }
        public int getInputSize() { return inputSize; }
        public double getExecutionTimeMs() { return executionTimeMs; }
        public long getComparisons() { return comparisons; }
        public long getSwaps() { return swaps; }
    }

    public List<MetricResult> compareSortingAlgorithms(int[] sizes) {
        List<MetricResult> results = new ArrayList<>();
        Random rand = new Random(42);

        for (int size : sizes) {
            int[] baseArray = new int[size];
            for (int i = 0; i < size; i++) {
                baseArray[i] = rand.nextInt(size * 10);
            }

            // Bubble Sort
            if (size <= 5000) {
                results.add(benchmarkBubbleSort(baseArray.clone()));
            }

            // Selection Sort
            if (size <= 5000) {
                results.add(benchmarkSelectionSort(baseArray.clone()));
            }

            // Insertion Sort
            if (size <= 5000) {
                results.add(benchmarkInsertionSort(baseArray.clone()));
            }

            // Merge Sort
            results.add(benchmarkMergeSort(baseArray.clone()));

            // Quick Sort
            results.add(benchmarkQuickSort(baseArray.clone()));

            // Heap Sort
            results.add(benchmarkHeapSort(baseArray.clone()));
        }

        return results;
    }

    private MetricResult benchmarkBubbleSort(int[] arr) {
        long comparisons = 0;
        long swaps = 0;
        long start = System.nanoTime();
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swaps++;
                }
            }
        }
        long end = System.nanoTime();
        double elapsedMs = (end - start) / 1e6;
        return new MetricResult("Bubble Sort", n, elapsedMs, comparisons, swaps);
    }

    private MetricResult benchmarkSelectionSort(int[] arr) {
        long comparisons = 0;
        long swaps = 0;
        long start = System.nanoTime();
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                comparisons++;
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                int temp = arr[i];
                arr[i] = arr[minIdx];
                arr[minIdx] = temp;
                swaps++;
            }
        }
        long end = System.nanoTime();
        double elapsedMs = (end - start) / 1e6;
        return new MetricResult("Selection Sort", n, elapsedMs, comparisons, swaps);
    }

    private MetricResult benchmarkInsertionSort(int[] arr) {
        long comparisons = 0;
        long swaps = 0;
        long start = System.nanoTime();
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0) {
                comparisons++;
                if (arr[j] > key) {
                    arr[j + 1] = arr[j];
                    swaps++;
                    j--;
                } else {
                    break;
                }
            }
            arr[j + 1] = key;
        }
        long end = System.nanoTime();
        double elapsedMs = (end - start) / 1e6;
        return new MetricResult("Insertion Sort", n, elapsedMs, comparisons, swaps);
    }

    private MetricResult benchmarkMergeSort(int[] arr) {
        long[] stats = new long[2];
        long start = System.nanoTime();
        mergeSortHelper(arr, 0, arr.length - 1, stats);
        long end = System.nanoTime();
        double elapsedMs = (end - start) / 1e6;
        return new MetricResult("Merge Sort", arr.length, elapsedMs, stats[0], stats[1]);
    }

    private void mergeSortHelper(int[] arr, int l, int r, long[] stats) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSortHelper(arr, l, m, stats);
            mergeSortHelper(arr, m + 1, r, stats);
            merge(arr, l, m, r, stats);
        }
    }

    private void merge(int[] arr, int l, int m, int r, long[] stats) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int[] L = new int[n1];
        int[] R = new int[n2];
        for (int i = 0; i < n1; i++) L[i] = arr[l + i];
        for (int j = 0; j < n2; j++) R[j] = arr[m + 1 + j];
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            stats[0]++;
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
            } else {
                arr[k] = R[j];
                j++;
            }
            stats[1]++;
            k++;
        }
        while (i < n1) {
            arr[k] = L[i];
            i++; k++; stats[1]++;
        }
        while (j < n2) {
            arr[k] = R[j];
            j++; k++; stats[1]++;
        }
    }

    private MetricResult benchmarkQuickSort(int[] arr) {
        long[] stats = new long[2];
        long start = System.nanoTime();
        quickSortHelper(arr, 0, arr.length - 1, stats);
        long end = System.nanoTime();
        double elapsedMs = (end - start) / 1e6;
        return new MetricResult("Quick Sort", arr.length, elapsedMs, stats[0], stats[1]);
    }

    private void quickSortHelper(int[] arr, int low, int high, long[] stats) {
        if (low < high) {
            int pi = partition(arr, low, high, stats);
            quickSortHelper(arr, low, pi - 1, stats);
            quickSortHelper(arr, pi + 1, high, stats);
        }
    }

    private int partition(int[] arr, int low, int high, long[] stats) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            stats[0]++;
            if (arr[j] < pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                stats[1]++;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        stats[1]++;
        return i + 1;
    }

    private MetricResult benchmarkHeapSort(int[] arr) {
        long[] stats = new long[2];
        long start = System.nanoTime();
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--)
            heapify(arr, n, i, stats);
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            stats[1]++;
            heapify(arr, i, 0, stats);
        }
        long end = System.nanoTime();
        double elapsedMs = (end - start) / 1e6;
        return new MetricResult("Heap Sort", n, elapsedMs, stats[0], stats[1]);
    }

    private void heapify(int[] arr, int n, int i, long[] stats) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n) {
            stats[0]++;
            if (arr[l] > arr[largest]) largest = l;
        }
        if (r < n) {
            stats[0]++;
            if (arr[r] > arr[largest]) largest = r;
        }
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            stats[1]++;
            heapify(arr, n, largest, stats);
        }
    }
}
