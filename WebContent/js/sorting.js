/**
 * Sorting & Searching Visualizer Page Event Handler - AlgoVision Pro
 * Dynamically handles user entered numbers and displays real-time results
 */

let engine = null;

function parseInputArray(str) {
    if (!str || !str.trim()) return [5, 2, 8, 1, 3, 9, 4, 7];
    const parts = str.trim().split(/[,\s]+/);
    const result = [];
    parts.forEach(p => {
        const num = parseInt(p.trim(), 10);
        if (!isNaN(num)) result.push(num);
    });
    return result.length > 0 ? result : [5, 2, 8, 1, 3, 9, 4, 7];
}

document.addEventListener("DOMContentLoaded", () => {
    engine = new VisualizerEngine("arrayContainer", "logContainer");

    const form = document.getElementById("visualizerForm");
    if (form) {
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            fetchSteps(true);
        });
    }

    const inputDataEl = document.getElementById("inputData");
    if (inputDataEl) {
        inputDataEl.addEventListener("change", () => {
            fetchSteps(false);
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
        targetInput.addEventListener("change", () => {
            fetchSteps(false);
        });
    }

    // Default initial load
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
        engine.setSteps(steps, category);
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
        console.warn("Backend fetch failed, generating client-side visualization steps:", err);
        const localSteps = generateLocalSteps(category, algo, inputData, target);
        if (localSteps && localSteps.length > 0) {
            engine.setSteps(localSteps, category);
            const urlParams = new URLSearchParams(window.location.search);
            const isRunRequested = shouldAutoPlay || urlParams.get("run") === "true";
            if (isRunRequested) {
                setTimeout(() => {
                    if (engine && !engine.isPlaying) {
                        engine.play();
                    }
                }, 300);
            }
        }
    });
}

