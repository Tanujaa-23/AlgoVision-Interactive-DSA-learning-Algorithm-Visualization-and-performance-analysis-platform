<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.algovision.model.UserProgress, com.algovision.model.Achievement, java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Progress & Achievements - AlgoVision Pro</title>
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
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/progress">Progress & Badges</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-4">
        <h2 class="fw-bold text-white mb-4">Topic Mastery Progress</h2>

        <div class="row g-4 mb-5">
            <% List<UserProgress> pList = (List<UserProgress>) request.getAttribute("userProgress");
               if (pList != null) {
                   for (UserProgress p : pList) { 
                       String topicColor = "bg-primary";
                       String visualizerUrl = request.getContextPath() + "/visualize?type=sorting";
                       if ("Sorting".equalsIgnoreCase(p.getTopic())) {
                           topicColor = "bg-indigo";
                           visualizerUrl = request.getContextPath() + "/visualize?type=sorting";
                       } else if ("Searching".equalsIgnoreCase(p.getTopic())) {
                           topicColor = "bg-info";
                           visualizerUrl = request.getContextPath() + "/visualize?type=searching";
                       } else if ("Graph".equalsIgnoreCase(p.getTopic())) {
                           topicColor = "bg-purple";
                           visualizerUrl = request.getContextPath() + "/visualize?type=graph";
                       } else if ("Data Structures".equalsIgnoreCase(p.getTopic())) {
                           topicColor = "bg-success";
                           visualizerUrl = request.getContextPath() + "/datastructures";
                       }
            %>
                    <div class="col-md-6">
                        <div class="card card-custom p-4">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <strong class="text-white fs-5"><%= p.getTopic() %></strong>
                                <div class="d-flex align-items-center gap-2">
                                    <span class="text-info fw-semibold"><%= p.getCompletedCount() %> / <%= p.getTotalCount() %> Completed (<%= p.getPercentage() %>%)</span>
                                    <a href="<%= visualizerUrl %>" class="btn btn-outline-primary btn-sm py-0 px-2" title="Practice <%= p.getTopic() %>">
                                        <i class="bi bi-play-fill"></i> Practice
                                    </a>
                                </div>
                            </div>
                            <div class="progress bg-dark border border-secondary" style="height: 12px;">
                                <div class="progress-bar <%= topicColor %>" role="progressbar" style="width: <%= p.getPercentage() %>%;"></div>
                            </div>
                        </div>
                    </div>
            <% } } %>
        </div>

        <h3 class="fw-bold text-white mb-4"><i class="bi bi-award-fill text-warning me-2"></i>Earned Badges & Achievements</h3>

        <div class="row g-4">
            <% List<Achievement> achList = (List<Achievement>) request.getAttribute("userAchievements");
               if (achList != null) {
                   for (Achievement a : achList) { %>
                    <div class="col-md-4 col-lg-2">
                        <div class="badge-card <%= a.isEarned() ? "earned" : "" %>">
                            <i class="<%= a.getIconClass() %> badge-icon"></i>
                            <h6 class="fw-bold text-white mt-2 mb-1"><%= a.getTitle() %></h6>
                            <p class="text-muted small mb-0"><%= a.getDescription() %></p>
                        </div>
                    </div>
            <% } } %>
        </div>
    </div>

</body>
</html>
