/**
 * Graph Canvas Visualizer - AlgoVision Pro
 * Dynamically supports user-defined nodes and numbers (e.g. 1, 2, 3, 4, 5, 6, 7, 8, 9)
 */

class GraphCanvasVisualizer {
    constructor(canvasId, logId = "logContainer") {
        this.canvas = document.getElementById(canvasId);
        this.logContainer = document.getElementById(logId);
        if (!this.canvas) return;
        this.ctx = this.canvas.getContext("2d");

        this.nodes = {};
        this.edges = [];
        this.steps = [];
        this.currentStepIdx = 0;
        this.isPlaying = false;
        this.speed = 500; // ms per step
        this.timer = null;
        this.currentAlgo = "BFS";

        // Build default 1..9 node graph
        this.buildGraphFromNodes(["1", "2", "3", "4", "5", "6", "7", "8", "9"]);
    }

    buildGraphFromNodes(nodeList) {
        if (!nodeList || nodeList.length === 0) {
            nodeList = ["1", "2", "3", "4", "5", "6", "7", "8", "9"];
        }

        this.nodes = {};
        this.edges = [];

        const N = nodeList.length;
        const width = this.canvas.width || 620;
        const height = this.canvas.height || 320;
        const cx = width / 2;
        const cy = height / 2;

        // Spread nodes evenly on an ellipse
        const rx = Math.max(80, Math.min(235, width * 0.40));
        const ry = Math.max(60, Math.min(115, height * 0.38));

        nodeList.forEach((key, i) => {
            const angle = (2 * Math.PI * i) / N - Math.PI / 2;
            this.nodes[key] = {
                x: Math.round(cx + rx * Math.cos(angle)),
                y: Math.round(cy + ry * Math.sin(angle))
            };
        });

        // Build connecting edges with weights
        const edgeMap = new Set();
        for (let i = 0; i < N; i++) {
            const u = nodeList[i];
            let neighbors = [];
            if (N === 1) neighbors = [];
            else if (N === 2) neighbors = [(i + 1) % N];
            else if (N <= 4) neighbors = [(i + 1) % N, (i + 2) % N];
            else neighbors = [(i + 1) % N, (i + 2) % N, Math.floor((i + N / 2) % N)];

            neighbors.forEach(ni => {
                if (ni === i) return;
                const v = nodeList[ni];
                const key1 = u + "-" + v;
                const key2 = v + "-" + u;
                if (!edgeMap.has(key1) && !edgeMap.has(key2)) {
                    edgeMap.add(key1);
                    const weight = ((i * 3 + ni * 5) % 9) + 2;
                    this.edges.push({ from: u, to: v, weight: weight });
                }
            });
        }

        this.drawGraph();
    }

