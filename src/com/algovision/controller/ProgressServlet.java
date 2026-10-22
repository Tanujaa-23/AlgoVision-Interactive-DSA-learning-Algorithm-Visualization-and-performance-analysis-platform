package com.algovision.controller;

import com.algovision.model.User;
import com.algovision.service.ProgressService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class ProgressServlet extends HttpServlet {

    private final ProgressService progressService = new ProgressService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;
        int userId = 2; // Default to Student (user 2)
        if (user != null) {
            userId = user.getId();
        } else if (session != null && session.getAttribute("userId") != null) {
            userId = (Integer) session.getAttribute("userId");
        }

        request.setAttribute("userProgress", progressService.getUserProgress(userId));
        request.setAttribute("userAchievements", progressService.getUserAchievements(userId));
        request.getRequestDispatcher("/progress/progress.jsp").forward(request, response);
    }
}
