<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.algovision.service.PerformanceService.MetricResult, java.util.List, java.util.Set, java.util.LinkedHashSet, java.util.Map, java.util.HashMap, java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Performance Analyzer - AlgoVision Pro</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>

    <nav class="navbar navbar-expand-lg navbar-dark navbar-custom sticky-top">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center gap-2" href="${pageContext.request.contextPath}/dashboard">
                <i class="bi bi-diagram-3-fill text-info"></i> AlgoVision Pro
            </a>
            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/algorithms">Algorithms</a></li>
                    <li class="nav-item"><a class="nav-link active" href="${pageContext.request.contextPath}/performance">Performance Analyzer</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="fw-bold text-white mb-1">Empirical Performance Benchmark Analyzer</h2>
                <p class="text-muted mb-0">Empirically evaluate execution time, comparison counts, and swaps vs array size across O(N²), O(N log N), and O(N) sorting algorithms</p>
            </div>
        </div>

        <!-- Benchmark Configuration Bar -->
        <div class="card card-custom p-4 mb-4">
            <form action="${pageContext.request.contextPath}/performance" method="get" class="row g-3 align-items-center">
                <div class="col-md-8">
                    <label class="form-label text-info small fw-bold">INPUT ARRAY SIZES (COMMA SEPARATED)</label>
                    <input type="text" name="sizes" class="form-control bg-dark text-light border-secondary" value="${sizesParam != null ? sizesParam : '100, 500, 1000, 5000'}" placeholder="e.g. 100, 500, 1000, 5000">
                </div>
                <div class="col-md-4 text-end">
                    <label class="form-label d-block">&nbsp;</label>
                    <button type="submit" class="btn btn-primary-custom w-100"><i class="bi bi-cpu-fill me-2"></i>Run Benchmark</button>
                </div>
            </form>
        </div>

        <!-- Chart.js Benchmark Graph -->
        <div class="card card-custom p-4 mb-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h5 class="fw-bold text-white mb-0"><i class="bi bi-graph-up text-info me-2"></i>Algorithm Benchmark Visualization</h5>
                <div class="btn-group btn-group-sm" role="group">
                    <button type="button" class="btn btn-outline-info active" id="btnMetricTime" onclick="switchMetric('time')">Execution Time (ms)</button>
                    <button type="button" class="btn btn-outline-info" id="btnMetricComps" onclick="switchMetric('comps')">Comparisons Count</button>
                    <button type="button" class="btn btn-outline-info" id="btnMetricSwaps" onclick="switchMetric('swaps')">Swaps Count</button>
                </div>
            </div>
            <div style="height: 340px;">
                <canvas id="perfChartCanvas"></canvas>
            </div>
        </div>

        <!-- Metrics Table -->
        <div class="card card-custom p-4">
            <h5 class="fw-bold text-white mb-3"><i class="bi bi-table text-warning me-2"></i>Detailed Benchmark Metrics Table</h5>
            <div class="table-responsive">
                <table class="table table-dark table-striped align-middle mb-0">
                    <thead>
                        <tr>
                            <th>Algorithm</th>
                            <th>Input Size (N)</th>
                            <th>Execution Time (ms)</th>
                            <th>Comparisons</th>
                            <th>Swaps / Operations</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% List<MetricResult> results = (List<MetricResult>) request.getAttribute("benchmarkResults");
                           if (results != null && !results.isEmpty()) {
                               for (MetricResult m : results) { %>
                            <tr>
                                <td><strong class="text-info"><%= m.getAlgorithmName() %></strong></td>
                                <td><span class="badge bg-secondary"><%= m.getInputSize() %> items</span></td>
                                <td class="text-warning font-monospace"><%= String.format("%.3f", m.getExecutionTimeMs()) %> ms</td>
                                <td class="font-monospace"><%= String.format("%,d", m.getComparisons()) %></td>
                                <td class="font-monospace"><%= String.format("%,d", m.getSwaps()) %></td>
                            </tr>
                        <% } } else { %>
                            <tr>
                                <td colspan="5" class="text-center text-muted">No benchmark results available. Click 'Run Benchmark' above.</td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        <% 
        Set<Integer> sizeSet = new LinkedHashSet<>();
        if (results != null) {
            for (MetricResult m : results) {
                sizeSet.add(m.getInputSize());
            }
        }
        StringBuilder labelsSb = new StringBuilder();
        if (!sizeSet.isEmpty()) {
            for (Integer sz : sizeSet) {
                if (labelsSb.length() > 0) labelsSb.append(", ");
                labelsSb.append("'").append(sz).append("'");
            }
        } else {
            labelsSb.append("'100', '500', '1000', '5000'");
        }
        %>

        const labels = [<%= labelsSb.toString() %>];
        let chartInstance = null;

        const benchmarkData = [
            <% if (results != null && !results.isEmpty()) {
                for (int i = 0; i < results.size(); i++) {
                    MetricResult m = results.get(i); %>
                    {
                        algo: "<%= m.getAlgorithmName() %>",
                        size: <%= m.getInputSize() %>,
                        time: <%= m.getExecutionTimeMs() %>,
                        comps: <%= m.getComparisons() %>,
                        swaps: <%= m.getSwaps() %>
                    }<%= i < results.size() - 1 ? "," : "" %>
                <% }
            } %>
        ];

        const algoColors = {
            'Bubble Sort': '#ef4444',
            'Selection Sort': '#f59e0b',
            'Insertion Sort': '#eab308',
            'Merge Sort': '#10b981',
            'Quick Sort': '#38bdf8',
            'Heap Sort': '#8b5cf6'
        };

        function getDatasets(metricKey) {
            const algoMap = {};
            benchmarkData.forEach(item => {
                if (!algoMap[item.algo]) {
                    algoMap[item.algo] = [];
                }
                algoMap[item.algo].push(item[metricKey]);
            });

            return Object.keys(algoMap).map(algo => ({
                label: algo,
                data: algoMap[algo],
                borderColor: algoColors[algo] || '#38bdf8',
                backgroundColor: algoColors[algo] || '#38bdf8',
                tension: 0.2
            }));
        }

        function renderChart(metricKey, yTitle) {
            const ctx = document.getElementById("perfChartCanvas").getContext("2d");
            if (chartInstance) {
                chartInstance.destroy();
            }

            chartInstance = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: getDatasets(metricKey)
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        x: { ticks: { color: '#94a3b8' }, title: { display: true, text: 'Input Size (N)', color: '#f8fafc' } },
                        y: { ticks: { color: '#94a3b8' }, title: { display: true, text: yTitle, color: '#f8fafc' } }
                    },
                    plugins: {
                        legend: { labels: { color: '#f8fafc' } }
                    }
                }
            });
        }

        function switchMetric(metric) {
            document.getElementById("btnMetricTime").classList.remove("active");
            document.getElementById("btnMetricComps").classList.remove("active");
            document.getElementById("btnMetricSwaps").classList.remove("active");

            if (metric === 'time') {
                document.getElementById("btnMetricTime").classList.add("active");
                renderChart('time', 'Execution Time (ms)');
            } else if (metric === 'comps') {
                document.getElementById("btnMetricComps").classList.add("active");
                renderChart('comps', 'Total Comparisons');
            } else if (metric === 'swaps') {
                document.getElementById("btnMetricSwaps").classList.add("active");
                renderChart('swaps', 'Total Swaps / Operations');
            }
        }

        document.addEventListener("DOMContentLoaded", () => {
            renderChart('time', 'Execution Time (ms)');
        });
    </script>
</body>
</html>
