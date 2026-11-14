package com.algovision.controller;

import com.algovision.model.Question;
import com.algovision.model.QuizResult;
import com.algovision.model.User;
import com.algovision.service.QuizService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizServlet extends HttpServlet {

    private final QuizService quizService = new QuizService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String topic = request.getParameter("topic");
        if (topic == null || topic.trim().isEmpty()) topic = "Sorting";

        List<Question> questions = quizService.getQuizQuestions(topic);
        request.setAttribute("topic", topic);
        request.setAttribute("questions", questions);
        request.getRequestDispatcher("/quiz/quiz.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;
        int userId = (user != null && user.getId() > 0) ? user.getId() : 2;

        String topic = request.getParameter("topic");
        if (topic == null) topic = "Sorting";

        List<Question> questions = quizService.getQuizQuestions(topic);
        Map<Integer, String> userAnswers = new HashMap<>();

        for (Question q : questions) {
            String ans = request.getParameter("q_" + q.getId());
            if (ans != null) userAnswers.put(q.getId(), ans);
        }

        int timeTaken = 60;
        try {
            String timeStr = request.getParameter("timeTaken");
            if (timeStr != null) timeTaken = Integer.parseInt(timeStr);
        } catch (NumberFormatException ignored) {}

        QuizResult result = quizService.evaluateQuiz(userId, topic, userAnswers, timeTaken);
        request.setAttribute("quizResult", result);
        request.setAttribute("questions", questions);
        request.setAttribute("userAnswers", userAnswers);
        request.getRequestDispatcher("/quiz/result.jsp").forward(request, response);
    }
}
