<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register - AlgoVision Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body class="d-flex align-items-center justify-content-center py-5">
    <div class="container" style="max-width: 480px;">
        <div class="card card-custom p-4">
            <div class="text-center mb-4">
                <a href="${pageContext.request.contextPath}/" class="h3 fw-bold text-decoration-none text-info">
                    <i class="bi bi-diagram-3-fill"></i> AlgoVision Pro
                </a>
                <p class="text-muted small mt-2">Create your student or instructor account</p>
            </div>

            <% String err = (String) request.getAttribute("errorMessage");
               if (err != null) { %>
                <div class="alert alert-danger py-2 small" role="alert"><%= err %></div>
            <% } %>

            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="mb-3">
                    <label class="form-label text-info small fw-bold">FULL NAME</label>
                    <input type="text" name="name" class="form-control bg-dark text-light border-secondary" placeholder="Alex Student" required>
                </div>
                <div class="mb-3">
                    <label class="form-label text-info small fw-bold">EMAIL ADDRESS</label>
                    <input type="email" name="email" class="form-control bg-dark text-light border-secondary" placeholder="student@algovision.com" required>
                </div>
                <div class="mb-3">
                    <label class="form-label text-info small fw-bold">ROLE</label>
                    <select name="role" class="form-select bg-dark text-light border-secondary">
                        <option value="STUDENT" selected>Student</option>
                        <option value="ADMIN">Admin / Instructor</option>
                    </select>
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label text-info small fw-bold">PASSWORD</label>
                        <input type="password" name="password" class="form-control bg-dark text-light border-secondary" placeholder="••••••••" required>
                    </div>
                    <div class="col-md-6 mb-4">
                        <label class="form-label text-info small fw-bold">CONFIRM PASSWORD</label>
                        <input type="password" name="confirmPassword" class="form-control bg-dark text-light border-secondary" placeholder="••••••••" required>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary-custom w-100 mb-3">Create Account</button>
            </form>

            <div class="text-center mt-3">
                <p class="text-muted small">Already have an account? <a href="${pageContext.request.contextPath}/login.jsp" class="text-info text-decoration-none">Sign in here</a></p>
            </div>
        </div>
    </div>
</body>
</html>
