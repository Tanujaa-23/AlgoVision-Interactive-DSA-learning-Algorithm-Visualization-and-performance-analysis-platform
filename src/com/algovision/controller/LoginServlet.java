package com.algovision.controller;

import com.algovision.dao.UserDAO;
import com.algovision.model.User;
import com.algovision.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedUser") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (!ValidationUtil.isValidEmail(email) || !ValidationUtil.isNotEmpty(password)) {
            request.setAttribute("errorMessage", "Please enter a valid email address and password.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        String cleanEmail = email.trim().toLowerCase();
        User existingUser = userDAO.getUserByEmail(cleanEmail);

        User user = userDAO.validateLogin(email, password);
        if (user != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedUser", user);
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userId", user.getId());

            if (user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/admin");
            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } else {
            if (existingUser != null) {
                request.setAttribute("errorMessage", "Incorrect password for '" + cleanEmail + "'. Please verify your password.");
            } else {
                request.setAttribute("errorMessage", "Invalid email or password. Please check your credentials or register.");
            }
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
