package com.algovision.controller;

import com.algovision.dao.AlgorithmDAO;
import com.algovision.dao.ChallengeDAO;
import com.algovision.dao.ExecutionDAO;
import com.algovision.dao.UserDAO;
import com.algovision.model.Algorithm;
import com.algovision.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class AdminServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final AlgorithmDAO algorithmDAO = new AlgorithmDAO();
    private final ExecutionDAO executionDAO = new ExecutionDAO();
    private final ChallengeDAO challengeDAO = new ChallengeDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        // Role validation check for Admin
        if (user == null || !user.isAdmin()) {
            // Provide preview mode or redirect
            request.setAttribute("adminMode", "demo");
        }

        List<User> userList = userDAO.getAllUsers();
        List<Algorithm> algoList = algorithmDAO.getAllAlgorithms();

        request.setAttribute("totalUsers", userDAO.getTotalUserCount());
        request.setAttribute("totalAlgorithms", algoList.size());
        request.setAttribute("totalExecutions", executionDAO.getTotalExecutionCount());
        request.setAttribute("totalChallenges", challengeDAO.getAllChallenges().size());
        request.setAttribute("userList", userList);
        request.setAttribute("algoList", algoList);

        request.getRequestDispatcher("/admin/adminDashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("addAlgo".equalsIgnoreCase(action)) {
            Algorithm a = new Algorithm();
            a.setName(request.getParameter("name"));
            a.setCategory(request.getParameter("category"));
            a.setDescription(request.getParameter("description"));
            a.setHowItWorks(request.getParameter("howItWorks"));
            a.setComplexityBest(request.getParameter("complexityBest"));
            a.setComplexityAvg(request.getParameter("complexityAvg"));
            a.setComplexityWorst(request.getParameter("complexityWorst"));
            a.setSpaceComplexity(request.getParameter("spaceComplexity"));
            a.setStable("true".equalsIgnoreCase(request.getParameter("isStable")));
            a.setInPlace("true".equalsIgnoreCase(request.getParameter("isInPlace")));
            algorithmDAO.addAlgorithm(a);
        }
        response.sendRedirect(request.getContextPath() + "/admin?status=success");
    }
}
