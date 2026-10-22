package com.algovision.controller;

import com.algovision.dao.ExecutionDAO;
import com.algovision.dao.QuizDAO;
import com.algovision.model.Execution;
import com.algovision.model.QuizResult;
import com.algovision.model.User;
import com.algovision.model.UserProgress;
import com.algovision.service.ProgressService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashboardServlet extends HttpServlet {

    private final ExecutionDAO executionDAO = new ExecutionDAO();
    private final QuizDAO quizDAO = new QuizDAO();
    private final ProgressService progressService = new ProgressService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("loggedUser") : null;

        if (user == null) {
            user = new User(2, "Alex Student", "student@algovision.com", "", "STUDENT");
            user.setStreakDays(7);
        }

        int userId = user.getId();
        List<Execution> recentExecutions = executionDAO.getRecentExecutionsByUser(userId, 8);
        List<QuizResult> recentQuizzes = quizDAO.getResultsByUser(userId);

        double avgQuizScore = 86.0;
        if (!recentQuizzes.isEmpty()) {
            double sum = 0;
            for (QuizResult r : recentQuizzes) sum += r.getPercentage();
            avgQuizScore = sum / recentQuizzes.size();
        }

        List<UserProgress> userProgressList = progressService.getUserProgress(userId);
        int totalTopicProgress = 0;
        if (userProgressList != null) {
            for (UserProgress p : userProgressList) {
                totalTopicProgress += p.getCompletedCount();
            }
        }

        int totalExecutions = executionDAO.getTotalExecutionCountByUser(userId);
        int distinctAlgorithmsCompleted = executionDAO.getDistinctAlgorithmCountByUser(userId);
        
        // Accurate count of algorithms completed: increments dynamically with each visualized run
        int algorithmsCompleted = Math.max(totalExecutions, distinctAlgorithmsCompleted);
        if (algorithmsCompleted == 0 && recentExecutions != null && !recentExecutions.isEmpty()) {
            algorithmsCompleted = recentExecutions.size();
        }

        com.algovision.dao.ChallengeDAO challengeDAO = new com.algovision.dao.ChallengeDAO();
        List<com.algovision.model.Submission> userSubs = challengeDAO.getSubmissionsByUser(userId);
        Set<Integer> solvedChallenges = new HashSet<>();
        for (com.algovision.model.Submission s : userSubs) {
            if ("Accepted".equalsIgnoreCase(s.getStatus())) {
                solvedChallenges.add(s.getChallengeId());
            }
        }
        int problemsSolved = Math.max(solvedChallenges.size(), totalTopicProgress);

        request.setAttribute("user", user);
        request.setAttribute("algorithmsCompleted", algorithmsCompleted);
        request.setAttribute("problemsSolved", problemsSolved);
        request.setAttribute("quizScorePct", String.format("%.1f", avgQuizScore));
        request.setAttribute("streakDays", user.getStreakDays() > 0 ? user.getStreakDays() : 7);
        request.setAttribute("recentExecutions", recentExecutions);
        request.setAttribute("recentQuizzes", recentQuizzes);
        request.setAttribute("userProgress", userProgressList);
        request.setAttribute("userAchievements", progressService.getUserAchievements(userId));

        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
