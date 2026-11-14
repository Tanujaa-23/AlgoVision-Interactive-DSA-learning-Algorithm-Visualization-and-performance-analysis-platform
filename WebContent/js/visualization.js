/**
 * AlgoVision Pro - Interactive Algorithm Visualization Engine
 * Supports custom numeric inputs and dynamic result display for Sorting & Searching
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
        this.category = "Sorting";
        this.currentAlgo = "";
        this.currentInput = "";
        this.hasRecorded = false;
    }

    setSteps(steps, category = "Sorting", algo = "", input = "") {
        this.steps = steps || [];
        this.category = category;
        this.currentAlgo = algo;
        this.currentInput = input;
        this.currentStepIdx = 0;
        this.hasRecorded = false;
        this.pause();
        if (this.logContainer) this.logContainer.innerHTML = "";

        const banner = document.getElementById("finalResultBanner");
        if (banner) banner.classList.add("d-none");

        const statusEl = document.getElementById("visualStatus");
        if (statusEl) {
            statusEl.innerText = "Ready";
            statusEl.className = "text-success h4";
        }

        this.renderStep(0);
    }

    renderStep(idx) {
        if (!this.steps || this.steps.length === 0 || idx < 0 || idx >= this.steps.length) return;

        this.currentStepIdx = idx;
        const step = this.steps[idx];

        // Update metric counters
        const compEl = document.getElementById("comparisonCount");
        const swapEl = document.getElementById("swapCount");
        const stepEl = document.getElementById("stepCounter");
        const descEl = document.getElementById("stepDescription");
        const statusEl = document.getElementById("visualStatus");

        if (compEl) compEl.innerText = step.comparisonCount || 0;
        if (swapEl) swapEl.innerText = step.swapCount || 0;
        if (stepEl) stepEl.innerText = `${step.stepNumber || (idx + 1)} / ${this.steps.length}`;
        if (descEl) descEl.innerText = step.description || "";

        const isLast = (idx === this.steps.length - 1);
        const action = step.action || "";

        // Status update
        if (statusEl) {
            if (action === "found") {
                statusEl.innerText = "Target Found";
                statusEl.className = "text-success h4";
            } else if (action === "not_found") {
                statusEl.innerText = "Not Found";
                statusEl.className = "text-danger h4";
            } else if (action === "complete" || isLast) {
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

        // Final Result Display in Banner
        const banner = document.getElementById("finalResultBanner");
        const resText = document.getElementById("finalResultText");
        const resTitle = document.getElementById("finalResultTitle");
        const resBadge = document.getElementById("finalResultBadge");
        const targetInput = document.getElementById("targetInput");
        const targetVal = targetInput ? targetInput.value : "";

        if (banner && resText) {
            if (action === "complete" || (isLast && this.category === "Sorting")) {
                banner.classList.remove("d-none");
                banner.classList.add("d-flex");
                if (resTitle) resTitle.innerText = "Array Fully Sorted!";
                resText.innerText = "Final Sorted Result: [ " + (step.array || []).join(", ") + " ]";
                if (resBadge) {
                    resBadge.innerText = "Sorted Result";
                    resBadge.className = "badge bg-success px-3 py-2";
                }
            } else if (action === "found") {
                banner.classList.remove("d-none");
                banner.classList.add("d-flex");
                if (resTitle) resTitle.innerText = "Target Element Located!";
                resText.innerText = `Search Result: Target value ${targetVal || step.index1} found at index ${step.index1}!`;
                if (resBadge) {
                    resBadge.innerText = "Found";
                    resBadge.className = "badge bg-success px-3 py-2";
                }
            } else if (action === "not_found") {
                banner.classList.remove("d-none");
                banner.classList.add("d-flex");
                if (resTitle) resTitle.innerText = "Target Not Found";
                resText.innerText = `Search Result: Target value ${targetVal} is NOT present in the array.`;
                if (resBadge) {
                    resBadge.innerText = "Not Found";
                    resBadge.className = "badge bg-danger px-3 py-2";
                }
            }
        }

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
        const minVal = Math.min(...arr, 0);
        const maxVal = Math.max(...arr, 1);
        const range = (maxVal - minVal) || 1;

        arr.forEach((val, i) => {
            const bar = document.createElement("div");
            bar.className = "array-bar";

            // Calculate height percentage normalized for user's numbers
            const pct = Math.max(16, Math.round(((val - minVal) / range) * 80));
            bar.style.height = `${pct}%`;
            bar.innerText = val;

            // Highlight based on action & index
            if (action === "complete" || (isLast && this.category === "Sorting")) {
                bar.classList.add("sorted");
            } else if (i === step.pivotIndex) {
                bar.classList.add("pivot");
            } else if (i === step.index1 || i === step.index2) {
                if (action === "compare") bar.classList.add("comparing");
                else if (action === "swap") bar.classList.add("swapping");
                else if (action === "found") bar.classList.add("sorted");
            }

            this.container.appendChild(bar);
        });
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
                    const lastStep = this.steps[this.steps.length - 1];
                    if (lastStep && lastStep.action === "found") {
                        sEl.innerText = "Target Found";
                        sEl.className = "text-success h4";
                    } else if (lastStep && lastStep.action === "not_found") {
                        sEl.innerText = "Not Found";
                        sEl.className = "text-danger h4";
                    } else {
                        sEl.innerText = "Completed";
                        sEl.className = "text-success h4";
                    }
                }
                this.recordCompletion();
            }
        }, this.speed);
    }

    recordCompletion() {
        if (this.hasRecorded || !this.steps || this.steps.length === 0) return;
        this.hasRecorded = true;

        const lastStep = this.steps[this.steps.length - 1];
        const contextPath = window.contextPath || "";
        const endpoint = contextPath ? (contextPath + "/visualize") : "visualize";

        const params = new URLSearchParams();
        params.append("action", "record");
        params.append("category", this.category || "Sorting");
        params.append("algorithm", this.currentAlgo || "Bubble Sort");
        params.append("inputData", this.currentInput || "");
        params.append("stepCount", this.steps.length);
        params.append("comparisons", (lastStep && lastStep.comparisonCount) ? lastStep.comparisonCount : this.steps.length);
        params.append("swaps", (lastStep && lastStep.swapCount) ? lastStep.swapCount : 0);

        fetch(endpoint, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: params.toString()
        }).catch(err => console.warn("Failed to record completion:", err));
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
        this.speed = Math.max(50, newSpeedMs);
        if (this.isPlaying) {
            this.pause();
            this.play();
        }
    }
}