    drawGraph(activeNode = null, visitedNodes = [], activeEdge = null, path = []) {
        if (!this.ctx || !this.canvas) return;
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

        // Draw Edges
        this.edges.forEach(e => {
            const p1 = this.nodes[e.from];
            const p2 = this.nodes[e.to];
            if (!p1 || !p2) return;

            this.ctx.beginPath();
            this.ctx.moveTo(p1.x, p1.y);
            this.ctx.lineTo(p2.x, p2.y);

            const isPathEdge = (path && path.length > 1 && path.includes(e.from) && path.includes(e.to));
            const isActiveEdge = activeEdge && (
                activeEdge === (e.from + "-" + e.to) ||
                activeEdge === (e.to + "-" + e.from) ||
                (activeEdge.includes(e.from) && activeEdge.includes(e.to))
            );

            if (isPathEdge) {
                this.ctx.strokeStyle = "#10b981"; // Emerald green for final path / MST
                this.ctx.lineWidth = 5;
            } else if (isActiveEdge) {
                this.ctx.strokeStyle = "#f59e0b"; // Amber active traversal edge
                this.ctx.lineWidth = 4;
            } else {
                this.ctx.strokeStyle = "#475569";
                this.ctx.lineWidth = 2;
            }
            this.ctx.stroke();

            // Draw Edge Weight Bubble
            const midX = (p1.x + p2.x) / 2;
            const midY = (p1.y + p2.y) / 2;

            this.ctx.fillStyle = "#0f172a";
            this.ctx.beginPath();
            this.ctx.arc(midX, midY, 11, 0, 2 * Math.PI);
            this.ctx.fill();
            this.ctx.strokeStyle = "#64748b";
            this.ctx.lineWidth = 1;
            this.ctx.stroke();

            this.ctx.fillStyle = "#38bdf8";
            this.ctx.font = "bold 11px sans-serif";
            this.ctx.textAlign = "center";
            this.ctx.textBaseline = "middle";
            this.ctx.fillText(e.weight, midX, midY);
        });

        // Draw Nodes
        const radius = Object.keys(this.nodes).length > 8 ? 20 : 23;
        Object.keys(this.nodes).forEach(key => {
            const node = this.nodes[key];
            if (!node) return;

            this.ctx.beginPath();
            this.ctx.arc(node.x, node.y, radius, 0, 2 * Math.PI);

            if (key === activeNode) {
                this.ctx.fillStyle = "#f59e0b"; // Active current node
            } else if (visitedNodes && visitedNodes.includes(key)) {
                this.ctx.fillStyle = "#6366f1"; // Visited node
            } else {
                this.ctx.fillStyle = "#1e293b"; // Unvisited node
            }
            this.ctx.fill();

            this.ctx.lineWidth = 3;
            this.ctx.strokeStyle = (key === activeNode) ? "#fbbf24" : (visitedNodes && visitedNodes.includes(key) ? "#818cf8" : "#38bdf8");
            this.ctx.stroke();

            // Draw Label
            this.ctx.fillStyle = "#ffffff";
            this.ctx.font = "bold 13px sans-serif";
            this.ctx.textAlign = "center";
            this.ctx.textBaseline = "middle";
            this.ctx.fillText(key, node.x, node.y);
        });
    }

    setSteps(steps, algo = "BFS") {
        this.steps = steps || [];
        this.currentAlgo = algo;
        this.currentStepIdx = 0;
        this.pause();
        if (this.logContainer) this.logContainer.innerHTML = "";

        const banner = document.getElementById("finalResultBanner");
        if (banner) banner.classList.add("d-none");

        this.renderStep(0);
    }

