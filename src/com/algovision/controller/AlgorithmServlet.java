package com.algovision.controller;

import com.algovision.model.Algorithm;
import com.algovision.service.AlgorithmService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class AlgorithmServlet extends HttpServlet {

    private final AlgorithmService algorithmService = new AlgorithmService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String category = request.getParameter("category");
        String name = request.getParameter("name");

        if (name != null && !name.trim().isEmpty()) {
            Algorithm algo = algorithmService.getAlgorithmByName(name.trim());
            request.setAttribute("algorithm", algo);
            request.getRequestDispatcher("/algorithms/algorithmDetail.jsp").forward(request, response);
            return;
        }

        List<Algorithm> algorithms;
        if (category != null && !category.trim().isEmpty()) {
            algorithms = algorithmService.getAlgorithmsByCategory(category.trim());
            request.setAttribute("category", category);
        } else {
            algorithms = algorithmService.getAllAlgorithms();
        }

        request.setAttribute("algorithmList", algorithms);
        request.getRequestDispatcher("/algorithms/algorithms.jsp").forward(request, response);
    }
}
