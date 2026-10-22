<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.algovision.model.Challenge, java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Coding Challenges - AlgoVision Pro</title>
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
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/challenges">Challenges</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="fw-bold text-white mb-1">DSA Practice Coding Challenges</h2>
                <p class="text-muted mb-0">Solve array, graph, stack, and search problems with immediate output verification</p>
            </div>
        </div>

        <div class="row g-4">
            <% List<Challenge> cList = (List<Challenge>) request.getAttribute("challenges");
               if (cList != null) {
                   for (Challenge c : cList) { %>
                    <div class="col-md-6">
                        <div class="card card-custom p-4 h-100">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <span class="badge bg-indigo"><%= c.getTopic() %></span>
                                <span class="badge <%= "Easy".equalsIgnoreCase(c.getDifficulty()) ? "bg-success" : "bg-warning" %>"><%= c.getDifficulty() %></span>
                            </div>
                            <h4 class="fw-bold text-white mb-2"><%= c.getTitle() %></h4>
                            <p class="text-muted small mb-4"><%= c.getDescription() %></p>
                            <a href="${pageContext.request.contextPath}/challenges?id=<%= c.getId() %>" class="btn btn-primary-custom w-100"><i class="bi bi-code-slash me-1"></i>Solve Problem</a>
                        </div>
                    </div>
            <% } } %>
        </div>
    </div>

</body>
</html>
