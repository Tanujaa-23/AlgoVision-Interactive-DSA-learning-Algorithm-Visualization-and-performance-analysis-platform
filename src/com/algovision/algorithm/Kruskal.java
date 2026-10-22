package com.algovision.algorithm;

import java.util.*;

public class Kruskal {

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

    private static class DisjointSet {
        private final Map<String, String> parent = new HashMap<>();

        public void makeSet(Collection<String> nodes) {
            for (String node : nodes) {
                parent.put(node, node);
            }
        }

        public String find(String i) {
            if (parent.get(i).equals(i)) return i;
            String root = find(parent.get(i));
            parent.put(i, root);
            return root;
        }

        public boolean union(String i, String j) {
            String rootI = find(i);
            String rootJ = find(j);
            if (!rootI.equals(rootJ)) {
                parent.put(rootI, rootJ);
                return true;
            }
            return false;
        }
    }

    public static List<GraphStep> runWithSteps(Set<String> nodes, List<Edge> edges) {
        List<GraphStep> steps = new ArrayList<>();
        if (nodes == null || edges == null) return steps;

        int stepNum = 0;
        List<Edge> sortedEdges = new ArrayList<>(edges);
        sortedEdges.sort(Comparator.comparingDouble(e -> e.weight));

        DisjointSet ds = new DisjointSet();
        ds.makeSet(nodes);

        GraphStep initStep = new GraphStep();
        initStep.setAction("start");
        initStep.setDescription("Sorted all " + edges.size() + " graph edges in ascending order of weight.");
        initStep.setStepNumber(++stepNum);
        steps.add(initStep);

        double totalWeight = 0.0;
        Set<String> visitedNodes = new HashSet<>();

        for (Edge edge : sortedEdges) {
            if (ds.union(edge.u, edge.v)) {
                totalWeight += edge.weight;
                visitedNodes.add(edge.u);
                visitedNodes.add(edge.v);

                GraphStep addStep = new GraphStep();
                addStep.setCurrentEdge(edge.u + "-" + edge.v + " (wt " + edge.weight + ")");
                addStep.setVisitedNodes(new ArrayList<>(visitedNodes));
                addStep.setAction("mst_add");
                addStep.setDescription("Added edge " + edge.u + "-" + edge.v + " (wt " + edge.weight + ") to MST (No cycle formed).");
                addStep.setTotalDistance(totalWeight);
                addStep.setStepNumber(++stepNum);
                steps.add(addStep);
            } else {
                GraphStep skipStep = new GraphStep();
                skipStep.setCurrentEdge(edge.u + "-" + edge.v + " (wt " + edge.weight + ")");
                skipStep.setAction("cycle_skip");
                skipStep.setDescription("Skipped edge " + edge.u + "-" + edge.v + " (wt " + edge.weight + ") because it forms a cycle.");
                skipStep.setStepNumber(++stepNum);
                steps.add(skipStep);
            }
        }

        GraphStep completeStep = new GraphStep();
        completeStep.setAction("complete");
        completeStep.setVisitedNodes(new ArrayList<>(visitedNodes));
        completeStep.setTotalDistance(totalWeight);
        completeStep.setDescription("Kruskal MST Construction Complete! Total MST Weight: " + totalWeight);
        completeStep.setStepNumber(++stepNum);
        steps.add(completeStep);

        return steps;
    }
}
