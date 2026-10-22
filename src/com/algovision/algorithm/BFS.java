package com.algovision.algorithm;

import java.util.*;

public class BFS {

    public static List<GraphStep> runWithSteps(Map<String, List<String>> adjList, String startNode) {
        List<GraphStep> steps = new ArrayList<>();
        if (adjList == null || startNode == null || !adjList.containsKey(startNode)) return steps;

        int stepNum = 0;
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        visited.add(startNode);
        queue.add(startNode);

        GraphStep initStep = new GraphStep();
        initStep.setCurrentNode(startNode);
        initStep.setVisitedNodes(new ArrayList<>(visited));
        initStep.setQueueOrStack(new ArrayList<>(queue));
        initStep.setAction("start");
        initStep.setDescription("Started BFS at root node " + startNode);
        initStep.setStepNumber(++stepNum);
        steps.add(initStep);

        while (!queue.isEmpty()) {
            String curr = queue.poll();

            GraphStep popStep = new GraphStep();
            popStep.setCurrentNode(curr);
            popStep.setVisitedNodes(new ArrayList<>(visited));
            popStep.setQueueOrStack(new ArrayList<>(queue));
            popStep.setAction("visit");
            popStep.setDescription("Dequeued node " + curr + " from queue and visiting its adjacent neighbors.");
            popStep.setStepNumber(++stepNum);
            steps.add(popStep);

            List<String> neighbors = adjList.getOrDefault(curr, Collections.emptyList());
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);

                    GraphStep enqStep = new GraphStep();
                    enqStep.setCurrentNode(curr);
                    enqStep.setTargetNode(neighbor);
                    enqStep.setCurrentEdge(curr + "-" + neighbor);
                    enqStep.setVisitedNodes(new ArrayList<>(visited));
                    enqStep.setQueueOrStack(new ArrayList<>(queue));
                    enqStep.setAction("enqueue");
                    enqStep.setDescription("Enqueued unvisited neighbor " + neighbor + " into FIFO queue.");
                    enqStep.setStepNumber(++stepNum);
                    steps.add(enqStep);
                }
            }
        }

        GraphStep endStep = new GraphStep();
        endStep.setVisitedNodes(new ArrayList<>(visited));
        endStep.setAction("complete");
        endStep.setDescription("BFS Traversal Complete! Visited " + visited.size() + " vertices.");
        endStep.setStepNumber(++stepNum);
        steps.add(endStep);

        return steps;
    }
}
