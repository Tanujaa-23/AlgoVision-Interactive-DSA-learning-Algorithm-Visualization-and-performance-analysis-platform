package com.algovision.service;

import java.util.*;

public class RecommendationService {

    public static class RecommendationResult {
        private String recommendedAlgorithm;
        private String category;
        private String reason;
        private String timeComplexity;
        private String spaceComplexity;

        public RecommendationResult(String recommendedAlgorithm, String category, String reason, String timeComplexity, String spaceComplexity) {
            this.recommendedAlgorithm = recommendedAlgorithm;
            this.category = category;
            this.reason = reason;
            this.timeComplexity = timeComplexity;
            this.spaceComplexity = spaceComplexity;
        }

        public String getRecommendedAlgorithm() { return recommendedAlgorithm; }
        public String getCategory() { return category; }
        public String getReason() { return reason; }
        public String getTimeComplexity() { return timeComplexity; }
        public String getSpaceComplexity() { return spaceComplexity; }
    }

    public RecommendationResult recommendAlgorithm(String query) {
        if (query == null || query.trim().isEmpty()) {
            return defaultRecommendation();
        }

        String q = query.toLowerCase().trim();

        // ==========================================
        // LAYER 1: DIRECT ALGORITHM NAME MATCHING
        // ==========================================
        if (q.contains("bubble")) {
            return bubbleSortResult();
        }
        if (q.contains("selection")) {
            return selectionSortResult();
        }
        if (q.contains("insertion") || q.contains("card")) {
            return insertionSortResult();
        }
        if (q.contains("merge sort") || q.contains("mergesort")) {
            return mergeSortResult();
        }
        if (q.contains("quick sort") || q.contains("quicksort") || q.contains("partition")) {
            return quickSortResult();
        }
        if (q.contains("heap sort") || q.contains("heapsort") || q.contains("binary heap") || q.contains("max heap") || q.contains("min heap")) {
            return heapSortResult();
        }
        if (q.contains("linear search") || q.contains("sequential search") || q.contains("linear scan")) {
            return linearSearchResult();
        }
        if (q.contains("binary search") || q.contains("bisection") || q.contains("binary")) {
            return binarySearchResult();
        }
        if (q.contains("jump search") || q.contains("block search") || q.contains("jump")) {
            return jumpSearchResult();
        }
        if (q.contains("dijkstra")) {
            return dijkstraResult();
        }
        if (q.contains("kruskal")) {
            return kruskalResult();
        }
        if (q.contains("prim")) {
            return primResult();
        }
        if (q.contains("bfs") || q.contains("breadth")) {
            return bfsResult();
        }
        if (q.contains("dfs") || q.contains("depth")) {
            return dfsResult();
        }

        // ==========================================
        // LAYER 2: GRAPH DOMAIN HEURISTICS
        // ==========================================
        // Minimum Spanning Tree
        if (q.contains("spanning tree") || q.contains("mst") || q.contains("connect all cities") || q.contains("minimum cable")) {
            if (q.contains("edge") || q.contains("sparse") || q.contains("union") || q.contains("disjoint")) {
                return kruskalResult();
            } else {
                return primResult();
            }
        }

        // Weighted Shortest Path / GPS / Routing
        if (q.contains("shortest path") || q.contains("cheapest route") || q.contains("routing") || q.contains("gps") || q.contains("weighted") || q.contains("distance") || q.contains("toll")) {
            return dijkstraResult();
        }

        // Maze / Cycle / Topological / Backtracking
        if (q.contains("maze") || q.contains("backtrack") || q.contains("cycle") || q.contains("topological") || q.contains("component") || q.contains("island") || q.contains("deep")) {
            return dfsResult();
        }

        // Unweighted Shortest Path / Level-by-Level / Social Connections
        if (q.contains("level order") || q.contains("nearest") || q.contains("neighbor") || q.contains("degree") || q.contains("hop") || q.contains("unweighted")) {
            return bfsResult();
        }

        // General Graph queries
        if (q.contains("graph") || q.contains("vertices") || q.contains("vertex") || q.contains("nodes and edges")) {
            if (q.contains("path") || q.contains("cost")) {
                return dijkstraResult();
            } else if (q.contains("tree") || q.contains("cycle")) {
                return dfsResult();
            } else {
                return bfsResult();
            }
        }

        // ==========================================
        // LAYER 3: SEARCHING DOMAIN HEURISTICS
        // ==========================================
        // Unsorted searching
        if ((q.contains("search") || q.contains("find") || q.contains("locate")) && (q.contains("unsorted") || q.contains("raw") || q.contains("random") || q.contains("sequential") || q.contains("brute"))) {
            return linearSearchResult();
        }

        // Sorted searching / Logarithmic
        if ((q.contains("search") || q.contains("find") || q.contains("locate") || q.contains("lookup")) && (q.contains("sorted") || q.contains("ordered") || q.contains("log") || q.contains("divide"))) {
            return binarySearchResult();
        }

        // Jump / Block search
        if (q.contains("square root") || q.contains("sqrt") || q.contains("block")) {
            return jumpSearchResult();
        }

        // General Search / Find query
        if (q.contains("search") || q.contains("find element") || q.contains("lookup") || q.contains("find target") || q.contains("locate")) {
            return binarySearchResult();
        }

        // ==========================================
        // LAYER 4: SORTING DOMAIN HEURISTICS
        // ==========================================
        // Nearly sorted / online
        if (q.contains("nearly") || q.contains("almost sorted") || q.contains("streaming") || q.contains("online") || q.contains("partially sorted")) {
            return insertionSortResult();
        }

        // Stable sorting / guaranteed O(N log N)
        if (q.contains("stable") || q.contains("stability") || q.contains("guarantee") || q.contains("divide and conquer") || q.contains("external")) {
            return mergeSortResult();
        }

        // Priority queue / Kth element / Heap
        if (q.contains("priority") || q.contains("kth") || q.contains("extreme") || q.contains("heap")) {
            return heapSortResult();
        }

        // Minimum memory writes or swaps
        if (q.contains("swap") || q.contains("minimum writes") || q.contains("smallest element")) {
            return selectionSortResult();
        }

        // General sort queries
        if (q.contains("sort") || q.contains("order") || q.contains("arrange") || q.contains("ascending") || q.contains("descending") || q.contains("alphabetical")) {
            return quickSortResult();
        }

        // ==========================================
        // LAYER 5: FALLBACK INTELLIGENT MATCHING
        // ==========================================
        // Check tokens for subtle intentions
        if (q.contains("path") || q.contains("road") || q.contains("node") || q.contains("edge") || q.contains("map") || q.contains("network")) {
            return dijkstraResult();
        }
        if (q.contains("find") || q.contains("target") || q.contains("index") || q.contains("value") || q.contains("key") || q.contains("match")) {
            return binarySearchResult();
        }

        // Default to fast general-purpose Quick Sort
        return defaultRecommendation();
    }

