<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.algovision.model.Challenge, com.algovision.model.Submission" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Solve Problem - AlgoVision Pro</title>
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
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/challenges">Back to Challenges</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-4">
        <% Challenge c = (Challenge) request.getAttribute("challenge");
           Submission sub = (Submission) request.getAttribute("submissionResult");
           String userCode = (String) request.getAttribute("userCode");
           if (userCode == null && c != null) userCode = c.getExpectedOutput(); %>

        <div class="row g-4">
            <div class="col-md-5">
                <div class="card card-custom p-4 h-100">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <span class="badge bg-indigo"><%= c != null ? c.getTopic() : "Arrays" %></span>
                        <span class="badge bg-success"><%= c != null ? c.getDifficulty() : "Easy" %></span>
                    </div>
                    <h3 class="fw-bold text-white mb-3"><%= c != null ? c.getTitle() : "Find Maximum Element" %></h3>
                    <p class="text-light mb-4"><%= c != null ? c.getDescription() : "Given space-separated integers, return maximum value." %></p>

                    <h6 class="fw-bold text-info mb-1">Sample Input</h6>
                    <pre class="bg-dark p-2 rounded text-light border border-secondary mb-3"><%= c != null ? c.getSampleInput() : "5 2 9 1 7" %></pre>

                    <h6 class="fw-bold text-info mb-1">Expected Sample Output</h6>
                    <pre class="bg-dark p-2 rounded text-light border border-secondary mb-3"><%= c != null ? c.getSampleOutput() : "9" %></pre>
                </div>
            </div>

            <div class="col-md-7">
                <div class="card card-custom p-4 h-100">
                    <h5 class="fw-bold text-white mb-3"><i class="bi bi-code-square text-info me-2"></i>Java Solution Workspace</h5>

                    <% if (sub != null) { %>
                        <div class="alert <%= "Accepted".equalsIgnoreCase(sub.getStatus()) ? "alert-success" : "alert-danger" %> py-2 mb-3">
                            <strong>Status: <%= sub.getStatus() %></strong> | Time: <%= String.format("%.2f ms", sub.getExecutionTimeMs()) %>
                        </div>
                    <% } %>

                    <form action="${pageContext.request.contextPath}/challenges" method="post">
                        <input type="hidden" name="challengeId" value="<%= c != null ? c.getId() : 1 %>">
                        <div class="mb-3">
                            <textarea name="userCode" class="form-control bg-dark text-light border-secondary font-monospace" rows="12" placeholder="Write output or Java method return..."><%= userCode != null ? userCode : "9" %></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary-custom w-100"><i class="bi bi-send-fill me-1"></i>Submit Solution</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
