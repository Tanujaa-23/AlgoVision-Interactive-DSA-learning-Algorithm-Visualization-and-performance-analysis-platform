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
                || lower.contains("dijkstra")
                || lower.contains("kruskal") || lower.contains("breadth-first")
                || lower.contains("depth-first") || lower.contains("shortest path")
                || lower.contains("spanning tree") || lower.contains("mst")
                || lower.contains("graph")) {
            return "graph";
        }

        // Check Searching algorithms
        if (lower.contains("linear") || lower.contains("binary")
                || lower.contains("search")) {
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
            if (user.getId() <= 0) {
                User dbUser = new com.algovision.dao.UserDAO().getUserByEmail(user.getEmail());
                if (dbUser != null && dbUser.getId() > 0) {
                    user.setId(dbUser.getId());
                    session.setAttribute("loggedUser", user);
                    session.setAttribute("userId", user.getId());
                }
            }
            if (user.getId() > 0) {
                userId = user.getId();
            }
        } else if (session != null && session.getAttribute("userId") != null) {
            int sessId = (Integer) session.getAttribute("userId");
            if (sessId > 0) {
                userId = sessId;
            }
        }

        String action = request.getParameter("action");
        String recordParam = request.getParameter("record");
        boolean shouldRecord = "true".equalsIgnoreCase(recordParam);

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

        if ("record".equalsIgnoreCase(action)) {
            String stepCountStr = request.getParameter("stepCount");
            int stepCount = 10;
            try { if (stepCountStr != null) stepCount = Integer.parseInt(stepCountStr.trim()); } catch (Exception ignored) {}

            String compStr = request.getParameter("comparisons");
            int comparisons = 0;
            try { if (compStr != null) comparisons = Integer.parseInt(compStr.trim()); } catch (Exception ignored) {}

            String swapStr = request.getParameter("swaps");
            int swaps = 0;
            try { if (swapStr != null) swaps = Integer.parseInt(swapStr.trim()); } catch (Exception ignored) {}

            String timeStr = request.getParameter("executionTimeMs");
            double timeMs = 1.0;
            try { if (timeStr != null) timeMs = Double.parseDouble(timeStr.trim()); } catch (Exception ignored) {}

            com.algovision.model.Execution exec = new com.algovision.model.Execution();
            exec.setUserId(userId);
            exec.setAlgorithmName(algoName);
            exec.setInputData(inputStr != null ? inputStr : "");
            exec.setStepCount(stepCount > 0 ? stepCount : 1);
            exec.setComparisonCount(comparisons);
            exec.setSwapCount(swaps);
            exec.setExecutionTimeMs(timeMs);

            com.algovision.dao.ExecutionDAO executionDAO = new com.algovision.dao.ExecutionDAO();
            executionDAO.recordExecution(exec);
            progressDAO.recordAlgorithmProgress(userId, category, algoName);

            out.print("{\"status\":\"recorded\",\"userId\":" + userId + "}");
            out.flush();
            return;
        }

        if ("Sorting".equalsIgnoreCase(category)) {
            int[] arr;
            try {
                arr = ValidationUtil.parseIntegerArray(inputStr != null ? inputStr : "5, 2, 8, 1, 3, 9, 4, 7");
            } catch (Exception e) {
                arr = new int[]{5, 2, 8, 1, 3, 9, 4, 7};
            }
            List<AlgorithmStep> steps = algorithmService.executeSortingAlgorithm(algoName, arr, userId, shouldRecord);
            out.print(gson.toJson(steps));

        } else if ("Searching".equalsIgnoreCase(category)) {
            int[] arr;
            int target = 5;
            try {
                String targetStr = request.getParameter("target");
                if (targetStr != null && !targetStr.trim().isEmpty()) {
                    target = Integer.parseInt(targetStr.trim());
                }
                arr = ValidationUtil.parseIntegerArray(inputStr != null ? inputStr : "1, 2, 3, 4, 5, 6, 7, 8, 9");
            } catch (Exception e) {
                arr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
            }
            List<AlgorithmStep> steps = algorithmService.executeSearchingAlgorithm(algoName, arr, target, userId, shouldRecord);
            out.print(gson.toJson(steps));

        } else if ("Graph".equalsIgnoreCase(category)) {
            long startTime = System.nanoTime();

            // Parse custom graph nodes / numbers from inputStr (e.g. "1, 2, 3, 4, 5, 6, 7, 8, 9" or any custom numbers/nodes)
            List<String> nodeList = new ArrayList<>();
            if (inputStr != null && !inputStr.trim().isEmpty()) {
                String[] parts = inputStr.trim().split("[,\\s]+");
                for (String p : parts) {
                    String clean = p.trim().toUpperCase();
                    if (!clean.isEmpty() && !nodeList.contains(clean)) {
                        nodeList.add(clean);
                    }
                }
            }
            if (nodeList.isEmpty()) {
                nodeList = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9");
            }

            if (startNode == null || !nodeList.contains(startNode)) {
                startNode = nodeList.get(0);
            }

            int N = nodeList.size();
            Map<String, List<String>> adjList = new LinkedHashMap<>();
            Map<String, List<Dijkstra.Edge>> dijkstraGraph = new LinkedHashMap<>();
            Set<String> nodes = new LinkedHashSet<>(nodeList);
            List<Kruskal.Edge> edges = new ArrayList<>();

            for (String node : nodeList) {
                adjList.put(node, new ArrayList<>());
                dijkstraGraph.put(node, new ArrayList<>());
            }

            // Create connections (ring + cross chords) to form a rich, connected graph
            Set<String> addedEdges = new HashSet<>();
            for (int i = 0; i < N; i++) {
                String u = nodeList.get(i);
                int[] neighborIndices;
                if (N == 1) {
                    neighborIndices = new int[]{};
                } else if (N == 2) {
                    neighborIndices = new int[]{(i + 1) % N};
                } else if (N <= 4) {
                    neighborIndices = new int[]{(i + 1) % N, (i + 2) % N};
                } else {
                    neighborIndices = new int[]{(i + 1) % N, (i + 2) % N, (i + N / 2) % N};
                }

                for (int ni : neighborIndices) {
                    if (ni == i) continue;
                    String v = nodeList.get(ni);
                    String edgeKey1 = u + "-" + v;
                    String edgeKey2 = v + "-" + u;
                    if (!addedEdges.contains(edgeKey1) && !addedEdges.contains(edgeKey2)) {
                        addedEdges.add(edgeKey1);
                        double weight = ((i * 3 + ni * 5) % 9) + 2.0;
                        if (weight < 1.0) weight = 2.0;

                        adjList.get(u).add(v);
                        adjList.get(v).add(u);

                        dijkstraGraph.get(u).add(new Dijkstra.Edge(v, weight));
                        dijkstraGraph.get(v).add(new Dijkstra.Edge(u, weight));

                        edges.add(new Kruskal.Edge(u, v, weight));
                    }
                }
            }

            List<GraphStep> steps;
            String lowerAlgo = algoName.toLowerCase();
            if (lowerAlgo.contains("dfs") || lowerAlgo.contains("depth")) {
                steps = DFS.runWithSteps(adjList, startNode);
            } else if (lowerAlgo.contains("dijkstra") || lowerAlgo.contains("shortest")) {
                String endNode = nodeList.get(nodeList.size() - 1);
                if (startNode.equals(endNode) && N > 1) {
                    endNode = nodeList.get((nodeList.indexOf(startNode) + 1) % N);
                }
                steps = Dijkstra.runWithSteps(dijkstraGraph, startNode, endNode);
            } else if (lowerAlgo.contains("kruskal")) {
                steps = Kruskal.runWithSteps(nodes, edges);
            } else {
                steps = BFS.runWithSteps(adjList, startNode);
            }

            long endTime = System.nanoTime();
            double elapsedMs = (endTime - startTime) / 1e6;

            if (shouldRecord && steps != null && !steps.isEmpty()) {
                com.algovision.model.Execution exec = new com.algovision.model.Execution();
                exec.setUserId(userId);
                exec.setAlgorithmName(algoName);
                exec.setInputData("Nodes: " + nodeList + ", Start: " + startNode);
                exec.setStepCount(steps.size());
                exec.setComparisonCount(steps.size());
                exec.setSwapCount(0);
                exec.setExecutionTimeMs(elapsedMs);

                com.algovision.dao.ExecutionDAO executionDAO = new com.algovision.dao.ExecutionDAO();
                executionDAO.recordExecution(exec);
                progressDAO.recordAlgorithmProgress(userId, "Graph", algoName);
            }

            out.print(gson.toJson(steps));
        } else {
            out.print("[]");
        }
        out.flush();
    }
}
