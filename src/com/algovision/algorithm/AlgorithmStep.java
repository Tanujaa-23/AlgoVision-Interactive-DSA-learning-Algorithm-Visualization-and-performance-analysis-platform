package com.algovision.algorithm;

import java.util.Arrays;

/**
 * Reusable execution step class emitted by Java backend DSA algorithms for frontend visualizer.
 */
public class AlgorithmStep {

    private int[] array;
    private int index1 = -1;
    private int index2 = -1;
    private int pivotIndex = -1;
    private int[] highlightedIndices;
    private String action; // "compare", "swap", "overwrite", "pivot", "found", "not_found", "visit", "relax"
    private String description;
    private int stepNumber;
    private int comparisonCount;
    private int swapCount;

    public AlgorithmStep() {}

    public AlgorithmStep(int[] currentArray, int index1, int index2, String action, String description, int stepNumber, int comparisonCount, int swapCount) {
        this.array = currentArray != null ? Arrays.copyOf(currentArray, currentArray.length) : new int[0];
        this.index1 = index1;
        this.index2 = index2;
        this.action = action;
        this.description = description;
        this.stepNumber = stepNumber;
        this.comparisonCount = comparisonCount;
        this.swapCount = swapCount;
    }

    public int[] getArray() { return array; }
    public void setArray(int[] array) { this.array = array != null ? Arrays.copyOf(array, array.length) : new int[0]; }

    public int getIndex1() { return index1; }
    public void setIndex1(int index1) { this.index1 = index1; }

    public int getIndex2() { return index2; }
    public void setIndex2(int index2) { this.index2 = index2; }

    public int getPivotIndex() { return pivotIndex; }
    public void setPivotIndex(int pivotIndex) { this.pivotIndex = pivotIndex; }

    public int[] getHighlightedIndices() { return highlightedIndices; }
    public void setHighlightedIndices(int[] highlightedIndices) { this.highlightedIndices = highlightedIndices; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getStepNumber() { return stepNumber; }
    public void setStepNumber(int stepNumber) { this.stepNumber = stepNumber; }

    public int getComparisonCount() { return comparisonCount; }
    public void setComparisonCount(int comparisonCount) { this.comparisonCount = comparisonCount; }

    public int getSwapCount() { return swapCount; }
    public void setSwapCount(int swapCount) { this.swapCount = swapCount; }
}
