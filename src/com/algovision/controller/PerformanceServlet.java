package com.algovision.controller;

import com.algovision.service.PerformanceService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PerformanceServlet extends HttpServlet {

    private final PerformanceService performanceService = new PerformanceService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sizesParam = request.getParameter("sizes");
        int[] inputSizes = new int[]{100, 500, 1000, 5000};
        
        if (sizesParam != null && !sizesParam.trim().isEmpty()) {
            try {
                String[] parts = sizesParam.split(",");
                int[] parsed = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    parsed[i] = Integer.parseInt(parts[i].trim());
                }
                if (parsed.length > 0) {
                    inputSizes = parsed;
                }
            } catch (Exception ignored) {}
        }

        List<PerformanceService.MetricResult> results = performanceService.compareSortingAlgorithms(inputSizes);
        request.setAttribute("benchmarkResults", results);
        request.setAttribute("sizesParam", sizesParam != null ? sizesParam : "100, 500, 1000, 5000");
        request.getRequestDispatcher("/performance/performance.jsp").forward(request, response);
    }
}
