<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Data Structure Visualizer - AlgoVision Pro</title>
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
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/datastructures">Data Structures</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-4">
        <!-- DS Selector Tabs -->
        <div class="card card-custom p-3 mb-4">
            <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
                <div class="btn-group">
                    <button class="btn btn-outline-custom ${'stack'.equals(selectedDS) ? 'active' : ''}" onclick="selectDS('stack')"><i class="bi bi-layers-fill me-1"></i>Stack</button>
                    <button class="btn btn-outline-custom ${'queue'.equals(selectedDS) ? 'active' : ''}" onclick="selectDS('queue')"><i class="bi bi-collection-fill me-1"></i>Queue</button>
                    <button class="btn btn-outline-custom ${'linkedlist'.equals(selectedDS) ? 'active' : ''}" onclick="selectDS('linkedlist')"><i class="bi bi-link-45deg me-1"></i>Linked List</button>
                    <button class="btn btn-outline-custom ${'heap'.equals(selectedDS) ? 'active' : ''}" onclick="selectDS('heap')"><i class="bi bi-diagram-2-fill me-1"></i>Binary Max Heap</button>
                </div>
                <div class="d-flex gap-2">
                    <input type="number" id="dsValue" class="form-control bg-dark text-light border-secondary" style="width: 120px;" value="42" placeholder="Value">
                    <button id="opBtn1" class="btn btn-primary-custom" onclick="performCurrentDS('push')">Push / Add</button>
                    <button id="opBtn2" class="btn btn-warning" onclick="performCurrentDS('pop')">Pop / Remove</button>
                    <button id="opBtn3" class="btn btn-info" onclick="performCurrentDS('peek')">Peek</button>
                </div>
            </div>
        </div>

        <!-- Status Alert -->
        <div class="alert alert-dark border border-secondary text-light fw-bold mb-4" id="dsStatusMsg">
            Interactive Data Structure Workspace Ready. Perform Operations Above.
        </div>

        <!-- Visual Render Box -->
        <div class="card card-custom p-4 min-vh-50 d-flex justify-content-center align-items-center" id="dsVisualContainer" style="min-height: 320px;">
            <p class="text-muted">Perform Push / Enqueue operations above to populate visualizer.</p>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/datastructures.js"></script>
    <script>
        let currentDS = "${selectedDS != null ? selectedDS : 'stack'}";

        function selectDS(ds) {
            currentDS = ds;
            window.location.href = "${pageContext.request.contextPath}/datastructures?ds=" + ds;
        }

        function performCurrentDS(op) {
            if (currentDS === "queue") {
                if (op === "push") op = "enqueue";
                if (op === "pop") op = "dequeue";
            } else if (currentDS === "linkedlist") {
                if (op === "push") op = "insert";
                if (op === "pop") op = "delete";
            } else if (currentDS === "heap") {
                if (op === "push") op = "insert";
                if (op === "pop") op = "extractMax";
            }
            performDSOperation(currentDS, op);
        }
    </script>
</body>
</html>
