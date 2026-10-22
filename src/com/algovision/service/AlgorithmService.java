package com.algovision.service;

import com.algovision.algorithm.*;
import com.algovision.dao.AlgorithmDAO;
import com.algovision.dao.ExecutionDAO;
import com.algovision.dao.ProgressDAO;
import com.algovision.model.Algorithm;
import com.algovision.model.Execution;

import java.util.*;

public class AlgorithmService {

    private final AlgorithmDAO algorithmDAO = new AlgorithmDAO();
    private final ExecutionDAO executionDAO = new ExecutionDAO();
    private final ProgressDAO progressDAO = new ProgressDAO();

    public List<Algorithm> getAllAlgorithms() {
        return algorithmDAO.getAllAlgorithms();
    }

    public List<Algorithm> getAlgorithmsByCategory(String cat) {
        return algorithmDAO.getAlgorithmsByCategory(cat);
    }

    public Algorithm getAlgorithmByName(String name) {
        return algorithmDAO.getAlgorithmByName(name);
    }

    public List<AlgorithmStep> executeSortingAlgorithm(String algoName, int[] inputArr, int userId) {
        long start = System.nanoTime();
        List<AlgorithmStep> steps;

        switch (algoName.trim().toLowerCase()) {
            case "selection sort":
                steps = SelectionSort.sortWithSteps(inputArr);
                break;
            case "insertion sort":
                steps = InsertionSort.sortWithSteps(inputArr);
                break;
            case "merge sort":
                steps = MergeSort.sortWithSteps(inputArr);
                break;
            case "quick sort":
                steps = QuickSort.sortWithSteps(inputArr);
                break;
            case "heap sort":
                steps = HeapSort.sortWithSteps(inputArr);
                break;
            case "bubble sort":
            default:
                steps = BubbleSort.sortWithSteps(inputArr);
                break;
        }

        long end = System.nanoTime();
        double elapsedMs = (end - start) / 1e6;

        if (!steps.isEmpty()) {
            AlgorithmStep finalStep = steps.get(steps.size() - 1);

            Execution exec = new Execution();
            exec.setUserId(userId);
            exec.setAlgorithmName(algoName);
            exec.setInputData(Arrays.toString(inputArr));
            exec.setStepCount(steps.size());
            exec.setComparisonCount(finalStep.getComparisonCount());
            exec.setSwapCount(finalStep.getSwapCount());
            exec.setExecutionTimeMs(elapsedMs);

            executionDAO.recordExecution(exec);
            
            // Automatically refresh & increment user progress count for Sorting
            progressDAO.recordAlgorithmProgress(userId, "Sorting", algoName);
        }

        return steps;
    }

    public List<AlgorithmStep> executeSearchingAlgorithm(String algoName, int[] inputArr, int target, int userId) {
        long start = System.nanoTime();
        List<AlgorithmStep> steps;

        switch (algoName.trim().toLowerCase()) {
            case "binary search":
                Arrays.sort(inputArr);
                steps = BinarySearch.searchWithSteps(inputArr, target);
                break;
            case "jump search":
                Arrays.sort(inputArr);
                steps = JumpSearch.searchWithSteps(inputArr, target);
                break;
            case "linear search":
            default:
                steps = LinearSearch.searchWithSteps(inputArr, target);
                break;
        }

        long end = System.nanoTime();
        double elapsedMs = (end - start) / 1e6;

        if (!steps.isEmpty()) {
            AlgorithmStep finalStep = steps.get(steps.size() - 1);
            Execution exec = new Execution();
            exec.setUserId(userId);
            exec.setAlgorithmName(algoName);
            exec.setInputData("Target: " + target + ", Array: " + Arrays.toString(inputArr));
            exec.setStepCount(steps.size());
            exec.setComparisonCount(finalStep.getComparisonCount());
            exec.setSwapCount(0);
            exec.setExecutionTimeMs(elapsedMs);
            
            executionDAO.recordExecution(exec);
            
            // Automatically refresh & increment user progress count for Searching
            progressDAO.recordAlgorithmProgress(userId, "Searching", algoName);
        }

        return steps;
    }
}
