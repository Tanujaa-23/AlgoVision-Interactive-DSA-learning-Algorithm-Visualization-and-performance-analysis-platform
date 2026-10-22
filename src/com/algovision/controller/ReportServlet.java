package com.algovision.controller;

import com.algovision.model.Algorithm;
import com.algovision.model.User;
import com.algovision.service.AlgorithmService;
import com.algovision.util.PDFReportGenerator;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;

public class ReportServlet extends HttpServlet {

    private final AlgorithmService algorithmService = new AlgorithmService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String algoName = request.getParameter("algo");
        if (algoName == null || algoName.trim().isEmpty()) {
            algoName = "Bubble Sort";
        }

        request.setAttribute("selectedAlgo", algoName.trim());
        request.getRequestDispatcher("/reports/report.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;
        String userName = user != null ? user.getName() : "Student User";

        String algoName = request.getParameter("algoName");
        if (algoName == null || algoName.trim().isEmpty()) {
            algoName = "Bubble Sort";
        }
        algoName = algoName.trim();

        String lower = algoName.toLowerCase();
        boolean isGraph = lower.contains("bfs") || lower.contains("dfs") 
                || lower.contains("dijkstra") || lower.contains("prim") 
                || lower.contains("kruskal") || lower.contains("graph");
        boolean isSearch = !isGraph && (lower.contains("search") || lower.contains("linear") 
                || lower.contains("binary") || lower.contains("jump"));

        String inputStr = request.getParameter("inputData");
        String outputStr = request.getParameter("outputData");

        if (inputStr == null || inputStr.trim().isEmpty()) {
            if (isGraph) {
                inputStr = "Graph Nodes: [A, B, C, D], Edges: A-B(4), A-C(2), B-C(1), B-D(5), C-D(8), Source: Node A";
            } else if (isSearch) {
                inputStr = "Target: 12, Array: [1, 2, 5, 8, 12, 16, 23, 34]";
            } else {
                inputStr = "5, 2, 8, 1, 3, 9, 4, 7";
            }
        }

        if (outputStr == null || outputStr.trim().isEmpty()) {
            if (isGraph) {
                if (lower.contains("dijkstra")) {
                    outputStr = "Shortest Path to D: A -> C -> B -> D (Min Cost: 8.0)";
                } else if (lower.contains("prim") || lower.contains("kruskal")) {
                    outputStr = "Minimum Spanning Tree Edges: [B-C(1), A-C(2), A-B(4)] (Total Weight: 7.0)";
                } else {
                    outputStr = "Traversal Sequence: [A, B, C, D] (All reachable nodes visited)";
                }
            } else if (isSearch) {
                outputStr = "Target element 12 successfully located at index 4";
            } else {
                outputStr = "1, 2, 3, 4, 5, 7, 8, 9 (Ascending order)";
            }
        }

        int defaultSteps = isGraph ? 7 : (isSearch ? 4 : 14);
        int defaultComps = isGraph ? 6 : (isSearch ? 4 : 12);
        int defaultSwaps = isGraph || isSearch ? 0 : 6;
        double defaultTime = isGraph ? 1.15 : (isSearch ? 0.38 : 1.25);

        int stepCount = parseDef(request.getParameter("stepCount"), defaultSteps);
        int comparisons = parseDef(request.getParameter("comparisons"), defaultComps);
        int swaps = parseDef(request.getParameter("swaps"), defaultSwaps);
        double timeMs = parseDoubleDef(request.getParameter("executionTimeMs"), defaultTime);

        Algorithm algo = algorithmService.getAlgorithmByName(algoName);
        String best = "O(n log n)";
        String avg = "O(n log n)";
        String worst = "O(n log n)";
        String space = "O(1)";

        if (algo != null) {
            best = algo.getComplexityBest();
            avg = algo.getComplexityAvg();
            worst = algo.getComplexityWorst();
            space = algo.getSpaceComplexity();
        } else {
            // Intelligent fallback metrics
            if (lower.contains("bubble")) {
                best = "O(n)"; avg = "O(n²)"; worst = "O(n²)"; space = "O(1)";
            } else if (lower.contains("selection")) {
                best = "O(n²)"; avg = "O(n²)"; worst = "O(n²)"; space = "O(1)";
            } else if (lower.contains("insertion")) {
                best = "O(n)"; avg = "O(n²)"; worst = "O(n²)"; space = "O(1)";
            } else if (lower.contains("merge")) {
                best = "O(n log n)"; avg = "O(n log n)"; worst = "O(n log n)"; space = "O(n)";
            } else if (lower.contains("quick")) {
                best = "O(n log n)"; avg = "O(n log n)"; worst = "O(n²)"; space = "O(log n)";
            } else if (lower.contains("heap")) {
                best = "O(n log n)"; avg = "O(n log n)"; worst = "O(n log n)"; space = "O(1)";
            } else if (lower.contains("linear")) {
                best = "O(1)"; avg = "O(n)"; worst = "O(n)"; space = "O(1)";
            } else if (lower.contains("binary")) {
                best = "O(1)"; avg = "O(log n)"; worst = "O(log n)"; space = "O(1)";
            } else if (lower.contains("jump")) {
                best = "O(1)"; avg = "O(√n)"; worst = "O(√n)"; space = "O(1)";
            } else if (lower.contains("dijkstra")) {
                best = "O((V + E) log V)"; avg = "O((V + E) log V)"; worst = "O((V + E) log V)"; space = "O(V)";
            } else if (lower.contains("prim")) {
                best = "O(E log V)"; avg = "O(E log V)"; worst = "O(E log V)"; space = "O(V)";
            } else if (lower.contains("kruskal")) {
                best = "O(E log E)"; avg = "O(E log E)"; worst = "O(E log E)"; space = "O(V + E)";
            } else if (lower.contains("bfs") || lower.contains("dfs")) {
                best = "O(V + E)"; avg = "O(V + E)"; worst = "O(V + E)"; space = "O(V)";
            }
        }

        String safeFileAlgo = algoName.replaceAll("[^a-zA-Z0-9_\\-]+", "_");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"AlgoVision_Report_" + safeFileAlgo + ".pdf\"");

        try (OutputStream out = response.getOutputStream()) {
            PDFReportGenerator.generateExecutionReport(
                out,
                userName,
                algoName,
                inputStr,
                outputStr,
                stepCount,
                comparisons,
                swaps,
                timeMs,
                best, avg, worst, space
            );
        } catch (Exception e) {
            System.err.println("[ReportServlet] PDF generation error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "PDF Generation Failed");
        }
    }

    private int parseDef(String val, int def) {
        try {
            return (val != null && !val.trim().isEmpty()) ? Integer.parseInt(val.trim()) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }

    private double parseDoubleDef(String val, double def) {
        try {
            return (val != null && !val.trim().isEmpty()) ? Double.parseDouble(val.trim()) : def;
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