    private RecommendationResult bubbleSortResult() {
        return new RecommendationResult(
            "Bubble Sort",
            "Sorting",
            "Bubble Sort repeatedly swaps adjacent inverted elements. It is simple, intuitive, and primarily used for educational visualization and checking if data is already sorted in O(n) time.",
            "O(n) Best / O(n²) Worst",
            "O(1)"
        );
    }

    private RecommendationResult selectionSortResult() {
        return new RecommendationResult(
            "Selection Sort",
            "Sorting",
            "Selection Sort repeatedly scans the unsorted subarray to find the minimum element and places it in position. It guarantees at most O(n) swaps, making it ideal when write operations are expensive.",
            "O(n²) in all cases",
            "O(1)"
        );
    }

    private RecommendationResult insertionSortResult() {
        return new RecommendationResult(
            "Insertion Sort",
            "Sorting",
            "Insertion Sort builds the sorted array one element at a time like arranging cards. It achieves O(n) linear performance on nearly sorted or small datasets and supports streaming online input.",
            "O(n) Best / O(n²) Worst",
            "O(1)"
        );
    }

    private RecommendationResult mergeSortResult() {
        return new RecommendationResult(
            "Merge Sort",
            "Sorting",
            "Merge Sort is a stable divide-and-conquer algorithm with guaranteed O(n log n) runtime across all best, average, and worst cases, ideal for linked lists and critical systems requiring stability.",
            "O(n log n) in all cases",
            "O(n)"
        );
    }