    renderStep(idx) {
        if (!this.steps || this.steps.length === 0 || idx < 0 || idx >= this.steps.length) return;
        this.currentStepIdx = idx;
        const step = this.steps[idx];

        const desc = document.getElementById("stepDescription");
        if (desc) desc.innerText = step.description || "";

        const stepCounter = document.getElementById("stepCounter");
        if (stepCounter) stepCounter.innerText = `${step.stepNumber || (idx + 1)} / ${this.steps.length}`;

        const currNodeEl = document.getElementById("currentNodeDisplay");
        if (currNodeEl) currNodeEl.innerText = step.currentNode || "-";

        const distEl = document.getElementById("totalDistance");
        if (distEl) {
            distEl.innerText = (step.totalDistance !== undefined && step.totalDistance !== null) ? Number(step.totalDistance).toFixed(1) : "0.0";
        }

        const isLast = (idx === this.steps.length - 1 || step.action === "complete");
        const statusEl = document.getElementById("visualStatus");
        if (statusEl) {
            if (isLast) {
                statusEl.innerText = "Completed";
                statusEl.className = "text-success h4";
            } else if (this.isPlaying) {
                statusEl.innerText = "Running";
                statusEl.className = "text-warning h4";
            } else {
                statusEl.innerText = "Paused";
                statusEl.className = "text-info h4";
            }
        }

        const path = step.currentPath || [];
        const visited = step.visitedNodes || [];
        this.drawGraph(step.currentNode, visited, step.currentEdge, path);

        // Display Final Result in Banner when completed
        const banner = document.getElementById("finalResultBanner");
        const resTitle = document.getElementById("finalResultTitle");
        const resText = document.getElementById("finalResultText");
        const resBadge = document.getElementById("finalResultBadge");

        if (isLast && banner && resText) {
            banner.classList.remove("d-none");
            banner.classList.add("d-flex");

            if (this.currentAlgo.toLowerCase().includes("dijkstra")) {
                if (resTitle) resTitle.innerText = "Shortest Path Found!";
                const pathStr = (path && path.length > 0) ? path.join(" → ") : "Reachable nodes";
                resText.innerText = "Optimal Path: " + pathStr + " | Total Distance: " + (step.totalDistance ? Number(step.totalDistance).toFixed(1) : "0.0");
                if (resBadge) resBadge.innerText = "Shortest Path";
            } else if (this.currentAlgo.toLowerCase().includes("kruskal")) {
                if (resTitle) resTitle.innerText = "Minimum Spanning Tree (MST) Constructed!";
                resText.innerText = (step.description || "MST construction complete") + " | Total MST Weight: " + (step.totalDistance ? Number(step.totalDistance).toFixed(1) : "0.0");
                if (resBadge) resBadge.innerText = "MST Result";
            } else {
                if (resTitle) resTitle.innerText = this.currentAlgo + " Traversal Completed!";
                const visitedStr = (visited && visited.length > 0) ? visited.join(" → ") : Object.keys(this.nodes).join(" → ");
                resText.innerText = "Complete Node Traversal Order: " + visitedStr;
                if (resBadge) resBadge.innerText = "Traversal Result";
            }
        }

        if (this.logContainer) {
            const p = document.createElement("div");
            p.className = "py-1 border-bottom border-secondary small text-light";
            p.innerText = `[Step ${idx + 1}] ${step.description || ""}`;
            this.logContainer.appendChild(p);
            this.logContainer.scrollTop = this.logContainer.scrollHeight;
        }
    }

    play() {
        if (this.isPlaying) return;
        this.isPlaying = true;
        const statusEl = document.getElementById("visualStatus");
        if (statusEl) {
            statusEl.innerText = "Running";
            statusEl.className = "text-warning h4";
        }
        this.timer = setInterval(() => {
            if (this.currentStepIdx < this.steps.length - 1) {
                this.renderStep(this.currentStepIdx + 1);
            } else {
                this.pause();
                const sEl = document.getElementById("visualStatus");
                if (sEl) {
                    sEl.innerText = "Completed";
                    sEl.className = "text-success h4";
                }
            }
        }, this.speed);
    }

    pause() {
        this.isPlaying = false;
        if (this.timer) {
            clearInterval(this.timer);
            this.timer = null;
        }
        const statusEl = document.getElementById("visualStatus");
        if (statusEl && this.steps && this.currentStepIdx < this.steps.length - 1) {
            statusEl.innerText = "Paused";
            statusEl.className = "text-info h4";
        }
    }

    next() {
        this.pause();
        if (this.currentStepIdx < this.steps.length - 1) {
            this.renderStep(this.currentStepIdx + 1);
        }
    }

    prev() {
        this.pause();
        if (this.currentStepIdx > 0) {
            this.renderStep(this.currentStepIdx - 1);
        }
    }

    reset() {
        this.pause();
        if (this.logContainer) this.logContainer.innerHTML = "";
        const statusEl = document.getElementById("visualStatus");
        if (statusEl) {
            statusEl.innerText = "Ready";
            statusEl.className = "text-success h4";
        }
        const banner = document.getElementById("finalResultBanner");
        if (banner) banner.classList.add("d-none");
        this.renderStep(0);
    }

    setSpeed(newSpeedMs) {
        this.speed = Math.max(100, newSpeedMs);
        if (this.isPlaying) {
            this.pause();
            this.play();
        }
    }

