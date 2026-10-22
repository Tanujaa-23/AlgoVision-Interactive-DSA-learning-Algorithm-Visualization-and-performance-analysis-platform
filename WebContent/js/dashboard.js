/**
 * Dashboard Chart.js Visual Analytics Script
 */

document.addEventListener("DOMContentLoaded", () => {
    initProgressChart();
});

function initProgressChart() {
    const ctx = document.getElementById("progressChartCanvas");
    if (!ctx) return;

    const labels = (typeof progressLabels !== 'undefined' && progressLabels.length > 0) 
        ? progressLabels 
        : ['Sorting', 'Searching', 'Graph', 'Data Structures'];

    const counts = (typeof progressCounts !== 'undefined' && progressCounts.length > 0) 
        ? progressCounts 
        : [0, 0, 0, 0];

    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: counts,
                backgroundColor: [
                    '#6366f1', // Indigo for Sorting
                    '#38bdf8', // Sky Blue for Searching
                    '#8b5cf6', // Purple for Graph
                    '#10b981'  // Emerald for Data Structures
                ],
                borderWidth: 2,
                borderColor: '#1e293b'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: { color: '#f8fafc', font: { size: 12 } }
                }
            }
        }
    });
}
