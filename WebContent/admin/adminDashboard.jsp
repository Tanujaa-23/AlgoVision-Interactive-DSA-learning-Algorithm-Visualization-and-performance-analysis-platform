<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.algovision.model.User, com.algovision.model.Algorithm, java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - AlgoVision Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark navbar-custom sticky-top">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/admin">
                <i class="bi bi-shield-lock-fill text-warning"></i> AlgoVision Admin
            </a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/admin">Control Panel</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/dashboard">Student View</a></li>
                </ul>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm">Logout</a>
            </div>
        </div>
    </nav>

    <div class="container py-4">
        <h2 class="fw-bold text-white mb-4">Platform Administration</h2>

        <!-- Stats Overview -->
        <div class="row g-3 mb-4 text-center">
            <div class="col-md-3">
                <div class="stat-box">
                    <div class="stat-value text-info">${totalUsers != null ? totalUsers : 2}</div>
                    <div class="stat-label">Registered Users</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-box">
                    <div class="stat-value text-success">${totalAlgorithms != null ? totalAlgorithms : 14}</div>
                    <div class="stat-label">Active Algorithms</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-box">
                    <div class="stat-value text-warning">${totalExecutions != null ? totalExecutions : 48}</div>
                    <div class="stat-label">Total Executions</div>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-box">
                    <div class="stat-value text-danger">${totalChallenges != null ? totalChallenges : 4}</div>
                    <div class="stat-label">Challenges</div>
                </div>
            </div>
        </div>

        <!-- Add Algorithm Form Modal & Management Tables -->
        <div class="row g-4 mb-4">
            <div class="col-md-5">
                <div class="card card-custom p-4">
                    <h5 class="fw-bold text-white mb-3"><i class="bi bi-plus-circle-fill text-success me-2"></i>Add New Algorithm</h5>
                    <form action="${pageContext.request.contextPath}/admin" method="post">
                        <input type="hidden" name="action" value="addAlgo">
                        <div class="mb-2">
                            <input type="text" name="name" class="form-control bg-dark text-light border-secondary" placeholder="Algorithm Name" required>
                        </div>
                        <div class="mb-2">
                            <select name="category" class="form-select bg-dark text-light border-secondary">
                                <option value="Sorting">Sorting</option>
                                <option value="Searching">Searching</option>
                                <option value="Graph">Graph</option>
                                <option value="DataStructure">Data Structure</option>
                            </select>
                        </div>
                        <div class="mb-2">
                            <textarea name="description" class="form-control bg-dark text-light border-secondary" rows="2" placeholder="Description"></textarea>
                        </div>
                        <div class="row g-2 mb-2">
                            <div class="col-6"><input type="text" name="complexityBest" class="form-control bg-dark text-light border-secondary" placeholder="Best O(n)"></div>
                            <div class="col-6"><input type="text" name="complexityAvg" class="form-control bg-dark text-light border-secondary" placeholder="Avg O(n log n)"></div>
                        </div>
                        <button type="submit" class="btn btn-primary-custom w-100">Save Algorithm</button>
                    </form>
                </div>
            </div>

            <!-- Users List Table -->
            <div class="col-md-7">
                <div class="card card-custom p-4">
                    <h5 class="fw-bold text-white mb-3"><i class="bi bi-people-fill text-info me-2"></i>Registered Platform Users</h5>
                    <div class="table-responsive">
                        <table class="table table-dark table-striped align-middle mb-0">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Email</th>
                                    <th>Role</th>
                                    <th>Streak</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% List<User> uList = (List<User>) request.getAttribute("userList");
                                   if (uList != null) {
                                       for (User u : uList) { %>
                                        <tr>
                                            <td><%= u.getId() %></td>
                                            <td><strong><%= u.getName() %></strong></td>
                                            <td class="small text-muted"><%= u.getEmail() %></td>
                                            <td><span class="badge <%= u.isAdmin() ? "bg-danger" : "bg-primary" %>"><%= u.getRole() %></span></td>
                                            <td><span class="text-warning"><%= u.getStreakDays() %> days</span></td>
                                        </tr>
                                <% } } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>