    private RecommendationResult quickSortResult() {
        return new RecommendationResult(
            "Quick Sort",
            "Sorting",
            "Quick Sort partitions elements around a selected pivot in-place. It is typically the fastest general-purpose sorting algorithm in practice with exceptional CPU cache locality.",
            "O(n log n) Avg / O(n²) Worst",
            "O(log n)"
        );
    }

    private RecommendationResult heapSortResult() {
        return new RecommendationResult(
            "Heap Sort",
            "Sorting",
            "Heap Sort builds a Binary Max Heap to repeatedly extract maximal elements in-place with guaranteed O(n log n) time without requiring auxiliary memory.",
            "O(n log n) in all cases",
            "O(1)"
        );
    }

    private RecommendationResult linearSearchResult() {
        return new RecommendationResult(
            "Linear Search",
            "Searching",
            "Linear Search inspects every element sequentially from left to right. It is required when the input dataset is unsorted or cannot be indexed directly.",
            "O(1) Best / O(n) Worst",
            "O(1)"
        );
    }

    private RecommendationResult binarySearchResult() {
        return new RecommendationResult(
            "Binary Search",
            "Searching",
            "Binary Search repeatedly divides the sorted search space in half by comparing the middle element, locating items in rapid logarithmic O(log n) time.",
            "O(1) Best / O(log n) Worst",
            "O(1)"
        );
    }

    private RecommendationResult jumpSearchResult() {
        return new RecommendationResult(
            "Jump Search",
            "Searching",
            "Jump Search skips ahead by fixed √n blocks in a sorted array before performing a local linear search, requiring fewer comparisons than linear search with lower overhead than binary search.",
            "O(1) Best / O(√n) Worst",
            "O(1)"
        );
    }

    private RecommendationResult dijkstraResult() {
        return new RecommendationResult(
            "Dijkstra's Algorithm",
            "Graph",
            "Dijkstra's Algorithm computes the single-source shortest path in weighted graphs with non-negative edge weights using a greedy priority queue approach.",
            "O((V + E) log V)",
            "O(V)"
        );
    }

    private RecommendationResult bfsResult() {
        return new RecommendationResult(
            "Breadth-First Search (BFS)",
            "Graph",
            "BFS traverses graphs level-by-level using a FIFO queue. It is optimal for unweighted shortest path routing, finding nearest neighbors, and level-order traversals.",
            "O(V + E) in all cases",
            "O(V)"
        );
    }

    private RecommendationResult dfsResult() {
        return new RecommendationResult(
            "Depth-First Search (DFS)",
            "Graph",
            "DFS explores as far as possible along each branch before backtracking using a stack. Optimal for maze pathfinding, cycle detection, connected components, and topological sorting.",
            "O(V + E) in all cases",
            "O(V)"
        );
    }

    private RecommendationResult primResult() {
        return new RecommendationResult(
            "Prim's Algorithm",
            "Graph",
            "Prim's Algorithm grows a single Minimum Spanning Tree (MST) from an arbitrary root by repeatedly adding the cheapest edge across the cut. Best suited for dense graphs.",
            "O(E log V)",
            "O(V)"
        );
    }

    private RecommendationResult kruskalResult() {
        return new RecommendationResult(
            "Kruskal's Algorithm",
            "Graph",
            "Kruskal's Algorithm constructs an MST by sorting all edges by weight and joining components using a Disjoint Set (Union-Find) without forming cycles. Ideal for sparse graphs.",
            "O(E log E)",
            "O(V + E)"
        );
    }

    private RecommendationResult defaultRecommendation() {
        return new RecommendationResult(
            "Quick Sort",
            "Sorting",
            "Quick Sort is an optimal, high-performance general-purpose sorting algorithm. If you need specific search or graph analysis, describe your data structure or problem statement.",
            "O(n log n) Avg",
            "O(log n)"
        );
    }
}
