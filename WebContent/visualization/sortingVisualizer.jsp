<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Sorting Algorithm Visualizer - AlgoVision Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <script>window.contextPath = '${pageContext.request.contextPath}';</script>
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
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/algorithms">Algorithms</a></li>
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/visualize?type=sorting">Visualizer</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/datastructures">Data Structures</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/performance">Performance</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/quiz">Quizzes</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/challenges">Challenges</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Visualizer Workspace -->
    <div class="container py-4">

        <!-- Category Navigation Switcher -->
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 gap-3">
            <div>
                <h2 class="fw-bold text-white mb-1"><i class="bi bi-bar-chart-steps text-info me-2"></i>Sorting Algorithm Visualizer</h2>
                <p class="text-muted mb-0">Showing sorting options only. Select any sorting algorithm to visualize step-by-step execution.</p>
            </div>
            <div class="btn-group p-1 bg-dark rounded border border-secondary shadow-sm" role="group" aria-label="Visualizer Category Switcher">
                <a href="${pageContext.request.contextPath}/visualize?type=sorting" class="btn btn-sm px-3 py-2 btn-primary-custom text-white active">
                    <i class="bi bi-bar-chart-steps me-1"></i> Sorting
                </a>
                <a href="${pageContext.request.contextPath}/visualize?type=searching" class="btn btn-sm px-3 py-2 btn-outline-custom">
                    <i class="bi bi-search me-1"></i> Searching
                </a>
                <a href="${pageContext.request.contextPath}/visualize?type=graph" class="btn btn-sm px-3 py-2 btn-outline-custom">
                    <i class="bi bi-diagram-3 me-1"></i> Graph
                </a>
            </div>
        </div>

        <!-- Top Configuration Bar -->
        <div class="card card-custom p-4 mb-4">
            <form id="visualizerForm" class="row g-3 align-items-center">
                <input type="hidden" id="categoryInput" value="Sorting">
                <div class="col-md-3">
                    <label class="form-label text-info small fw-bold">SELECT SORTING ALGORITHM</label>
                    <select id="algorithmSelect" class="form-select bg-dark text-light border-secondary">
                        <option value="Bubble Sort" ${"Bubble Sort".equalsIgnoreCase(selectedAlgo) ? "selected" : ""}>Bubble Sort</option>
                        <option value="Selection Sort" ${"Selection Sort".equalsIgnoreCase(selectedAlgo) ? "selected" : ""}>Selection Sort</option>
                        <option value="Insertion Sort" ${"Insertion Sort".equalsIgnoreCase(selectedAlgo) ? "selected" : ""}>Insertion Sort</option>
                        <option value="Merge Sort" ${"Merge Sort".equalsIgnoreCase(selectedAlgo) ? "selected" : ""}>Merge Sort</option>
                        <option value="Quick Sort" ${"Quick Sort".equalsIgnoreCase(selectedAlgo) ? "selected" : ""}>Quick Sort</option>
                        <option value="Heap Sort" ${"Heap Sort".equalsIgnoreCase(selectedAlgo) ? "selected" : ""}>Heap Sort</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label text-info small fw-bold">CUSTOM INPUT DATA (COMMA SEPARATED)</label>
                    <input type="text" id="inputData" class="form-control bg-dark text-light border-secondary" value="5, 2, 8, 1, 3, 9, 4, 7">
                </div>
                <div class="col-md-3">
                    <label class="form-label text-info small fw-bold">ANIMATION SPEED</label>
                    <input type="range" class="form-range" id="speedRange" min="50" max="1000" step="50" value="300" onchange="engine.setSpeed(1050 - this.value)">
                </div>
                <div class="col-md-2 text-end">
                    <label class="form-label d-block">&nbsp;</label>
                    <button type="submit" class="btn btn-primary-custom w-100"><i class="bi bi-arrow-repeat me-1"></i>Generate</button>
                </div>
            </form>
        </div>

        <!-- Metrics Header -->
        <div class="row g-3 mb-3 text-center">
            <div class="col-md-3">
                <div class="p-2 border border-secondary rounded bg-dark">
                    <span class="text-muted small d-block">COMPARISONS</span>
                    <strong id="comparisonCount" class="text-warning h4">0</strong>
                </div>
            </div>
            <div class="col-md-3">
                <div class="p-2 border border-secondary rounded bg-dark">
                    <span class="text-muted small d-block">SWAPS / MOVES</span>
                    <strong id="swapCount" class="text-danger h4">0</strong>
                </div>
            </div>
            <div class="col-md-3">
                <div class="p-2 border border-secondary rounded bg-dark">
                    <span class="text-muted small d-block">CURRENT STEP</span>
                    <strong id="stepCounter" class="text-info h4">0 / 0</strong>
                </div>
            </div>
            <div class="col-md-3">
                <div class="p-2 border border-secondary rounded bg-dark">
                    <span class="text-muted small d-block">STATUS</span>
                    <strong id="visualStatus" class="text-success h4">Ready</strong>
                </div>
            </div>
        </div>

        <!-- Array Visualizer Bar Canvas -->
        <div class="visualizer-container mb-4" id="arrayContainer">
            <!-- Dynamic Array Bars Rendered via visualization.js -->
        </div>

        <!-- Final Sorted Result Banner -->
        <div id="finalResultBanner" class="alert alert-success border border-success d-none align-items-center justify-content-between mb-4">
            <div class="d-flex align-items-center gap-2">
                <i class="bi bi-check-circle-fill text-success fs-4"></i>
                <div>
                    <strong class="d-block text-white">Array Fully Sorted!</strong>
                    <span id="finalResultText" class="text-light small">Result: [ ]</span>
                </div>
            </div>
            <span class="badge bg-success px-3 py-2">Sorted Result</span>
        </div>

        <!-- Current Step Explanation -->
        <div class="alert alert-dark border border-secondary d-flex align-items-center gap-3 mb-4">
            <i class="bi bi-info-circle-fill text-info h4 mb-0"></i>
            <div id="stepDescription" class="fw-semibold text-light">Click Play to begin step-by-step sorting algorithm animation.</div>
        </div>

        <!-- Playback Controls -->
        <div class="card card-custom p-3 text-center mb-4">
            <div class="d-flex justify-content-center align-items-center gap-3">
                <button class="btn btn-outline-secondary" onclick="engine.prev()"><i class="bi bi-skip-backward-fill"></i> Prev Step</button>
                <button class="btn btn-success px-4" onclick="engine.play()"><i class="bi bi-play-fill me-1"></i> Start / Resume</button>
                <button class="btn btn-warning px-4" onclick="engine.pause()"><i class="bi bi-pause-fill me-1"></i> Pause</button>
                <button class="btn btn-outline-secondary" onclick="engine.next()"><i class="bi bi-skip-forward-fill"></i> Next Step</button>
                <button class="btn btn-outline-danger" onclick="engine.reset()"><i class="bi bi-arrow-counterclockwise"></i> Reset</button>
            </div>
        </div>

        <!-- Real-time Step Log Console -->
        <div class="card card-custom p-3">
            <h6 class="fw-bold text-white mb-2"><i class="bi bi-terminal-fill text-info me-2"></i>Execution Step Console Log</h6>
            <div id="logContainer" style="height: 140px; overflow-y: auto; font-family: monospace;" class="bg-dark p-2 rounded border border-secondary">
            </div>
        </div>

    </div>

    <script src="${pageContext.request.contextPath}/js/visualization.js"></script>
    <script src="${pageContext.request.contextPath}/js/sorting.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
