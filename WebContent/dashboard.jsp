<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.algovision.model.User, com.algovision.model.Execution, com.algovision.model.QuizResult, com.algovision.model.UserProgress, java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Student Dashboard - AlgoVision Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark navbar-custom sticky-top">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/dashboard">
                <i class="bi bi-diagram-3-fill text-info"></i> AlgoVision Pro
            </a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/algorithms">Algorithms</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/visualize">Visualizer</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/datastructures">Data Structures</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/performance">Performance</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/quiz">Quizzes</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/challenges">Challenges</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/recommendation">Recommendations</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/progress">Progress</a></li>
                </ul>
                <div class="d-flex align-items-center gap-3 text-light">
                    <span class="badge bg-danger rounded-pill px-3 py-2"><i class="bi bi-fire me-1"></i>${streakDays} Day Streak</span>
                    <span class="small text-muted">Welcome, <strong>${sessionScope.userName != null ? sessionScope.userName : 'Alex Student'}</strong></span>
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm"><i class="bi bi-box-arrow-right"></i></a>
                </div>
            </div>
        </div>
    </nav>

    <!-- Main Container -->
    <div class="container py-4">

        <!-- Welcome Banner -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="fw-bold text-white mb-1">Welcome back, ${sessionScope.userName != null ? sessionScope.userName : 'Alex Student'}! 👋</h2>
                <p class="text-muted mb-0">Track your algorithm execution metrics and learning streak</p>
            </div>
            <a href="${pageContext.request.contextPath}/visualize" class="btn btn-primary-custom"><i class="bi bi-play-circle-fill me-2"></i>Launch Visualizer</a>
        </div>

        <!-- 4 Metric Counters -->
        <div class="row g-3 mb-4">
            <div class="col-md-3">
                <div class="stat-box text-center">
                    <div class="stat-value">${algorithmsCompleted}</div>
                    <div class="stat-label">Algorithms Completed</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-box text-center">
                    <div class="stat-value text-info">${problemsSolved}</div>
                    <div class="stat-label">Problems Solved</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-box text-center">
                    <div class="stat-value text-success">${quizScorePct}%</div>
                    <div class="stat-label">Average Quiz Score</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-box text-center">
                    <div class="stat-value text-warning">${streakDays} Days</div>
                    <div class="stat-label">Current Streak</div>
                </div>
            </div>
        </div>

        <!-- Charts & Recent Activity Row -->
        <div class="row g-4 mb-4">
            <!-- Progress Doughnut Chart & Topic Bars -->
            <div class="col-lg-5">
                <div class="card card-custom p-4 h-100">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="fw-bold text-white mb-0"><i class="bi bi-pie-chart-fill text-info me-2"></i>Topic Progress Breakdown</h5>
                        <a href="${pageContext.request.contextPath}/progress" class="btn btn-outline-info btn-sm py-0 px-2">
                            <i class="bi bi-award-fill me-1"></i>All Badges
                        </a>
                    </div>
                    
                    <div style="height: 180px; position: relative;">
                        <canvas id="progressChartCanvas"></canvas>
                    </div>

                    <% 
                    List<UserProgress> pListLocal = (List<UserProgress>) request.getAttribute("userProgress");
                    String strongest = "Sorting";
                    String weakest = "Graph";
                    if (pListLocal != null && !pListLocal.isEmpty()) {
                        UserProgress topP = null;
                        UserProgress lowP = null;
                        for (UserProgress up : pListLocal) {
                            if (topP == null || up.getPercentage() > topP.getPercentage()) topP = up;
                            if (lowP == null || up.getPercentage() < lowP.getPercentage()) lowP = up;
                        }
                        if (topP != null) strongest = topP.getTopic() + " (" + topP.getPercentage() + "%)";
                        if (lowP != null) weakest = lowP.getTopic() + " (" + lowP.getPercentage() + "%)";
                    }
                    %>

                    <!-- Accurate Topic Progress Bars -->
                    <div class="mt-3 pt-3 border-top border-secondary">
                        <% if (pListLocal != null && !pListLocal.isEmpty()) {
                            for (UserProgress up : pListLocal) { 
                                String pBarColor = "bg-primary";
                                if ("Sorting".equalsIgnoreCase(up.getTopic())) pBarColor = "bg-indigo";
                                else if ("Searching".equalsIgnoreCase(up.getTopic())) pBarColor = "bg-info";
                                else if ("Graph".equalsIgnoreCase(up.getTopic())) pBarColor = "bg-purple";
                                else if ("Data Structures".equalsIgnoreCase(up.getTopic())) pBarColor = "bg-success";
                        %>
                            <div class="mb-2">
                                <div class="d-flex justify-content-between small mb-1">
                                    <span class="text-light fw-medium"><%= up.getTopic() %></span>
                                    <span class="text-info"><%= up.getCompletedCount() %> / <%= up.getTotalCount() %> Completed (<%= up.getPercentage() %>%)</span>
                                </div>
                                <div class="progress bg-dark border border-secondary" style="height: 6px;">
                                    <div class="progress-bar <%= pBarColor %>" role="progressbar" style="width: <%= up.getPercentage() %>%;"></div>
                                </div>
                            </div>
                        <% } } %>
                    </div>

                    <div class="d-flex justify-content-between mt-3 text-muted small border-top border-secondary pt-2">
                        <span><strong>Strongest:</strong> <span class="text-success"><%= strongest %></span></span>
                        <span><strong>Focus Area:</strong> <span class="text-warning"><%= weakest %></span></span>
                    </div>
                </div>
            </div>

            <!-- Recent Executions List -->
            <div class="col-lg-7">
                <div class="card card-custom p-4 h-100">
                    <div class="d-flex justify-content-between align-items-center mb-3">
                        <h5 class="fw-bold text-white mb-0"><i class="bi bi-clock-history text-warning me-2"></i>Recent Algorithm Visualizations</h5>
                        <a href="${pageContext.request.contextPath}/visualize" class="btn btn-outline-primary btn-sm py-0 px-2">
                            <i class="bi bi-play-fill me-1"></i>New Run
                        </a>
                    </div>
                    <div class="table-responsive">
                        <table class="table table-dark table-hover align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>Algorithm</th>
                                    <th>Category</th>
                                    <th>Input / Parameters</th>
                                    <th>Steps</th>
                                    <th>Comparisons</th>
                                    <th>Time</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% List<Execution> execs = (List<Execution>) request.getAttribute("recentExecutions");
                                   if (execs != null && !execs.isEmpty()) {
                                       for (Execution e : execs) { 
                                           String cat = e.getCategory();
                                           String catBadge = "bg-primary";
                                           if ("Graph".equalsIgnoreCase(cat)) catBadge = "bg-purple text-white";
                                           else if ("Searching".equalsIgnoreCase(cat)) catBadge = "bg-info text-dark";
                                           else if ("Sorting".equalsIgnoreCase(cat)) catBadge = "bg-indigo text-white";
                                %>
                                    <tr>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/visualize?type=<%= e.getType() %>&algo=<%= java.net.URLEncoder.encode(e.getAlgorithmName(), "UTF-8") %>&run=true" 
                                               class="text-white text-decoration-none fw-semibold" title="Launch Visualizer">
                                                <%= e.getAlgorithmName() %>
                                            </a>
                                        </td>
                                        <td><span class="badge <%= catBadge %>"><%= cat %></span></td>
                                        <td class="small text-muted" style="max-width: 150px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;" title="<%= e.getInputData() %>">
                                            <%= e.getInputData() %>
                                        </td>
                                        <td><%= e.getStepCount() %></td>
                                        <td><%= e.getComparisonCount() %></td>
                                        <td class="text-info"><%= String.format("%.2f", e.getExecutionTimeMs()) %> ms</td>
                                        <td>
                                            <div class="d-flex align-items-center gap-1">
                                                <a href="${pageContext.request.contextPath}/visualize?type=<%= e.getType() %>&algo=<%= java.net.URLEncoder.encode(e.getAlgorithmName(), "UTF-8") %>&run=true" 
                                                   class="btn btn-outline-success btn-sm py-0 px-2" title="Re-run and solve <%= e.getAlgorithmName() %>">
                                                    <i class="bi bi-play-circle-fill"></i> Run
                                                </a>
                                                <form action="${pageContext.request.contextPath}/report" method="post" style="display:inline; margin:0;">
                                                    <input type="hidden" name="algoName" value="<%= e.getAlgorithmName() %>">
                                                    <input type="hidden" name="inputData" value="<%= e.getInputData() %>">
                                                    <input type="hidden" name="stepCount" value="<%= e.getStepCount() %>">
                                                    <input type="hidden" name="comparisons" value="<%= e.getComparisonCount() %>">
                                                    <input type="hidden" name="swaps" value="<%= e.getSwapCount() %>">
                                                    <input type="hidden" name="executionTimeMs" value="<%= e.getExecutionTimeMs() %>">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm py-0 px-2" title="Download PDF Report for <%= e.getAlgorithmName() %>">
                                                        <i class="bi bi-file-earmark-pdf"></i> PDF
                                                    </button>
                                                </form>
                                            </div>
                                        </td>
                                    </tr>
                                <% } } else { %>
                                    <tr>
                                        <td colspan="7" class="text-center text-muted py-4">
                                            <i class="bi bi-diagram-3 fs-3 d-block mb-2 text-secondary"></i>
                                            No algorithms visualized yet. Launch the visualizer to record steps!
                                        </td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- Recommended & Quick Actions -->
        <div class="row g-4">
            <div class="col-md-6">
                <div class="card card-custom p-4 h-100">
                    <h5 class="fw-bold text-white mb-3"><i class="bi bi-lightbulb-fill text-warning me-2"></i>Recommended Algorithms For You</h5>
                    <div class="list-group list-group-flush bg-transparent">
                        <a href="${pageContext.request.contextPath}/visualize?type=sorting&algo=Merge+Sort" class="list-group-item list-group-item-action bg-transparent text-light border-secondary d-flex justify-content-between align-items-center">
                            <div>
                                <strong>Merge Sort</strong> <span class="badge bg-secondary ms-2">O(n log n)</span>
                                <div class="small text-muted">Divide and conquer stable sorting</div>
                            </div>
                            <i class="bi bi-chevron-right text-info"></i>
                        </a>
                        <a href="${pageContext.request.contextPath}/visualize?type=graph&algo=Dijkstra" class="list-group-item list-group-item-action bg-transparent text-light border-secondary d-flex justify-content-between align-items-center">
                            <div>
                                <strong>Dijkstra's Algorithm</strong> <span class="badge bg-secondary ms-2">Graph Shortest Path</span>
                                <div class="small text-muted">Single source shortest path on weighted graph</div>
                            </div>
                            <i class="bi bi-chevron-right text-info"></i>
                        </a>
                        <a href="${pageContext.request.contextPath}/visualize?type=searching&algo=Binary+Search" class="list-group-item list-group-item-action bg-transparent text-light border-secondary d-flex justify-content-between align-items-center">
                            <div>
                                <strong>Binary Search</strong> <span class="badge bg-secondary ms-2">O(log n)</span>
                                <div class="small text-muted">Logarithmic lookup on sorted dataset</div>
                            </div>
                            <i class="bi bi-chevron-right text-info"></i>
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card card-custom p-4 h-100">
                    <h5 class="fw-bold text-white mb-2"><i class="bi bi-file-earmark-pdf-fill text-danger me-2"></i>Download Algorithm Execution Report</h5>
                    <p class="text-muted small mb-3">Generate and download a comprehensive PDF report with theoretical Big-O metrics, runtime complexity, and step analysis for any Sorting, Searching, or Graph algorithm.</p>
                    <form action="${pageContext.request.contextPath}/report" method="post" class="row g-2 align-items-center">
                        <div class="col-sm-8">
                            <label class="form-label text-info small fw-bold mb-1">SELECT ANY ALGORITHM</label>
                            <select name="algoName" class="form-select bg-dark text-light border-secondary">
                                <optgroup label="Sorting Algorithms">
                                    <option value="Bubble Sort">Bubble Sort</option>
                                    <option value="Selection Sort">Selection Sort</option>
                                    <option value="Insertion Sort">Insertion Sort</option>
                                    <option value="Merge Sort">Merge Sort</option>
                                    <option value="Quick Sort">Quick Sort</option>
                                    <option value="Heap Sort">Heap Sort</option>
                                </optgroup>
                                <optgroup label="Searching Algorithms">
                                    <option value="Linear Search">Linear Search</option>
                                    <option value="Binary Search">Binary Search</option>
                                    <option value="Jump Search">Jump Search</option>
                                </optgroup>
                                <optgroup label="Graph Algorithms">
                                    <option value="Breadth-First Search (BFS)">Breadth-First Search (BFS)</option>
                                    <option value="Depth-First Search (DFS)">Depth-First Search (DFS)</option>
                                    <option value="Dijkstra Shortest Path">Dijkstra Shortest Path</option>
                                    <option value="Prim Minimum Spanning Tree">Prim Minimum Spanning Tree</option>
                                    <option value="Kruskal MST">Kruskal MST</option>
                                </optgroup>
                            </select>
                        </div>
                        <div class="col-sm-4 text-end">
                            <label class="form-label d-block mb-1">&nbsp;</label>
                            <button type="submit" class="btn btn-outline-danger w-100"><i class="bi bi-download me-1"></i>Download PDF</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Chart.js & Dashboard Script -->
    <script>
        <% 
        List<UserProgress> pList = (List<UserProgress>) request.getAttribute("userProgress");
        StringBuilder pLabels = new StringBuilder();
        StringBuilder pCounts = new StringBuilder();
        if (pList != null && !pList.isEmpty()) {
            for (int i = 0; i < pList.size(); i++) {
                UserProgress up = pList.get(i);
                if (i > 0) { pLabels.append(", "); pCounts.append(", "); }
                pLabels.append("'").append(up.getTopic()).append("'");
                pCounts.append(up.getCompletedCount());
            }
        }
        %>
        const progressLabels = [<%= pLabels.toString() %>];
        const progressCounts = [<%= pCounts.toString() %>];
    </script>
    <script src="${pageContext.request.contextPath}/js/dashboard.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
