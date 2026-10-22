package com.algovision.algorithm;

import java.util.*;

public class DFS {

    public static List<GraphStep> runWithSteps(Map<String, List<String>> adjList, String startNode) {
        List<GraphStep> steps = new ArrayList<>();
        if (adjList == null || startNode == null || !adjList.containsKey(startNode)) return steps;

        int stepNum = 0;
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        stack.push(startNode);

        GraphStep initStep = new GraphStep();
        initStep.setCurrentNode(startNode);
        initStep.setVisitedNodes(new ArrayList<>(visited));
        initStep.setQueueOrStack(new ArrayList<>(stack));
        initStep.setAction("start");
        initStep.setDescription("Pushed start node " + startNode + " onto DFS call stack.");
        initStep.setStepNumber(++stepNum);
        steps.add(initStep);

        while (!stack.isEmpty()) {
            String curr = stack.pop();

            if (!visited.contains(curr)) {
                visited.add(curr);

                GraphStep visitStep = new GraphStep();
                visitStep.setCurrentNode(curr);
                visitStep.setVisitedNodes(new ArrayList<>(visited));
                visitStep.setQueueOrStack(new ArrayList<>(stack));
                visitStep.setAction("visit");
                visitStep.setDescription("Popped node " + curr + " from stack and marked as visited.");
                visitStep.setStepNumber(++stepNum);
                steps.add(visitStep);

                List<String> neighbors = adjList.getOrDefault(curr, Collections.emptyList());
                // Push neighbors in reverse order to explore in natural order
                for (int i = neighbors.size() - 1; i >= 0; i--) {
                    String neighbor = neighbors.get(i);
                    if (!visited.contains(neighbor)) {
                        stack.push(neighbor);

                        GraphStep pushStep = new GraphStep();
                        pushStep.setCurrentNode(curr);
                        pushStep.setTargetNode(neighbor);
                        pushStep.setCurrentEdge(curr + "-" + neighbor);
                        pushStep.setVisitedNodes(new ArrayList<>(visited));
                        pushStep.setQueueOrStack(new ArrayList<>(stack));
                        pushStep.setAction("enqueue");
                        pushStep.setDescription("Pushed unvisited neighbor " + neighbor + " onto stack.");
                        pushStep.setStepNumber(++stepNum);
                        steps.add(pushStep);
                    }
                }
            }
        }

        GraphStep endStep = new GraphStep();
        endStep.setVisitedNodes(new ArrayList<>(visited));
        endStep.setAction("complete");
        endStep.setDescription("DFS Traversal Complete! Visited " + visited.size() + " vertices.");
        endStep.setStepNumber(++stepNum);
        steps.add(endStep);

        return steps;
    }
}
