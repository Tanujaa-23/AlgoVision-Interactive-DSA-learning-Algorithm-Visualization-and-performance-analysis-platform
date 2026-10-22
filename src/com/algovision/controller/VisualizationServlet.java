package com.algovision.controller;

import com.algovision.algorithm.*;
import com.algovision.dao.ProgressDAO;
import com.algovision.model.User;
import com.algovision.service.AlgorithmService;
import com.algovision.util.ValidationUtil;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class VisualizationServlet extends HttpServlet {

    private final AlgorithmService algorithmService = new AlgorithmService();
    private final ProgressDAO progressDAO = new ProgressDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        String algo = request.getParameter("algo");

        if (algo != null) algo = algo.trim();
        if (type != null) type = type.trim();

        if (type == null || type.isEmpty()) {
            if (algo != null && !algo.isEmpty()) {
                type = inferTypeFromAlgorithm(algo);
            } else {
                type = "sorting";
            }
        }

        if (algo == null || algo.isEmpty()) {
            if ("graph".equalsIgnoreCase(type)) {
                algo = "BFS";
            } else if ("searching".equalsIgnoreCase(type)) {
                algo = "Linear Search";
            } else {
                algo = "Bubble Sort";
            }
        }

        request.setAttribute("selectedAlgo", algo);
        request.setAttribute("type", type.toLowerCase());

        if ("graph".equalsIgnoreCase(type)) {
            request.getRequestDispatcher("/visualization/graphVisualizer.jsp").forward(request, response);
        } else if ("searching".equalsIgnoreCase(type)) {
            request.getRequestDispatcher("/visualization/searchingVisualizer.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/visualization/sortingVisualizer.jsp").forward(request, response);
        }
    }

    private String inferTypeFromAlgorithm(String algo) {
        if (algo == null) return "sorting";
        String lower = algo.trim().toLowerCase();

        // Check Graph algorithms first (avoids "Breadth-First Search" being caught by "search")
        if (lower.contains("bfs") || lower.contains("dfs")
                || lower.contains("dijkstra") || lower.contains("prim")
                || lower.contains("kruskal") || lower.contains("breadth-first")
                || lower.contains("depth-first") || lower.contains("shortest path")
                || lower.contains("spanning tree") || lower.contains("mst")
                || lower.contains("graph")) {
            return "graph";
        }

        // Check Searching algorithms
        if (lower.contains("linear") || lower.contains("binary")
                || lower.contains("jump") || lower.contains("search")) {
            return "searching";
        }

        return "sorting";
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;
        int userId = 2; // Default to Student (user 2)
        if (user != null) {
            userId = user.getId();
        } else if (session != null && session.getAttribute("userId") != null) {
            userId = (Integer) session.getAttribute("userId");
        }

        String algoName = request.getParameter("algorithm");
        String inputStr = request.getParameter("inputData");
        String category = request.getParameter("category");
        String startNode = request.getParameter("startNode");
        if (startNode == null || startNode.trim().isEmpty()) {
            startNode = "A";
        } else {
            startNode = startNode.trim().toUpperCase();
        }

        if (algoName == null) algoName = "Bubble Sort";
        algoName = algoName.trim();

        if (category == null || category.trim().isEmpty()) {
            category = inferTypeFromAlgorithm(algoName);
        }

        if ("Sorting".equalsIgnoreCase(category)) {
            int[] arr;
            try {
                arr = ValidationUtil.parseIntegerArray(inputStr != null ? inputStr : "5, 2, 8, 1, 3");
            } catch (Exception e) {
                arr = new int[]{5, 2, 8, 1, 3};
            }
            List<AlgorithmStep> steps = algorithmService.executeSortingAlgorithm(algoName, arr, userId);
            out.print(gson.toJson(steps));

        } else if ("Searching".equalsIgnoreCase(category)) {
            int[] arr;
            int target = 8;
            try {
                String targetStr = request.getParameter("target");
                if (targetStr != null && !targetStr.trim().isEmpty()) {
                    target = Integer.parseInt(targetStr.trim());
                }
                arr = ValidationUtil.parseIntegerArray(inputStr != null ? inputStr : "1, 2, 5, 8, 12, 16");
            } catch (Exception e) {
                arr = new int[]{1, 2, 5, 8, 12, 16};
            }
            List<AlgorithmStep> steps = algorithmService.executeSearchingAlgorithm(algoName, arr, target, userId);
            out.print(gson.toJson(steps));

        } else if ("Graph".equalsIgnoreCase(category)) {
            long startTime = System.nanoTime();
            Map<String, List<String>> adjList = new HashMap<>();
            adjList.put("A", Arrays.asList("B", "C"));
            adjList.put("B", Arrays.asList("A", "C", "D"));
            adjList.put("C", Arrays.asList("A", "B", "D"));
            adjList.put("D", Arrays.asList("B", "C"));

            List<GraphStep> steps;
            String lowerAlgo = algoName.toLowerCase();
            if (lowerAlgo.contains("dfs") || lowerAlgo.contains("depth")) {
                steps = DFS.runWithSteps(adjList, startNode);
            } else if (lowerAlgo.contains("dijkstra") || lowerAlgo.contains("shortest")) {
                Map<String, List<Dijkstra.Edge>> graph = new HashMap<>();
                graph.put("A", Arrays.asList(new Dijkstra.Edge("B", 4), new Dijkstra.Edge("C", 2)));
                graph.put("B", Arrays.asList(new Dijkstra.Edge("A", 4), new Dijkstra.Edge("C", 1), new Dijkstra.Edge("D", 5)));
                graph.put("C", Arrays.asList(new Dijkstra.Edge("A", 2), new Dijkstra.Edge("B", 1), new Dijkstra.Edge("D", 8)));
                graph.put("D", Arrays.asList(new Dijkstra.Edge("B", 5), new Dijkstra.Edge("C", 8)));
                String endNode = "D".equals(startNode) ? "A" : "D";
                steps = Dijkstra.runWithSteps(graph, startNode, endNode);
            } else if (lowerAlgo.contains("prim")) {
                Map<String, List<Prim.Edge>> primGraph = new HashMap<>();
                primGraph.put("A", Arrays.asList(new Prim.Edge("A", "B", 4), new Prim.Edge("A", "C", 2)));
                primGraph.put("B", Arrays.asList(new Prim.Edge("B", "A", 4), new Prim.Edge("B", "C", 1), new Prim.Edge("B", "D", 5)));
                primGraph.put("C", Arrays.asList(new Prim.Edge("C", "A", 2), new Prim.Edge("C", "B", 1), new Prim.Edge("C", "D", 8)));
                primGraph.put("D", Arrays.asList(new Prim.Edge("D", "B", 5), new Prim.Edge("D", "C", 8)));
                steps = Prim.runWithSteps(primGraph, startNode);
            } else if (lowerAlgo.contains("kruskal")) {
                Set<String> nodes = new HashSet<>(Arrays.asList("A", "B", "C", "D"));
                List<Kruskal.Edge> edges = Arrays.asList(
                    new Kruskal.Edge("A", "B", 4),
                    new Kruskal.Edge("A", "C", 2),
                    new Kruskal.Edge("B", "C", 1),
                    new Kruskal.Edge("B", "D", 5),
                    new Kruskal.Edge("C", "D", 8)
                );
                steps = Kruskal.runWithSteps(nodes, edges);
            } else {
                steps = BFS.runWithSteps(adjList, startNode);
            }

            long endTime = System.nanoTime();
            double elapsedMs = (endTime - startTime) / 1e6;

            if (steps != null && !steps.isEmpty()) {
                com.algovision.model.Execution exec = new com.algovision.model.Execution();
                exec.setUserId(userId);
                exec.setAlgorithmName(algoName);
                exec.setInputData("Start: " + startNode + ", Graph: A-B(4), A-C(2), B-C(1), B-D(5), C-D(8)");
                exec.setStepCount(steps.size());
                exec.setComparisonCount(steps.size());
                exec.setSwapCount(0);
                exec.setExecutionTimeMs(elapsedMs);

                com.algovision.dao.ExecutionDAO executionDAO = new com.algovision.dao.ExecutionDAO();
                executionDAO.recordExecution(exec);
            }

            // Increment & refresh user progress accurately for Graph algorithms
            progressDAO.recordAlgorithmProgress(userId, "Graph", algoName);

            out.print(gson.toJson(steps));
        } else {
            out.print("[]");
        }
        out.flush();
    }
}
