<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.algovision.model.Algorithm" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${algorithm != null ? algorithm.name : 'Algorithm Details'} - AlgoVision Pro</title>
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
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-5" style="max-width: 900px;">
        <% Algorithm a = (Algorithm) request.getAttribute("algorithm");
           if (a != null) { 
               String aType = "sorting";
               if (a.getCategory() != null) {
                   String c = a.getCategory().trim().toLowerCase();
                   if (c.contains("graph")) aType = "graph";
                   else if (c.contains("search")) aType = "searching";
                   else aType = "sorting";
               }
               String encodedName = java.net.URLEncoder.encode(a.getName(), "UTF-8");
        %>
            <div class="mb-4">
                <a href="${pageContext.request.contextPath}/algorithms" class="btn btn-outline-secondary btn-sm mb-3">
                    <i class="bi bi-arrow-left me-1"></i> Back to Algorithms Catalog
                </a>
                <div class="d-flex flex-wrap justify-content-between align-items-center gap-3">
                    <div>
                        <span class="badge bg-indigo px-3 py-2 text-uppercase mb-2"><%= a.getCategory() %> Algorithm</span>
                        <h1 class="fw-bold text-white mb-1"><%= a.getName() %></h1>
                    </div>
                    <div class="d-flex gap-2">
                        <a href="${pageContext.request.contextPath}/visualize?type=<%= aType %>&algo=<%= encodedName %>&run=true" class="btn btn-primary-custom btn-lg">
                            <i class="bi bi-play-circle-fill me-2"></i>Run Algorithm
                        </a>
                        <form action="${pageContext.request.contextPath}/report" method="post" style="display:inline;">
                            <input type="hidden" name="algoName" value="<%= a.getName() %>">
                            <button type="submit" class="btn btn-outline-danger btn-lg" title="Download PDF Report">
                                <i class="bi bi-file-earmark-pdf"></i> Report
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Complexities Grid -->
            <div class="row g-3 mb-4 text-center">
                <div class="col-6 col-md-3">
                    <div class="card card-custom p-3">
                        <span class="text-muted small d-block mb-1">BEST CASE</span>
                        <strong class="text-success h5 mb-0"><%= a.getComplexityBest() %></strong>
                    </div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="card card-custom p-3">
                        <span class="text-muted small d-block mb-1">AVERAGE CASE</span>
                        <strong class="text-info h5 mb-0"><%= a.getComplexityAvg() %></strong>
                    </div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="card card-custom p-3">
                        <span class="text-muted small d-block mb-1">WORST CASE</span>
                        <strong class="text-warning h5 mb-0"><%= a.getComplexityWorst() %></strong>
                    </div>
                </div>
                <div class="col-6 col-md-3">
                    <div class="card card-custom p-3">
                        <span class="text-muted small d-block mb-1">SPACE COMPLEXITY</span>
                        <strong class="text-danger h5 mb-0"><%= a.getSpaceComplexity() %></strong>
                    </div>
                </div>
            </div>

            <!-- Algorithm Description -->
            <div class="card card-custom p-4 mb-4">
                <h4 class="fw-bold text-white mb-3"><i class="bi bi-card-text text-info me-2"></i>Overview & Characteristics</h4>
                <p class="text-light lead fs-6 mb-3"><%= a.getDescription() %></p>
                <div class="d-flex gap-3">
                    <span class="badge <%= a.isStable() ? "bg-success" : "bg-secondary" %> px-3 py-2">
                        <i class="bi <%= a.isStable() ? "bi-check-circle" : "bi-x-circle" %> me-1"></i>
                        <%= a.isStable() ? "Stable Algorithm" : "Unstable Algorithm" %>
                    </span>
                    <span class="badge <%= a.isInPlace() ? "bg-primary" : "bg-dark border border-secondary" %> px-3 py-2">
                        <i class="bi <%= a.isInPlace() ? "bi-check-circle" : "bi-x-circle" %> me-1"></i>
                        <%= a.isInPlace() ? "In-Place Execution" : "Requires Auxiliary Memory" %>
                    </span>
                </div>
            </div>

            <!-- How it Works -->
            <div class="card card-custom p-4 mb-4">
                <h4 class="fw-bold text-white mb-3"><i class="bi bi-gear-fill text-warning me-2"></i>How It Works</h4>
                <div class="p-3 bg-dark rounded border border-secondary text-light">
                    <%= a.getHowItWorks() != null ? a.getHowItWorks() : "Detailed execution steps can be observed dynamically in the visualizer." %>
                </div>
            </div>

            <!-- Bottom Run CTA -->
            <div class="text-center p-4 bg-dark rounded border border-secondary">
                <h5 class="fw-bold text-white mb-2">Ready to test and observe <%= a.getName() %> in real-time?</h5>
                <p class="text-muted small mb-3">Launch the interactive visual engine with custom data inputs, step highlights, and operation counters.</p>
                <a href="${pageContext.request.contextPath}/visualize?type=<%= aType %>&algo=<%= encodedName %>&run=true" class="btn btn-primary-custom px-4 py-2">
                    <i class="bi bi-play-circle-fill me-2"></i>Run <%= a.getName() %> Now
                </a>
            </div>

        <% } else { %>
            <div class="alert alert-warning text-center">
                <h5>Algorithm not found</h5>
                <a href="${pageContext.request.contextPath}/algorithms" class="btn btn-outline-custom mt-2">Return to Catalog</a>
            </div>
        <% } %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
