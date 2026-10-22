/**
 * Graph Canvas Visualizer - AlgoVision Pro
 */

class GraphCanvasVisualizer {
    constructor(canvasId, logId = "logContainer") {
        this.canvas = document.getElementById(canvasId);
        this.logContainer = document.getElementById(logId);
        if (!this.canvas) return;
        this.ctx = this.canvas.getContext("2d");
        
        // Default nodes position
        this.nodes = {
            "A": { x: 100, y: 160 },
            "B": { x: 300, y: 80 },
            "C": { x: 300, y: 240 },
            "D": { x: 500, y: 160 }
        };

        this.edges = [
            { from: "A", to: "B", weight: 4 },
            { from: "A", to: "C", weight: 2 },
            { from: "B", to: "C", weight: 1 },
            { from: "B", to: "D", weight: 5 },
            { from: "C", to: "D", weight: 8 }
        ];

        this.steps = [];
        this.currentStepIdx = 0;
        this.isPlaying = false;
        this.speed = 500; // ms per step
        this.timer = null;
        this.drawGraph();
    }

    drawGraph(activeNode = null, visitedNodes = [], activeEdge = null, path = []) {
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);

        // Draw Edges
        this.edges.forEach(e => {
            const p1 = this.nodes[e.from];
            const p2 = this.nodes[e.to];
            if (!p1 || !p2) return;

            this.ctx.beginPath();
            this.ctx.moveTo(p1.x, p1.y);
            this.ctx.lineTo(p2.x, p2.y);

            const isPathEdge = (path.includes(e.from) && path.includes(e.to));
            const isActiveEdge = activeEdge && (
                (activeEdge.includes(e.from + "-" + e.to)) || 
                (activeEdge.includes(e.to + "-" + e.from)) ||
                (activeEdge.includes(e.from) && activeEdge.includes(e.to))
            );

            if (isPathEdge) {
                this.ctx.strokeStyle = "#10b981"; // Emerald path
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
        Object.keys(this.nodes).forEach(key => {
            const node = this.nodes[key];
            this.ctx.beginPath();
            this.ctx.arc(node.x, node.y, 24, 0, 2 * Math.PI);

            if (key === activeNode) {
                this.ctx.fillStyle = "#f59e0b"; // Active current node
            } else if (visitedNodes.includes(key)) {
                this.ctx.fillStyle = "#6366f1"; // Visited node
            } else {
                this.ctx.fillStyle = "#1e293b"; // Unvisited node
            }
            this.ctx.fill();
            this.ctx.lineWidth = 3;
            this.ctx.strokeStyle = (key === activeNode) ? "#fbbf24" : (visitedNodes.includes(key) ? "#818cf8" : "#38bdf8");
            this.ctx.stroke();

            // Draw Label
            this.ctx.fillStyle = "#ffffff";
            this.ctx.font = "bold 15px sans-serif";
            this.ctx.textAlign = "center";
            this.ctx.textBaseline = "middle";
            this.ctx.fillText(key, node.x, node.y);
        });
    }

    setSteps(steps) {
        this.steps = steps || [];
        this.currentStepIdx = 0;
        this.pause();
        if (this.logContainer) this.logContainer.innerHTML = "";
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

        const statusEl = document.getElementById("visualStatus");
        if (statusEl) {
            if (idx === this.steps.length - 1 || step.action === "complete") {
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
        this.renderStep(0);
    }

    setSpeed(newSpeedMs) {
        this.speed = Math.max(100, newSpeedMs);
        if (this.isPlaying) {
            this.pause();
            this.play();
        }
    }
}
