package com.algovision.controller;

import com.algovision.model.Challenge;
import com.algovision.model.Submission;
import com.algovision.model.User;
import com.algovision.service.ChallengeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class ChallengeServlet extends HttpServlet {

    private final ChallengeService challengeService = new ChallengeService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                Challenge challenge = challengeService.getChallengeById(id);
                request.setAttribute("challenge", challenge);
                request.getRequestDispatcher("/challenges/challenge.jsp").forward(request, response);
                return;
            } catch (NumberFormatException ignored) {}
        }

        List<Challenge> challenges = challengeService.getAllChallenges();
        request.setAttribute("challenges", challenges);
        request.getRequestDispatcher("/challenges/challenges.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;
        int userId = (user != null && user.getId() > 0) ? user.getId() : 2;

        int challengeId = 1;
        try {
            challengeId = Integer.parseInt(request.getParameter("challengeId"));
        } catch (NumberFormatException ignored) {}

        String code = request.getParameter("userCode");
        Submission submission = challengeService.evaluateSubmission(userId, challengeId, code);

        Challenge challenge = challengeService.getChallengeById(challengeId);
        request.setAttribute("challenge", challenge);
        request.setAttribute("submissionResult", submission);
        request.setAttribute("userCode", code);
        request.getRequestDispatcher("/challenges/challenge.jsp").forward(request, response);
    }
}