// Client-side execution fallback engine
function generateLocalSteps(category, algoName, inputStr, targetStr) {
    const steps = [];
    const arr = parseInputArray(inputStr);
    const target = targetStr ? parseInt(targetStr, 10) : (arr[0] || 5);
    let stepNum = 0;
    let comparisons = 0;
    let swaps = 0;

    const lowerAlgo = (algoName || "").toLowerCase();

    if (category === "Searching") {
        if (lowerAlgo.includes("binary")) {
            // Binary search expects sorted array
            arr.sort((a, b) => a - b);
            steps.push({
                array: [...arr],
                index1: -1,
                index2: -1,
                action: "start",
                description: `Starting Binary Search for target: ${target} on sorted array.`,
                stepNumber: ++stepNum,
                comparisonCount: 0,
                swapCount: 0
            });

            let low = 0, high = arr.length - 1;
            let found = false;

            while (low <= high) {
                const mid = Math.floor(low + (high - low) / 2);
                comparisons++;
                steps.push({
                    array: [...arr],
                    index1: low,
                    index2: high,
                    pivotIndex: mid,
                    action: "compare",
                    description: `Search window [${low}..${high}]. Checking mid index ${mid} (value ${arr[mid]}) against target ${target}.`,
                    stepNumber: ++stepNum,
                    comparisonCount: comparisons,
                    swapCount: 0
                });

                if (arr[mid] === target) {
                    steps.push({
                        array: [...arr],
                        index1: mid,
                        index2: -1,
                        pivotIndex: mid,
                        action: "found",
                        description: `Target element ${target} FOUND at index ${mid}!`,
                        stepNumber: ++stepNum,
                        comparisonCount: comparisons,
                        swapCount: 0
                    });
                    found = true;
                    break;
                } else if (arr[mid] < target) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }

            if (!found) {
                steps.push({
                    array: [...arr],
                    index1: -1,
                    index2: -1,
                    action: "not_found",
                    description: `Target element ${target} NOT FOUND in array.`,
                    stepNumber: ++stepNum,
                    comparisonCount: comparisons,
                    swapCount: 0
                });
            }
        } else {
            // Linear Search
            steps.push({
                array: [...arr],
                index1: -1,
                index2: -1,
                action: "start",
                description: `Starting Linear Search for target: ${target}.`,
                stepNumber: ++stepNum,
                comparisonCount: 0,
                swapCount: 0
            });

            let found = false;
            for (let i = 0; i < arr.length; i++) {
                comparisons++;
                steps.push({
                    array: [...arr],
                    index1: i,
                    index2: -1,
                    action: "compare",
                    description: `Checking index ${i}: arr[${i}] (${arr[i]}) == target (${target})?`,
                    stepNumber: ++stepNum,
                    comparisonCount: comparisons,
                    swapCount: 0
                });

                if (arr[i] === target) {
                    steps.push({
                        array: [...arr],
                        index1: i,
                        index2: -1,
                        action: "found",
                        description: `Target element ${target} FOUND at index ${i}!`,
                        stepNumber: ++stepNum,
                        comparisonCount: comparisons,
                        swapCount: 0
                    });
                    found = true;
                    break;
                }
            }

            if (!found) {
                steps.push({
                    array: [...arr],
                    index1: -1,
                    index2: -1,
                    action: "not_found",
                    description: `Target element ${target} NOT FOUND in array.`,
                    stepNumber: ++stepNum,
                    comparisonCount: comparisons,
                    swapCount: 0
                });
            }
        }
    } else {
        // Sorting algorithms
        steps.push({
            array: [...arr],
            index1: -1,
            index2: -1,
            action: "start",
            description: `Starting ${algoName} with input array: [ ${arr.join(", ")} ].`,
            stepNumber: ++stepNum,
            comparisonCount: 0,
            swapCount: 0
        });

        const working = [...arr];
        const n = working.length;

        if (lowerAlgo.includes("selection")) {
            for (let i = 0; i < n - 1; i++) {
                let minIdx = i;
                for (let j = i + 1; j < n; j++) {
                    comparisons++;
                    steps.push({
                        array: [...working],
                        index1: j,
                        index2: minIdx,
                        action: "compare",
                        description: `Comparing arr[${j}] (${working[j]}) with current minimum arr[${minIdx}] (${working[minIdx]}).`,
                        stepNumber: ++stepNum,
                        comparisonCount: comparisons,
                        swapCount: swaps
                    });
                    if (working[j] < working[minIdx]) minIdx = j;
                }
                if (minIdx !== i) {
                    const temp = working[i];
                    working[i] = working[minIdx];
                    working[minIdx] = temp;
                    swaps++;
                    steps.push({
                        array: [...working],
                        index1: i,
                        index2: minIdx,
                        action: "swap",
                        description: `Swapped arr[${i}] and arr[${minIdx}]. Placed minimum element ${working[i]} in sorted position.`,
                        stepNumber: ++stepNum,
                        comparisonCount: comparisons,
                        swapCount: swaps
                    });
                }
            }
        } else if (lowerAlgo.includes("insertion")) {
            for (let i = 1; i < n; i++) {
                const key = working[i];
                let j = i - 1;
                while (j >= 0) {
                    comparisons++;
                    steps.push({
                        array: [...working],
                        index1: j,
                        index2: j + 1,
                        action: "compare",
                        description: `Comparing element arr[${j}] (${working[j]}) with key (${key}).`,
                        stepNumber: ++stepNum,
                        comparisonCount: comparisons,
                        swapCount: swaps
                    });
                    if (working[j] > key) {
                        working[j + 1] = working[j];
                        swaps++;
                        steps.push({
                            array: [...working],
                            index1: j,
                            index2: j + 1,
                            action: "swap",
                            description: `Shifted arr[${j}] (${working[j + 1]}) to right.`,
                            stepNumber: ++stepNum,
                            comparisonCount: comparisons,
                            swapCount: swaps
                        });
                        j--;
                    } else {
                        break;
                    }
                }
                working[j + 1] = key;
            }
        } else {
            // Default Bubble Sort
            for (let i = 0; i < n - 1; i++) {
                let swapped = false;
                for (let j = 0; j < n - i - 1; j++) {
                    comparisons++;
                    steps.push({
                        array: [...working],
                        index1: j,
                        index2: j + 1,
                        action: "compare",
                        description: `Comparing arr[${j}] (${working[j]}) and arr[${j + 1}] (${working[j + 1]}).`,
                        stepNumber: ++stepNum,
                        comparisonCount: comparisons,
                        swapCount: swaps
                    });

                    if (working[j] > working[j + 1]) {
                        const temp = working[j];
                        working[j] = working[j + 1];
                        working[j + 1] = temp;
                        swaps++;
                        swapped = true;
                        steps.push({
                            array: [...working],
                            index1: j,
                            index2: j + 1,
                            action: "swap",
                            description: `Swapped arr[${j}] (${working[j]}) and arr[${j + 1}] (${working[j + 1]}).`,
                            stepNumber: ++stepNum,
                            comparisonCount: comparisons,
                            swapCount: swaps
                        });
                    }
                }
                if (!swapped) break;
            }
        }

        steps.push({
            array: [...working],
            index1: -1,
            index2: -1,
            action: "complete",
            description: `Sorting Complete! Final sorted result: [ ${working.join(", ")} ].`,
            stepNumber: ++stepNum,
            comparisonCount: comparisons,
            swapCount: swaps
        });
    }

    return steps;
}