    // Client-side fallback solver for resilience
    generateLocalSteps(algo, startNode, nodeList) {
        const steps = [];
        if (!nodeList || nodeList.length === 0) return steps;
        if (!startNode || !nodeList.includes(startNode)) startNode = nodeList[0];

        const adj = {};
        nodeList.forEach(n => { adj[n] = []; });
        this.edges.forEach(e => {
            if (adj[e.from]) adj[e.from].push({ node: e.to, weight: e.weight });
            if (adj[e.to]) adj[e.to].push({ node: e.from, weight: e.weight });
        });

        let stepNum = 0;
        const lower = (algo || "BFS").toLowerCase();

        if (lower.includes("dfs") || lower.includes("depth")) {
            const visited = [];
            const stack = [startNode];
            steps.push({
                currentNode: startNode,
                visitedNodes: [startNode],
                action: "start",
                description: `Started Depth-First Search (DFS) traversal from root Node ${startNode}.`,
                stepNumber: ++stepNum,
                totalDistance: 0
            });
            visited.push(startNode);

            const dfsHelper = (curr) => {
                const neighbors = adj[curr] || [];
                for (const nb of neighbors) {
                    if (!visited.includes(nb.node)) {
                        visited.push(nb.node);
                        steps.push({
                            currentNode: nb.node,
                            visitedNodes: [...visited],
                            currentEdge: `${curr}-${nb.node}`,
                            action: "visit",
                            description: `Traversed edge from Node ${curr} to Node ${nb.node}. Marked Node ${nb.node} as visited.`,
                            stepNumber: ++stepNum,
                            totalDistance: 0
                        });
                        dfsHelper(nb.node);
                    }
                }
            };
            dfsHelper(startNode);

            steps.push({
                currentNode: startNode,
                visitedNodes: [...visited],
                action: "complete",
                description: `DFS Traversal Complete! All ${visited.length} reachable nodes visited in depth-first order: [ ${visited.join(" → ")} ].`,
                stepNumber: ++stepNum,
                totalDistance: 0
            });
        } else if (lower.includes("dijkstra") || lower.includes("shortest")) {
            const endNode = nodeList[nodeList.length - 1];
            const dist = {};
            const prev = {};
            const visited = [];
            nodeList.forEach(n => { dist[n] = Infinity; });
            dist[startNode] = 0;

            steps.push({
                currentNode: startNode,
                visitedNodes: [],
                action: "start",
                description: `Initialized Dijkstra shortest path algorithm from Node ${startNode}. Target destination is Node ${endNode}.`,
                stepNumber: ++stepNum,
                totalDistance: 0
            });

            while (visited.length < nodeList.length) {
                let u = null;
                let minDist = Infinity;
                nodeList.forEach(n => {
                    if (!visited.includes(n) && dist[n] < minDist) {
                        minDist = dist[n];
                        u = n;
                    }
                });

                if (u === null || minDist === Infinity) break;
                visited.push(u);

                steps.push({
                    currentNode: u,
                    visitedNodes: [...visited],
                    action: "visit",
                    description: `Extracted minimum distance Node ${u} with cumulative distance ${dist[u].toFixed(1)}.`,
                    stepNumber: ++stepNum,
                    totalDistance: dist[u]
                });

                if (u === endNode) break;

                const neighbors = adj[u] || [];
                for (const nb of neighbors) {
                    if (!visited.includes(nb.node)) {
                        const alt = dist[u] + nb.weight;
                        if (alt < dist[nb.node]) {
                            dist[nb.node] = alt;
                            prev[nb.node] = u;
                            steps.push({
                                currentNode: u,
                                targetNode: nb.node,
                                currentEdge: `${u}-${nb.node}`,
                                visitedNodes: [...visited],
                                action: "relax",
                                description: `Relaxed edge ${u}-${nb.node} (weight ${nb.weight}): Updated distance of Node ${nb.node} to ${alt.toFixed(1)}.`,
                                stepNumber: ++stepNum,
                                totalDistance: alt
                            });
                        }
                    }
                }
            }

            // Reconstruct path
            const path = [];
            let curr = endNode;
            while (curr) {
                path.unshift(curr);
                curr = prev[curr];
            }
            if (path[0] !== startNode) path.length = 0;

            steps.push({
                currentNode: endNode,
                visitedNodes: [...visited],
                currentPath: path,
                action: "complete",
                description: `Dijkstra Shortest Path Search Complete! Shortest path to Node ${endNode}: [ ${path.join(" → ")} ] with total cost ${dist[endNode] < Infinity ? dist[endNode].toFixed(1) : "N/A"}.`,
                stepNumber: ++stepNum,
                totalDistance: dist[endNode] < Infinity ? dist[endNode] : 0
            });
        } else if (lower.includes("kruskal")) {
            const sortedEdges = [...this.edges].sort((a, b) => a.weight - b.weight);
            const parent = {};
            nodeList.forEach(n => { parent[n] = n; });
            const find = (i) => {
                if (parent[i] === i) return i;
                parent[i] = find(parent[i]);
                return parent[i];
            };
            const union = (i, j) => {
                const ri = find(i);
                const rj = find(j);
                if (ri !== rj) {
                    parent[ri] = rj;
                    return true;
                }
                return false;
            };

            steps.push({
                currentNode: startNode,
                action: "start",
                description: `Initialized Kruskal's algorithm. Sorted all ${sortedEdges.length} edges in ascending order of weight.`,
                stepNumber: ++stepNum,
                totalDistance: 0
            });

            const mstEdges = [];
            let totalWeight = 0;

            for (const edge of sortedEdges) {
                if (union(edge.from, edge.to)) {
                    mstEdges.push(edge);
                    totalWeight += edge.weight;
                    steps.push({
                        currentNode: edge.from,
                        targetNode: edge.to,
                        currentEdge: `${edge.from}-${edge.to}`,
                        currentPath: mstEdges.map(e => e.from).concat(mstEdges.map(e => e.to)),
                        action: "mst_add",
                        description: `Accepted edge ${edge.from}-${edge.to} (weight ${edge.weight}) into MST without cycle. Running weight: ${totalWeight.toFixed(1)}.`,
                        stepNumber: ++stepNum,
                        totalDistance: totalWeight
                    });
                }
            }

            const mstNodeList = Array.from(new Set(mstEdges.flatMap(e => [e.from, e.to])));
            steps.push({
                currentNode: startNode,
                visitedNodes: mstNodeList,
                currentPath: mstNodeList,
                action: "complete",
                description: `Kruskal MST Construction Complete! Selected ${mstEdges.length} edges connecting all nodes with minimum total weight of ${totalWeight.toFixed(1)}.`,
                stepNumber: ++stepNum,
                totalDistance: totalWeight
            });
        } else {
            // Default BFS
            const visited = [startNode];
            const queue = [startNode];

            steps.push({
                currentNode: startNode,
                visitedNodes: [startNode],
                action: "start",
                description: `Started Breadth-First Search (BFS) traversal at source Node ${startNode}.`,
                stepNumber: ++stepNum,
                totalDistance: 0
            });

            while (queue.length > 0) {
                const curr = queue.shift();
                const neighbors = adj[curr] || [];

                for (const nb of neighbors) {
                    if (!visited.includes(nb.node)) {
                        visited.push(nb.node);
                        queue.push(nb.node);
                        steps.push({
                            currentNode: curr,
                            targetNode: nb.node,
                            currentEdge: `${curr}-${nb.node}`,
                            visitedNodes: [...visited],
                            action: "visit",
                            description: `Visited edge ${curr}-${nb.node}. Enqueued Node ${nb.node} for level-order exploration.`,
                            stepNumber: ++stepNum,
                            totalDistance: 0
                        });
                    }
                }
            }

            steps.push({
                currentNode: startNode,
                visitedNodes: [...visited],
                action: "complete",
                description: `BFS Traversal Complete! All ${visited.length} reachable nodes visited level-by-level: [ ${visited.join(" → ")} ].`,
                stepNumber: ++stepNum,
                totalDistance: 0
            });
        }

        return steps;
    }
}
