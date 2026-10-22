/**
 * Sorting visualizer page event handler
 */

let engine = null;

document.addEventListener("DOMContentLoaded", () => {
    engine = new VisualizerEngine("arrayContainer", "logContainer");

    const form = document.getElementById("visualizerForm");
    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            fetchSteps(true);
        });
    }

    const algoSelect = document.getElementById("algorithmSelect");
    if (algoSelect) {
        algoSelect.addEventListener("change", () => {
            fetchSteps(false);
        });
    }

    const targetInput = document.getElementById("targetInput");
    if (targetInput) {
        targetInput.addEventListener("input", () => {
            const targetDisplay = document.getElementById("targetDisplay");
            if (targetDisplay) targetDisplay.innerText = targetInput.value;
        });
    }

    // Default load (will auto-run if ?run=true is in URL)
    fetchSteps();
});

function fetchSteps(shouldAutoPlay = false) {
    const algoEl = document.getElementById("algorithmSelect");
    if (!algoEl) return;
    const algo = algoEl.value;
    const inputData = document.getElementById("inputData") ? document.getElementById("inputData").value : "";
    const category = document.getElementById("categoryInput") ? document.getElementById("categoryInput").value : "Sorting";
    const targetEl = document.getElementById("targetInput");
    const target = targetEl ? targetEl.value : "";
    const targetDisplay = document.getElementById("targetDisplay");
    if (targetDisplay && target) {
        targetDisplay.innerText = target;
    }

    const formData = new URLSearchParams();
    formData.append("algorithm", algo);
    formData.append("inputData", inputData);
    formData.append("category", category);
    if (target) formData.append("target", target);

    const contextPath = window.contextPath || "";
    const endpoint = contextPath ? (contextPath + "/visualize") : "visualize";

    fetch(endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: formData.toString()
    })
    .then(res => res.json())
    .then(steps => {
        engine.setSteps(steps);
        const urlParams = new URLSearchParams(window.location.search);
        const isRunRequested = shouldAutoPlay || urlParams.get("run") === "true";
        if (isRunRequested) {
            setTimeout(() => {
                if (engine && !engine.isPlaying) {
                    engine.play();
                }
            }, 300);
        }
    })
    .catch(err => {
        console.error("Error fetching execution steps:", err);
    });
}
