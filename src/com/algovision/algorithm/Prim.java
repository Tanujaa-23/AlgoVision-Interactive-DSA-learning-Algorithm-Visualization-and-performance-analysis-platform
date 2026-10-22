package com.algovision.algorithm;

import java.util.*;

public class Prim {

    public static class Edge {
        public String u;
        public String v;
        public double weight;
        public Edge(String u, String v, double weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    }

    public static List<GraphStep> runWithSteps(Map<String, List<Edge>> graph, String startNode) {
        List<GraphStep> steps = new ArrayList<>();
        if (graph == null || !graph.containsKey(startNode)) return steps;

        int stepNum = 0;
        Set<String> visited = new HashSet<>();
        List<Edge> mstEdges = new ArrayList<>();
        double totalMSTWeight = 0.0;

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));
        visited.add(startNode);

        for (Edge e : graph.getOrDefault(startNode, Collections.emptyList())) {
            pq.add(e);
        }

        GraphStep initStep = new GraphStep();
        initStep.setCurrentNode(startNode);
        initStep.setVisitedNodes(new ArrayList<>(visited));
        initStep.setAction("start");
        initStep.setDescription("Started Prim Minimum Spanning Tree from vertex " + startNode);
        initStep.setStepNumber(++stepNum);
        steps.add(initStep);

        while (!pq.isEmpty() && visited.size() < graph.size()) {
            Edge edge = pq.poll();
            if (visited.contains(edge.u) && visited.contains(edge.v)) continue;

            String nextNode = visited.contains(edge.u) ? edge.v : edge.u;
            visited.add(nextNode);
            mstEdges.add(edge);
            totalMSTWeight += edge.weight;

            GraphStep addStep = new GraphStep();
            addStep.setCurrentNode(nextNode);
            addStep.setCurrentEdge(edge.u + "-" + edge.v + " (wt " + edge.weight + ")");
            addStep.setVisitedNodes(new ArrayList<>(visited));
            addStep.setAction("mst_add");
            addStep.setDescription("Added minimum cut edge " + edge.u + "-" + edge.v + " (wt " + edge.weight + ") to MST.");
            addStep.setTotalDistance(totalMSTWeight);
            addStep.setStepNumber(++stepNum);
            steps.add(addStep);

            for (Edge nextEdge : graph.getOrDefault(nextNode, Collections.emptyList())) {
                if (!visited.contains(nextEdge.v)) {
                    pq.add(nextEdge);
                }
            }
        }

        GraphStep endStep = new GraphStep();
        endStep.setAction("complete");
        endStep.setVisitedNodes(new ArrayList<>(visited));
        endStep.setTotalDistance(totalMSTWeight);
        endStep.setDescription("Prim MST Construction Complete! Total MST Weight: " + totalMSTWeight);
        endStep.setStepNumber(++stepNum);
        steps.add(endStep);

        return steps;
    }
}
