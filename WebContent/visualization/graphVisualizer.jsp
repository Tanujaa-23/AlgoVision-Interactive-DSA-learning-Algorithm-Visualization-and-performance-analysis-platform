<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Graph Algorithm Visualizer - AlgoVision Pro</title>
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
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/visualize?type=graph">Visualizer</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/datastructures">Data Structures</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/performance">Performance</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/quiz">Quizzes</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/challenges">Challenges</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-4">

        <!-- Category Navigation Switcher -->
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 gap-3">
            <div>
                <h2 class="fw-bold text-white mb-1"><i class="bi bi-diagram-3 text-info me-2"></i>Graph Algorithm Visualizer</h2>
                <p class="text-muted mb-0">Showing graph options only. Select any graph algorithm to observe traversals, shortest paths, and spanning trees.</p>
            </div>
            <div class="btn-group p-1 bg-dark rounded border border-secondary shadow-sm" role="group" aria-label="Visualizer Category Switcher">
                <a href="${pageContext.request.contextPath}/visualize?type=sorting" class="btn btn-sm px-3 py-2 btn-outline-custom">
                    <i class="bi bi-bar-chart-steps me-1"></i> Sorting
                </a>
                <a href="${pageContext.request.contextPath}/visualize?type=searching" class="btn btn-sm px-3 py-2 btn-outline-custom">
                    <i class="bi bi-search me-1"></i> Searching
                </a>
                <a href="${pageContext.request.contextPath}/visualize?type=graph" class="btn btn-sm px-3 py-2 btn-primary-custom text-white active">
                    <i class="bi bi-diagram-3 me-1"></i> Graph
                </a>
            </div>
        </div>

        <!-- Top Configuration Bar -->
        <div class="card card-custom p-4 mb-4">
            <form id="graphForm" class="row g-3 align-items-center" onsubmit="event.preventDefault(); runGraphAlgo(true);">
                <div class="col-md-3">
                    <label class="form-label text-info small fw-bold">SELECT GRAPH ALGORITHM</label>
                    <select id="graphAlgoSelect" class="form-select bg-dark text-light border-secondary">
                        <option value="BFS" ${empty selectedAlgo || "BFS".equalsIgnoreCase(selectedAlgo) || selectedAlgo.toLowerCase().contains("breadth") ? "selected" : ""}>Breadth-First Search (BFS)</option>
                        <option value="DFS" ${"DFS".equalsIgnoreCase(selectedAlgo) || selectedAlgo.toLowerCase().contains("depth") ? "selected" : ""}>Depth-First Search (DFS)</option>
                        <option value="Dijkstra" ${"Dijkstra".equalsIgnoreCase(selectedAlgo) || selectedAlgo.toLowerCase().contains("dijkstra") || selectedAlgo.toLowerCase().contains("shortest") ? "selected" : ""}>Dijkstra Shortest Path</option>
                        <option value="Kruskal" ${"Kruskal".equalsIgnoreCase(selectedAlgo) || selectedAlgo.toLowerCase().contains("kruskal") ? "selected" : ""}>Kruskal's MST</option>
                    </select>
                </div>
                <div class="col-md-4">
                    <label class="form-label text-info small fw-bold">GRAPH NODES / NUMBERS</label>
                    <input type="text" id="graphNodesInput" class="form-control bg-dark text-light border-secondary" value="1, 2, 3, 4, 5, 6, 7, 8, 9" placeholder="e.g. 1, 2, 3, 4, 5, 6, 7, 8, 9">
                </div>
                <div class="col-md-2">
                    <label class="form-label text-info small fw-bold">SOURCE ROOT</label>
                    <select id="startNodeSelect" class="form-select bg-dark text-light border-secondary">
                        <option value="1" selected>Node 1 (Source)</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <label class="form-label text-info small fw-bold">ANIMATION SPEED</label>
                    <input type="range" class="form-range" id="graphSpeedRange" min="100" max="1500" step="50" value="500" onchange="if(graphVis) graphVis.setSpeed(1600 - this.value)">
                </div>
                <div class="col-md-1 text-end">
                    <label class="form-label d-block">&nbsp;</label>
                    <button type="submit" class="btn btn-primary-custom w-100" title="Run Graph Algorithm"><i class="bi bi-play-circle-fill"></i></button>
                </div>
            </form>
        </div>

        <!-- Metrics Header -->
        <div class="row g-3 mb-3 text-center">
            <div class="col-md-3">
                <div class="p-2 border border-secondary rounded bg-dark">
                    <span class="text-muted small d-block">ACTIVE NODE</span>
                    <strong id="currentNodeDisplay" class="text-warning h4">-</strong>
                </div>
            </div>
            <div class="col-md-3">
                <div class="p-2 border border-secondary rounded bg-dark">
                    <span class="text-muted small d-block">TOTAL DISTANCE / WEIGHT</span>
                    <strong id="totalDistance" class="text-danger h4">0.0</strong>
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

        <!-- Graph Final Result Banner -->
        <div id="finalResultBanner" class="alert alert-success border border-success d-none align-items-center justify-content-between mb-4">
            <div class="d-flex align-items-center gap-2">
                <i class="bi bi-diagram-3-fill text-success fs-4"></i>
                <div>
                    <strong id="finalResultTitle" class="d-block text-white">Graph Execution Completed</strong>
                    <span id="finalResultText" class="text-light small">Result details</span>
                </div>
            </div>
            <span id="finalResultBadge" class="badge bg-success px-3 py-2">Result</span>
        </div>

        <div class="row g-4 mb-4">
            <div class="col-lg-8">
                <div class="card card-custom p-3 text-center" style="background: #090d16;">
                    <h6 class="fw-bold text-white mb-2"><i class="bi bi-diagram-3 text-info me-2"></i>Interactive Canvas Graph</h6>
                    <canvas id="graphCanvas" width="620" height="320" style="border: 1px dashed #334155; border-radius: 8px; max-width: 100%;"></canvas>
                </div>
            </div>
            <div class="col-lg-4">
                <div class="card card-custom p-3 h-100 d-flex flex-column">
                    <h6 class="fw-bold text-white mb-3"><i class="bi bi-info-circle text-warning me-2"></i>Traversal Step Explanation</h6>
                    <div id="stepDescription" class="p-3 bg-dark rounded border border-secondary text-light small mb-3 flex-grow-1" style="min-height: 120px;">
                        Loading graph algorithm execution steps...
                    </div>
                    <div class="border-top border-secondary pt-3">
                        <span class="small text-muted d-block mb-2"><strong>Legend:</strong> Yellow: Active node/edge | Purple: Visited | Blue: Unvisited | Green: Path/MST</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- Playback Controls -->
        <div class="card card-custom p-3 text-center mb-4">
            <div class="d-flex justify-content-center align-items-center gap-3">
                <button class="btn btn-outline-secondary" onclick="if(graphVis) graphVis.prev()"><i class="bi bi-skip-backward-fill"></i> Prev Step</button>
                <button class="btn btn-success px-4" onclick="if(graphVis) graphVis.play()"><i class="bi bi-play-fill me-1"></i> Start / Resume</button>
                <button class="btn btn-warning px-4" onclick="if(graphVis) graphVis.pause()"><i class="bi bi-pause-fill me-1"></i> Pause</button>
                <button class="btn btn-outline-secondary" onclick="if(graphVis) graphVis.next()"><i class="bi bi-skip-forward-fill"></i> Next Step</button>
                <button class="btn btn-outline-danger" onclick="if(graphVis) graphVis.reset()"><i class="bi bi-arrow-counterclockwise"></i> Reset</button>
            </div>
        </div>

        <!-- Real-time Step Log Console -->
        <div class="card card-custom p-3">
            <h6 class="fw-bold text-white mb-2"><i class="bi bi-terminal-fill text-info me-2"></i>Execution Step Console Log</h6>
            <div id="logContainer" style="height: 140px; overflow-y: auto; font-family: monospace;" class="bg-dark p-2 rounded border border-secondary">
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/graph.js"></script>
    <script>
        let graphVis = null;

        function getParsedNodes() {
            const inputEl = document.getElementById("graphNodesInput");
            const val = inputEl ? inputEl.value.trim() : "";
            if (!val) return ["1", "2", "3", "4", "5", "6", "7", "8", "9"];
            const tokens = val.split(/[,\s]+/).map(t => t.trim().toUpperCase()).filter(t => t.length > 0);
            const unique = [];
            tokens.forEach(t => { if (!unique.includes(t)) unique.push(t); });
            return unique.length > 0 ? unique : ["1", "2", "3", "4", "5", "6", "7", "8", "9"];
        }

        function updateStartNodeOptions() {
            const startSelect = document.getElementById("startNodeSelect");
            if (!startSelect) return;
            const currentSelected = startSelect.value;
            const nodes = getParsedNodes();

            startSelect.innerHTML = "";
            nodes.forEach((node, idx) => {
                const opt = document.createElement("option");
                opt.value = node;
                opt.innerText = "Node " + node + (idx === 0 ? " (Source)" : "");
                if (node === currentSelected || (idx === 0 && !nodes.includes(currentSelected))) {
                    opt.selected = true;
                }
                startSelect.appendChild(opt);
            });
        }

        document.addEventListener("DOMContentLoaded", () => {
            graphVis = new GraphCanvasVisualizer("graphCanvas", "logContainer");

            updateStartNodeOptions();

            const nodesInput = document.getElementById("graphNodesInput");
            if (nodesInput) {
                nodesInput.addEventListener("change", () => {
                    updateStartNodeOptions();
                    runGraphAlgo(false);
                });
            }

            const algoSelect = document.getElementById("graphAlgoSelect");
            if (algoSelect) {
                algoSelect.addEventListener("change", () => runGraphAlgo(false));
            }

            const startNodeSelect = document.getElementById("startNodeSelect");
            if (startNodeSelect) {
                startNodeSelect.addEventListener("change", () => runGraphAlgo(false));
            }

            // Immediately run and solve the selected graph algorithm on page load (auto-plays if ?run=true)
            runGraphAlgo();
        });

        function runGraphAlgo(shouldAutoPlay = false) {
            const algoSelect = document.getElementById("graphAlgoSelect");
            if (!algoSelect) return;
            const algo = algoSelect.value;
            const nodes = getParsedNodes();
            const startSelect = document.getElementById("startNodeSelect");
            const start = (startSelect && startSelect.value) ? startSelect.value : nodes[0];

            // Rebuild canvas structure with user's nodes
            if (graphVis) {
                graphVis.buildGraphFromNodes(nodes);
            }

            // Hide previous result banner until completion
            const banner = document.getElementById("finalResultBanner");
            if (banner) banner.classList.add("d-none");

            const params = new URLSearchParams();
            params.append("category", "Graph");
            params.append("algorithm", algo);
            params.append("startNode", start);
            params.append("inputData", nodes.join(", "));

            const contextPath = window.contextPath || "";
            const endpoint = contextPath ? (contextPath + "/visualize") : "visualize";

            fetch(endpoint, {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: params.toString()
            })
            .then(res => res.json())
            .then(steps => {
                if (graphVis) {
                    graphVis.setSteps(steps, algo);
                    const urlParams = new URLSearchParams(window.location.search);
                    const isRunRequested = shouldAutoPlay || urlParams.get("run") === "true";
                    if (isRunRequested) {
                        setTimeout(() => {
                            if (graphVis && !graphVis.isPlaying) {
                                graphVis.play();
                            }
                        }, 350);
                    }
                }
            })
            .catch(err => {
                console.warn("Backend fetch failed, running client-side graph solver:", err);
                if (graphVis) {
                    const localSteps = graphVis.generateLocalSteps(algo, start, nodes);
                    graphVis.setSteps(localSteps, algo);
                    if (shouldAutoPlay) {
                        setTimeout(() => graphVis.play(), 300);
                    }
                }
            });
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
