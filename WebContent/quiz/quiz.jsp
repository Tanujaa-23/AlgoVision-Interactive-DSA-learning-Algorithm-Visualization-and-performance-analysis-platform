<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.algovision.model.Question, java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>DSA Quiz - AlgoVision Pro</title>
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
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/quiz">Quizzes</a></li>
                </ul>
                <div class="d-flex align-items-center text-warning gap-2">
                    <i class="bi bi-stopwatch-fill h5 mb-0"></i>
                    <span id="quizTimerDisplay" class="h5 fw-bold mb-0">00:00</span>
                </div>
            </div>
        </div>
    </nav>

    <div class="container py-4" style="max-width: 800px;">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="fw-bold text-white mb-0">DSA Assessment Quiz: <span class="text-info">${topic}</span></h3>
            <span class="badge bg-indigo px-3 py-2">6 Questions</span>
        </div>

        <form action="${pageContext.request.contextPath}/quiz" method="post">
            <input type="hidden" name="topic" value="${topic}">
            <input type="hidden" id="timeTakenInput" name="timeTaken" value="0">

            <% List<Question> qList = (List<Question>) request.getAttribute("questions");
               if (qList != null) {
                   int idx = 0;
                   for (Question q : qList) { idx++; %>
                    <div class="card card-custom p-4 mb-4">
                        <div class="d-flex justify-content-between mb-2">
                            <span class="badge bg-secondary">Question <%= idx %></span>
                            <span class="badge bg-dark border border-warning text-warning"><%= q.getDifficulty() %></span>
                        </div>
                        <h5 class="fw-bold text-white mb-3"><%= q.getQuestion() %></h5>

                        <div class="form-check mb-2">
                            <input class="form-check-input" type="radio" name="q_<%= q.getId() %>" value="A" id="q_<%= q.getId() %>_a">
                            <label class="form-check-label text-light" for="q_<%= q.getId() %>_a">A) <%= q.getOptionA() %></label>
                        </div>
                        <div class="form-check mb-2">
                            <input class="form-check-input" type="radio" name="q_<%= q.getId() %>" value="B" id="q_<%= q.getId() %>_b">
                            <label class="form-check-label text-light" for="q_<%= q.getId() %>_b">B) <%= q.getOptionB() %></label>
                        </div>
                        <div class="form-check mb-2">
                            <input class="form-check-input" type="radio" name="q_<%= q.getId() %>" value="C" id="q_<%= q.getId() %>_c">
                            <label class="form-check-label text-light" for="q_<%= q.getId() %>_c">C) <%= q.getOptionC() %></label>
                        </div>
                        <div class="form-check mb-2">
                            <input class="form-check-input" type="radio" name="q_<%= q.getId() %>" value="D" id="q_<%= q.getId() %>_d">
                            <label class="form-check-label text-light" for="q_<%= q.getId() %>_d">D) <%= q.getOptionD() %></label>
                        </div>
                    </div>
            <% } } %>

            <button type="submit" class="btn btn-primary-custom btn-lg w-100 mb-5">Submit Answers</button>
        </form>
    </div>

    <script src="${pageContext.request.contextPath}/js/quiz.js"></script>
</body>
</html>
