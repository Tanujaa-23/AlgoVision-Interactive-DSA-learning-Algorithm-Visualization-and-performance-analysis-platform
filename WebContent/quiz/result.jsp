<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.algovision.model.QuizResult, com.algovision.model.Question, java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Quiz Score Result - AlgoVision Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark navbar-custom sticky-top">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/dashboard">
                <i class="bi bi-diagram-3-fill text-info"></i> AlgoVision Pro
            </a>
        </div>
    </nav>

    <div class="container py-4" style="max-width: 800px;">
        <% QuizResult res = (QuizResult) request.getAttribute("quizResult"); %>

        <div class="card card-custom p-4 text-center mb-4">
            <h2 class="fw-bold text-white mb-2">Quiz Assessment Score</h2>
            <div class="display-3 fw-bold <%= res != null && res.getPercentage() >= 70 ? "text-success" : "text-warning" %> my-3">
                <%= res != null ? String.format("%.0f%%", res.getPercentage()) : "83%" %>
            </div>
            <p class="text-muted mb-4">
                Topic: <strong><%= res != null ? res.getTopic() : "Sorting" %></strong> |
                Correct: <strong class="text-success"><%= res != null ? res.getCorrectAnswers() : 5 %></strong> / <%= res != null ? res.getTotalQuestions() : 6 %> |
                Time Taken: <strong><%= res != null ? res.getTimeTakenSeconds() : 45 %>s</strong>
            </p>
            <div class="d-flex justify-content-center gap-3">
                <a href="${pageContext.request.contextPath}/quiz" class="btn btn-outline-custom">Retake Quiz</a>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary-custom">Return to Dashboard</a>
            </div>
        </div>

        <h4 class="fw-bold text-white mb-3">Detailed Solution Breakdown</h4>
        <% List<Question> qList = (List<Question>) request.getAttribute("questions");
           Map<Integer, String> userAns = (Map<Integer, String>) request.getAttribute("userAnswers");
           if (qList != null) {
               for (Question q : qList) {
                   String userSelected = userAns != null ? userAns.get(q.getId()) : "";
                   boolean isCorrect = q.getCorrectOption().equalsIgnoreCase(userSelected); %>
                    <div class="card card-custom p-4 mb-3 border-start border-4 <%= isCorrect ? "border-success" : "border-danger" %>">
                        <div class="d-flex justify-content-between mb-1">
                            <h6 class="fw-bold text-white"><%= q.getQuestion() %></h6>
                            <span class="badge <%= isCorrect ? "bg-success" : "bg-danger" %>"><%= isCorrect ? "CORRECT" : "WRONG" %></span>
                        </div>
                        <p class="small text-muted mb-2">Your Answer: <%= userSelected %> | Correct Answer: <strong class="text-success"><%= q.getCorrectOption() %></strong></p>
                        <div class="p-2 bg-dark rounded text-info small">
                            <strong>Explanation:</strong> <%= q.getExplanation() %>
                        </div>
                    </div>
        <% } } %>
    </div>

</body>
</html>
