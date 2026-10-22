/**
 * Data Structures Visualizer JS Handler
 */

function performDSOperation(dsType, operation) {
    const valInput = document.getElementById("dsValue");
    const val = valInput ? valInput.value : "";

    const params = new URLSearchParams();
    params.append("ds", dsType);
    params.append("operation", operation);
    params.append("value", val);

    fetch("datastructures", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: params.toString()
    })
    .then(res => res.json())
    .then(data => {
        renderDSContainer(dsType, data);
    })
    .catch(err => console.error("DS error:", err));
}

function renderDSContainer(dsType, data) {
    const container = document.getElementById("dsVisualContainer");
    const msgEl = document.getElementById("dsStatusMsg");
    if (msgEl) msgEl.innerText = data.message || "";

    if (!container) return;
    container.innerHTML = "";

    const elements = data.elements || [];

    if (dsType === "stack") {
        // Render stack vertical blocks bottom-up
        elements.slice().reverse().forEach(val => {
            const block = document.createElement("div");
            block.className = "p-3 mb-2 text-center rounded text-white font-weight-bold bg-primary border border-light";
            block.innerText = `Stack Item: ${val}`;
            container.appendChild(block);
        });
    } else if (dsType === "queue") {
        // Render horizontal queue blocks
        const row = document.createElement("div");
        row.className = "d-flex flex-wrap gap-2 justify-content-center align-items-center w-100";
        elements.forEach((val, idx) => {
            const block = document.createElement("div");
            block.className = "p-3 text-center rounded text-white font-weight-bold bg-secondary border border-info";
            block.innerText = `${idx === 0 ? "FRONT: " : ""}${val}`;
            row.appendChild(block);
        });
        container.appendChild(row);
    } else if (dsType === "linkedlist") {
        const row = document.createElement("div");
        row.className = "d-flex flex-wrap gap-2 justify-content-center align-items-center w-100";
        elements.forEach((val, idx) => {
            const block = document.createElement("div");
            block.className = "p-3 rounded text-white bg-dark border border-primary d-flex align-items-center gap-2";
            block.innerHTML = `<span>Node(${val})</span> <i class="bi bi-arrow-right text-info"></i>`;
            row.appendChild(block);
        });
        container.appendChild(row);
    } else {
        // General elements block display
        const row = document.createElement("div");
        row.className = "d-flex flex-wrap gap-2 justify-content-center w-100";
        elements.forEach(val => {
            const block = document.createElement("div");
            block.className = "p-3 rounded text-white bg-indigo border border-warning";
            block.innerText = val;
            row.appendChild(block);
        });
        container.appendChild(row);
    }
}
