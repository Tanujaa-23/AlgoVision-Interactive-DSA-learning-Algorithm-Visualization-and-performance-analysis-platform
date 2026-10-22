/**
 * AlgoVision Pro - Interactive Algorithm Visualization Engine
 */

class VisualizerEngine {
    constructor(containerId, logId) {
        this.container = document.getElementById(containerId);
        this.logContainer = document.getElementById(logId);
        this.steps = [];
        this.currentStepIdx = 0;
        this.isPlaying = false;
        this.speed = 300; // ms per step
        this.timer = null;
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

        // Update counters if element exist
        const compEl = document.getElementById("comparisonCount");
        const swapEl = document.getElementById("swapCount");
        const stepEl = document.getElementById("stepCounter");
        const descEl = document.getElementById("stepDescription");

        if (compEl) compEl.innerText = step.comparisonCount || 0;
        if (swapEl) swapEl.innerText = step.swapCount || 0;
        if (stepEl) stepEl.innerText = `${step.stepNumber || (idx + 1)} / ${this.steps.length}`;
        if (descEl) descEl.innerText = step.description || "";

        // Log entry
        if (this.logContainer) {
            const p = document.createElement("div");
            p.className = "py-1 border-bottom border-secondary small text-light";
            p.innerText = `[Step ${idx + 1}] ${step.description}`;
            this.logContainer.appendChild(p);
            this.logContainer.scrollTop = this.logContainer.scrollHeight;
        }

        // Render Array Bars
        this.container.innerHTML = "";
        const arr = step.array || [];
        const maxVal = Math.max(...arr, 1);

        arr.forEach((val, i) => {
            const bar = document.createElement("div");
            bar.className = "array-bar";

            // Calculate height percentage
            const pct = Math.max(15, Math.round((val / maxVal) * 80));
            bar.style.height = `${pct}%`;
            bar.innerText = val;

            // Highlight based on action & index
            if (step.action === "complete") {
                bar.classList.add("sorted");
            } else if (i === step.pivotIndex) {
                bar.classList.add("pivot");
            } else if (i === step.index1 || i === step.index2) {
                if (step.action === "compare") bar.classList.add("comparing");
                else if (step.action === "swap") bar.classList.add("swapping");
                else if (step.action === "found") bar.classList.add("sorted");
            }

            this.container.appendChild(bar);
        });
    }

    play() {
        if (this.isPlaying) return;
        this.isPlaying = true;
        this.timer = setInterval(() => {
            if (this.currentStepIdx < this.steps.length - 1) {
                this.renderStep(this.currentStepIdx + 1);
            } else {
                this.pause();
            }
        }, this.speed);
    }

    pause() {
        this.isPlaying = false;
        if (this.timer) {
            clearInterval(this.timer);
            this.timer = null;
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
        this.renderStep(0);
    }

    setSpeed(newSpeedMs) {
        this.speed = newSpeedMs;
        if (this.isPlaying) {
            this.pause();
            this.play();
        }
    }
}
