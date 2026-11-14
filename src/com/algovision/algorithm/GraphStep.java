package com.algovision.algorithm;

import java.util.List;

/**
 * Execution step payload emitted by Graph algorithms (BFS, DFS, Dijkstra, Kruskal).
 */
public class GraphStep {

    private String currentNode;
    private String targetNode;
    private String currentEdge;
    private List<String> visitedNodes;
    private List<String> queueOrStack;
    private List<String> currentPath;
    private String action; // "visit", "enqueue", "pop", "relax", "mst_add", "cycle_skip"
    private String description;
    private int stepNumber;
    private double totalDistance = 0.0;

    public GraphStep() {}

    public String getCurrentNode() { return currentNode; }
    public void setCurrentNode(String currentNode) { this.currentNode = currentNode; }

    public String getTargetNode() { return targetNode; }
    public void setTargetNode(String targetNode) { this.targetNode = targetNode; }

    public String getCurrentEdge() { return currentEdge; }
    public void setCurrentEdge(String currentEdge) { this.currentEdge = currentEdge; }

    public List<String> getVisitedNodes() { return visitedNodes; }
    public void setVisitedNodes(List<String> visitedNodes) { this.visitedNodes = visitedNodes; }

    public List<String> getQueueOrStack() { return queueOrStack; }
    public void setQueueOrStack(List<String> queueOrStack) { this.queueOrStack = queueOrStack; }

    public List<String> getCurrentPath() { return currentPath; }
    public void setCurrentPath(List<String> currentPath) { this.currentPath = currentPath; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getStepNumber() { return stepNumber; }
    public void setStepNumber(int stepNumber) { this.stepNumber = stepNumber; }

    public double getTotalDistance() { return totalDistance; }
    public void setTotalDistance(double totalDistance) { this.totalDistance = totalDistance; }
}
