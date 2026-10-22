package com.algovision.controller;

import com.algovision.service.RecommendationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class RecommendationServlet extends HttpServlet {

    private final RecommendationService recommendationService = new RecommendationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/recommendation/recommendation.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String problemStatement = request.getParameter("problemStatement");
        RecommendationService.RecommendationResult result = recommendationService.recommendAlgorithm(problemStatement);
        request.setAttribute("query", problemStatement);
        request.setAttribute("recommendation", result);
        request.getRequestDispatcher("/recommendation/recommendation.jsp").forward(request, response);
    }
}
