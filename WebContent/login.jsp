<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login - AlgoVision Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body class="d-flex align-items-center justify-content-center py-5">
    <div class="container" style="max-width: 420px;">
        <div class="card card-custom p-4">
            <div class="text-center mb-4">
                <a href="${pageContext.request.contextPath}/" class="h3 fw-bold text-decoration-none text-info">
                    <i class="bi bi-diagram-3-fill"></i> AlgoVision Pro
                </a>
                <p class="text-muted small mt-2">Sign in to access your student dashboard</p>
            </div>

            <% String err = (String) request.getAttribute("errorMessage");
               if (err != null) { %>
                <div class="alert alert-danger py-2 small" role="alert"><%= err %></div>
            <% } %>
            <% String successMsg = (String) request.getAttribute("successMessage");
               if (successMsg == null && "true".equals(request.getParameter("registered"))) {
                   successMsg = "Registration successful! You can now log in with your email and password.";
               }
               if (successMsg != null) { %>
                <div class="alert alert-success py-2 small" role="alert"><%= successMsg %></div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="mb-3">
                    <label class="form-label text-info small fw-bold">EMAIL ADDRESS</label>
                    <input type="email" name="email" class="form-control bg-dark text-light border-secondary" placeholder="student@algovision.com" required>
                </div>
                <div class="mb-4">
                    <label class="form-label text-info small fw-bold">PASSWORD</label>
                    <input type="password" name="password" class="form-control bg-dark text-light border-secondary" placeholder="••••••••" required>
                </div>
                <button type="submit" class="btn btn-primary-custom w-100 mb-3">Sign In</button>
            </form>

            <div class="text-center mt-3">
                <p class="text-muted small">Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp" class="text-info text-decoration-none">Register here</a></p>
            </div>
        </div>
    </div>
</body>
</html>
