package com.algovision.algorithm;

import java.util.*;

public class Dijkstra {

    public static class Edge {
        public String target;
        public double weight;
        public Edge(String target, double weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    public static List<GraphStep> runWithSteps(Map<String, List<Edge>> graph, String startNode, String endNode) {
        List<GraphStep> steps = new ArrayList<>();
        if (graph == null || !graph.containsKey(startNode)) return steps;

        int stepNum = 0;
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (String node : graph.keySet()) {
            dist.put(node, Double.POSITIVE_INFINITY);
        }
        dist.put(startNode, 0.0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(startNode);

        GraphStep initStep = new GraphStep();
        initStep.setCurrentNode(startNode);
        initStep.setAction("start");
        initStep.setDescription("Initialized Dijkstra shortest path distances from source " + startNode);
        initStep.setStepNumber(++stepNum);
        steps.add(initStep);

        while (!pq.isEmpty()) {
            String curr = pq.poll();
            if (visited.contains(curr)) continue;
            visited.add(curr);

            GraphStep visitStep = new GraphStep();
            visitStep.setCurrentNode(curr);
            visitStep.setVisitedNodes(new ArrayList<>(visited));
            visitStep.setAction("visit");
            visitStep.setDescription("Extracted minimum distance node " + curr + " (dist = " + dist.get(curr) + ")");
            visitStep.setStepNumber(++stepNum);
            visitStep.setTotalDistance(dist.get(curr));
            steps.add(visitStep);

            if (endNode != null && curr.equals(endNode)) break;

            List<Edge> edges = graph.getOrDefault(curr, Collections.emptyList());
            for (Edge edge : edges) {
                if (!visited.contains(edge.target)) {
                    double newDist = dist.get(curr) + edge.weight;
                    if (newDist < dist.get(edge.target)) {
                        dist.put(edge.target, newDist);
                        prev.put(edge.target, curr);
                        pq.add(edge.target);

                        GraphStep relaxStep = new GraphStep();
                        relaxStep.setCurrentNode(curr);
                        relaxStep.setTargetNode(edge.target);
                        relaxStep.setCurrentEdge(curr + "-" + edge.target + " (wt " + edge.weight + ")");
                        relaxStep.setAction("relax");
                        relaxStep.setDescription("Relaxed edge to " + edge.target + ". Updated distance to " + newDist);
                        relaxStep.setStepNumber(++stepNum);
                        relaxStep.setTotalDistance(newDist);
                        steps.add(relaxStep);
                    }
                }
            }
        }

        // Reconstruct path if endNode specified
        List<String> path = new ArrayList<>();
        if (endNode != null && dist.get(endNode) != Double.POSITIVE_INFINITY) {
            String curr = endNode;
            while (curr != null) {
                path.add(0, curr);
                curr = prev.get(curr);
            }
        }

        GraphStep completeStep = new GraphStep();
        completeStep.setAction("complete");
        completeStep.setCurrentPath(path);
        completeStep.setTotalDistance(endNode != null ? dist.getOrDefault(endNode, 0.0) : 0.0);
        completeStep.setDescription("Dijkstra Algorithm Complete! Shortest path: " + (path.isEmpty() ? "All distances calculated" : String.join(" -> ", path)));
        completeStep.setStepNumber(++stepNum);
        steps.add(completeStep);

        return steps;
    }
}
