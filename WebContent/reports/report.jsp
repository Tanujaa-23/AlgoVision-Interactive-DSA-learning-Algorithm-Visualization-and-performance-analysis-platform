<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Generate PDF Execution Report - AlgoVision Pro</title>
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
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-4" style="max-width: 650px;">
        <div class="card card-custom p-4">
            <h3 class="fw-bold text-white mb-2"><i class="bi bi-file-earmark-pdf-fill text-danger me-2"></i>Generate PDF Execution Report</h3>
            <p class="text-muted small mb-4">Export step-by-step algorithm performance, Big-O metrics, and comparisons to a formatted PDF document.</p>

            <form action="${pageContext.request.contextPath}/report" method="post">
                <div class="mb-3">
                    <label class="form-label text-info small fw-bold">ALGORITHM NAME</label>
                    <input type="text" name="algoName" class="form-control bg-dark text-light border-secondary" value="${selectedAlgo != null ? selectedAlgo : 'Bubble Sort'}" required>
                </div>
                <div class="mb-3">
                    <label class="form-label text-info small fw-bold">INPUT DATA</label>
                    <input type="text" name="inputData" class="form-control bg-dark text-light border-secondary" value="5, 2, 8, 1, 3" required>
                </div>
                <div class="mb-3">
                    <label class="form-label text-info small fw-bold">SORTED / OUTPUT RESULT</label>
                    <input type="text" name="outputData" class="form-control bg-dark text-light border-secondary" value="1, 2, 3, 5, 8" required>
                </div>
                <div class="row g-3 mb-4">
                    <div class="col-md-4">
                        <label class="form-label text-info small fw-bold">STEPS</label>
                        <input type="number" name="stepCount" class="form-control bg-dark text-light border-secondary" value="12">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label text-info small fw-bold">COMPARISONS</label>
                        <input type="number" name="comparisons" class="form-control bg-dark text-light border-secondary" value="10">
                    </div>
                    <div class="col-md-4">
                        <label class="form-label text-info small fw-bold">SWAPS</label>
                        <input type="number" name="swaps" class="form-control bg-dark text-light border-secondary" value="4">
                    </div>
                </div>
                <button type="submit" class="btn btn-primary-custom w-100"><i class="bi bi-download me-2"></i>Generate & Download PDF</button>
            </form>
        </div>
    </div>

</body>
</html>
