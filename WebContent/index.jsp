<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AlgoVision Pro - Interactive DSA Learning & Visualization Platform</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Custom Theme CSS -->
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
</head>
<body>

    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg navbar-dark navbar-custom sticky-top">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/">
                <i class="bi bi-diagram-3-fill text-info"></i> AlgoVision Pro
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navMenu">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navMenu">
                <ul class="navbar-nav ms-auto align-items-center gap-3">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/algorithms">Algorithms</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/visualize">Visualizer</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/performance">Performance</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/quiz">Quizzes</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/challenges">Challenges</a></li>
                    <li class="nav-item"><a class="btn btn-outline-custom btn-sm" href="${pageContext.request.contextPath}/login.jsp">Login</a></li>
                    <li class="nav-item"><a class="btn btn-primary-custom btn-sm" href="${pageContext.request.contextPath}/register.jsp">Get Started</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero-section text-center text-light">
        <div class="container">
            <span class="badge bg-indigo px-3 py-2 rounded-pill mb-3 text-uppercase tracking-wider border border-primary">Interactive DSA Learning</span>
            <h1 class="hero-title mb-4">Learn. Visualize.<br>Practice. Master DSA.</h1>
            <p class="lead text-muted max-w-2xl mx-auto mb-5">
                AlgoVision Pro provides real-time step-by-step interactive visual engine, performance comparisons, quizzes, coding challenges, and AI recommendations for Data Structures and Algorithms.
            </p>
            <div class="d-flex justify-content-center gap-3">
                <a href="${pageContext.request.contextPath}/visualize" class="btn btn-primary-custom btn-lg"><i class="bi bi-play-circle-fill me-2"></i>Explore Visualizer</a>
                <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-outline-custom btn-lg"><i class="bi bi-person-plus-fill me-2"></i>Register Now</a>
            </div>
        </div>
    </section>

    <!-- Core Features Section -->
    <section class="py-5">
        <div class="container">
            <div class="text-center mb-5">
                <h2 class="fw-bold text-white">Platform Core Capabilities</h2>
                <p class="text-muted">Designed for Information Technology students and DSA enthusiasts</p>
            </div>
            <div class="row g-4">
                <div class="col-md-4">
                    <div class="card card-custom h-100 p-4">
                        <i class="bi bi-bar-chart-steps text-info display-5 mb-3"></i>
                        <h4 class="fw-bold text-white">Step-by-Step Visualization</h4>
                        <p class="text-muted small">Watch comparison highlights, swap animations, custom inputs, and dynamic step counters for 14+ fundamental algorithms.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card card-custom h-100 p-4">
                        <i class="bi bi-speedometer2 text-warning display-5 mb-3"></i>
                        <h4 class="fw-bold text-white">Performance Benchmarking</h4>
                        <p class="text-muted small">Compare execution time, comparison counts, and memory overhead across input sizes up to 10,000 using Chart.js graphs.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card card-custom h-100 p-4">
                        <i class="bi bi-robot text-primary display-5 mb-3"></i>
                        <h4 class="fw-bold text-white">Smart Recommendation</h4>
                        <p class="text-muted small">Enter your problem statement or graph characteristics and let the intelligent rule engine recommend optimal algorithms.</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Footer -->
    <footer class="text-center">
        <div class="container">
            <p class="mb-0 small">&copy; 2026 AlgoVision Pro. Built with Core Java, Servlets, JSP, JDBC & PostgreSQL.</p>
        </div>
    </footer>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
