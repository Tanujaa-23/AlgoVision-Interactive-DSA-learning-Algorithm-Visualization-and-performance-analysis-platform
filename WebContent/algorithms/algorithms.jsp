<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.algovision.model.Algorithm, java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Algorithms Catalog - AlgoVision Pro</title>
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
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/algorithms">Algorithms</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/visualize">Visualizer</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/performance">Performance</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/quiz">Quizzes</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/challenges">Challenges</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-5">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="fw-bold text-white mb-1">DSA Algorithms Catalog</h2>
                <p class="text-muted mb-0">Learn time & space complexities, stability, and step mechanics</p>
            </div>
            <div class="btn-group">
                <a href="${pageContext.request.contextPath}/algorithms" class="btn btn-outline-custom ${empty category ? 'active' : ''}">All</a>
                <a href="${pageContext.request.contextPath}/algorithms?category=Sorting" class="btn btn-outline-custom ${'Sorting'.equalsIgnoreCase(category) ? 'active' : ''}">Sorting</a>
                <a href="${pageContext.request.contextPath}/algorithms?category=Searching" class="btn btn-outline-custom ${'Searching'.equalsIgnoreCase(category) ? 'active' : ''}">Searching</a>
                <a href="${pageContext.request.contextPath}/algorithms?category=Graph" class="btn btn-outline-custom ${'Graph'.equalsIgnoreCase(category) ? 'active' : ''}">Graph</a>
            </div>
        </div>

        <div class="row g-4">
            <% List<Algorithm> list = (List<Algorithm>) request.getAttribute("algorithmList");
               if (list != null && !list.isEmpty()) {
                   for (Algorithm a : list) { 
                       String aType = "sorting";
                       if (a.getCategory() != null) {
                           String c = a.getCategory().trim().toLowerCase();
                           if (c.contains("graph")) aType = "graph";
                           else if (c.contains("search")) aType = "searching";
                           else aType = "sorting";
                       }
                       String encodedName = java.net.URLEncoder.encode(a.getName(), "UTF-8");
                   %>
                    <div class="col-md-6 col-lg-4">
                        <div class="card card-custom h-100 p-4">
                            <div class="d-flex justify-content-between align-items-center mb-2">
                                <span class="badge bg-indigo"><%= a.getCategory() %></span>
                                <span class="badge bg-dark border border-secondary text-info"><%= a.getComplexityAvg() %></span>
                            </div>
                            <h4 class="fw-bold text-white mb-2">
                                <a href="${pageContext.request.contextPath}/algorithms?name=<%= encodedName %>" class="text-white text-decoration-none">
                                    <%= a.getName() %>
                                </a>
                            </h4>
                            <p class="text-muted small mb-4 flex-grow-1"><%= a.getDescription() %></p>

                            <div class="border-top border-secondary pt-3 small text-muted mb-3">
                                <div class="d-flex justify-content-between mb-1">
                                    <span>Best Case:</span>
                                    <strong class="text-light"><%= a.getComplexityBest() %></strong>
                                </div>
                                <div class="d-flex justify-content-between mb-1">
                                    <span>Worst Case:</span>
                                    <strong class="text-light"><%= a.getComplexityWorst() %></strong>
                                </div>
                                <div class="d-flex justify-content-between">
                                    <span>Space Complexity:</span>
                                    <strong class="text-light"><%= a.getSpaceComplexity() %></strong>
                                </div>
                            </div>

                            <div class="mt-auto d-flex flex-column gap-2">
                                <a href="${pageContext.request.contextPath}/visualize?type=<%= aType %>&algo=<%= encodedName %>&run=true" class="btn btn-primary-custom w-100 fw-bold d-flex align-items-center justify-content-center">
                                    <i class="bi bi-play-circle-fill me-2 fs-5"></i>Run Algorithm
                                </a>
                                <div class="d-flex gap-2">
                                    <a href="${pageContext.request.contextPath}/algorithms?name=<%= encodedName %>" class="btn btn-outline-secondary btn-sm flex-fill">
                                        <i class="bi bi-info-circle me-1"></i>Details
                                    </a>
                                    <form action="${pageContext.request.contextPath}/report" method="post" class="flex-fill" style="margin:0;">
                                        <input type="hidden" name="algoName" value="<%= a.getName() %>">
                                        <button type="submit" class="btn btn-outline-danger btn-sm w-100" title="Download PDF Report">
                                            <i class="bi bi-file-earmark-pdf"></i> Report
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
            <% } } %>
        </div>
    </div>

</body>
</html>
