package com.algovision.model;

import java.sql.Timestamp;

public class Execution {
    private int id;
    private int userId;
    private int algorithmId;
    private String algorithmName;
    private String inputData;
    private int stepCount;
    private int comparisonCount;
    private int swapCount;
    private double executionTimeMs;
    private Timestamp executedAt;

    public Execution() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getAlgorithmId() { return algorithmId; }
    public void setAlgorithmId(int algorithmId) { this.algorithmId = algorithmId; }

    public String getAlgorithmName() { return algorithmName; }
    public void setAlgorithmName(String algorithmName) { this.algorithmName = algorithmName; }

    public String getInputData() { return inputData; }
    public void setInputData(String inputData) { this.inputData = inputData; }

    public int getStepCount() { return stepCount; }
    public void setStepCount(int stepCount) { this.stepCount = stepCount; }

    public int getComparisonCount() { return comparisonCount; }
    public void setComparisonCount(int comparisonCount) { this.comparisonCount = comparisonCount; }

    public int getSwapCount() { return swapCount; }
    public void setSwapCount(int swapCount) { this.swapCount = swapCount; }

    public double getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(double executionTimeMs) { this.executionTimeMs = executionTimeMs; }

    public Timestamp getExecutedAt() { return executedAt; }
    public void setExecutedAt(Timestamp executedAt) { this.executedAt = executedAt; }

    public String getCategory() {
        if (algorithmName == null) return "Sorting";
        String lower = algorithmName.toLowerCase();
        if (lower.contains("bfs") || lower.contains("dfs") || lower.contains("dijkstra")
                || lower.contains("kruskal") || lower.contains("breadth")
                || lower.contains("depth") || lower.contains("shortest") || lower.contains("mst")
                || lower.contains("graph")) {
            return "Graph";
        }
        if (lower.contains("linear") || lower.contains("binary")
                || lower.contains("search")) {
            return "Searching";
        }
        return "Sorting";
    }

    public String getType() {
        return getCategory().toLowerCase();
    }
}
