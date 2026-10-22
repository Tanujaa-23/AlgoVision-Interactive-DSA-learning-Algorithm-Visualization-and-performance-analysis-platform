<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.algovision.service.RecommendationService.RecommendationResult" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Algorithm Recommendation Engine - AlgoVision Pro</title>
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
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/algorithms">Algorithms</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/visualize">Visualizer</a></li>
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/recommendation">Recommendation Engine</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-4" style="max-width: 860px;">
        <div class="text-center mb-4">
            <h2 class="fw-bold text-white"><i class="bi bi-robot text-primary me-2"></i>Intelligent Algorithm Recommendation Engine</h2>
            <p class="text-muted">Type any problem statement, query, or data characteristics. AlgoVision will accurately suggest the optimal Sorting, Searching, or Graph algorithm.</p>
        </div>

        <div class="card card-custom p-4 mb-4">
            <form action="${pageContext.request.contextPath}/recommendation" method="post" id="recForm">
                <div class="mb-3">
                    <label class="form-label text-info small fw-bold">ENTER PROBLEM DESCRIPTION OR CONSTRAINTS</label>
                    <textarea name="problemStatement" id="problemStatementInput" class="form-control bg-dark text-light border-secondary" rows="3" placeholder="e.g. Find shortest path between two nodes in a weighted graph, or search element in an unsorted list...">${query != null ? query : 'I want to find the shortest path between two nodes in a weighted graph.'}</textarea>
                </div>

                <!-- Quick Prompt Suggestions -->
                <div class="mb-3">
                    <span class="text-muted small d-block mb-2">Try quick scenario prompts:</span>
                    <div class="d-flex flex-wrap gap-2">
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="setQuery('Shortest path on road map with road weights and distances')">Dijkstra Shortest Path</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="setQuery('Traverse graph level by level to find nearest friends in social network')">BFS Level Order</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="setQuery('Explore a maze and detect cycles using backtracking')">DFS Maze / Cycle</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="setQuery('Connect all computer network servers with minimum total cable wire (MST)')">Prim / Kruskal MST</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="setQuery('Search target number in an unsorted random array sequentially')">Linear Search</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="setQuery('Quickly find element in sorted dictionary in logarithmic O(log n) time')">Binary Search</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="setQuery('Search sorted array by jumping blocks of square root size')">Jump Search</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="setQuery('Fastest in-place partitioning sorting algorithm for large arrays')">Quick Sort</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="setQuery('Guaranteed O(n log n) stable sorting preserving identical element order')">Merge Sort</button>
                        <button type="button" class="btn btn-sm btn-outline-secondary" onclick="setQuery('Sort nearly sorted streaming cards data one item at a time')">Insertion Sort</button>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary-custom w-100"><i class="bi bi-cpu-fill me-2"></i>Analyze & Recommend Algorithm</button>
            </form>
        </div>

        <% RecommendationResult rec = (RecommendationResult) request.getAttribute("recommendation");
           if (rec != null) { 
               String rCat = "sorting";
               if (rec.getCategory() != null) {
                   String rc = rec.getCategory().toLowerCase();
                   if (rc.contains("graph")) rCat = "graph";
                   else if (rc.contains("search")) rCat = "searching";
                   else rCat = "sorting";
               }
               String rAlgoEnc = java.net.URLEncoder.encode(rec.getRecommendedAlgorithm(), "UTF-8");
               String badgeClass = "bg-primary";
               if ("Graph".equalsIgnoreCase(rec.getCategory())) badgeClass = "bg-warning text-dark";
               else if ("Searching".equalsIgnoreCase(rec.getCategory())) badgeClass = "bg-info text-dark";
           %>
            <div class="card card-custom p-4 border-start border-4 border-primary shadow">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <span class="badge <%= badgeClass %> px-3 py-2 fw-bold"><i class="bi bi-tag-fill me-1"></i><%= rec.getCategory() %> Algorithm</span>
                    <span class="badge bg-dark border border-secondary text-info"><%= rec.getTimeComplexity() %></span>
                </div>
                <h3 class="fw-bold text-white mb-2">Recommended: <span class="text-info"><%= rec.getRecommendedAlgorithm() %></span></h3>
                <p class="text-light mb-3"><strong>Why this algorithm:</strong> <%= rec.getReason() %></p>
                <div class="row g-2 border-top border-secondary pt-3 text-muted small">
                    <div class="col-md-6">
                        <span>Expected Time Complexity:</span> <strong class="text-warning"><%= rec.getTimeComplexity() %></strong>
                    </div>
                    <div class="col-md-6">
                        <span>Auxiliary Space Complexity:</span> <strong class="text-warning"><%= rec.getSpaceComplexity() %></strong>
                    </div>
                </div>
                <div class="mt-4 d-flex gap-3">
                    <a href="${pageContext.request.contextPath}/visualize?type=<%= rCat %>&algo=<%= rAlgoEnc %>" class="btn btn-primary-custom">
                        <i class="bi bi-play-fill me-1"></i>Visualize <%= rec.getRecommendedAlgorithm() %> Step-by-Step
                    </a>
                    <form action="${pageContext.request.contextPath}/report" method="post" style="display:inline;">
                        <input type="hidden" name="algoName" value="<%= rec.getRecommendedAlgorithm() %>">
                        <button type="submit" class="btn btn-outline-danger">
                            <i class="bi bi-file-earmark-pdf-fill me-1"></i>Download Execution Report
                        </button>
                    </form>
                </div>
            </div>
        <% } %>
    </div>

    <script>
        function setQuery(text) {
            document.getElementById('problemStatementInput').value = text;
            document.getElementById('recForm').submit();
        }
    </script>
</body>
</html>
