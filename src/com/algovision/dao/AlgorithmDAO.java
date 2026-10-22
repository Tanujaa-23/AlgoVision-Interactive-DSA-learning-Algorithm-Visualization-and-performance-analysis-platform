package com.algovision.dao;

import com.algovision.model.Algorithm;
import com.algovision.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DAO Pattern implementation for Algorithm information management.
 */
public class AlgorithmDAO {

    private static final Map<String, Algorithm> memoryAlgos = new ConcurrentHashMap<>();

    static {
        seedMemory();
    }

    private static void seedMemory() {
        addMem("Bubble Sort", "Sorting", "Bubble Sort repeatedly steps through the list, compares adjacent elements and swaps them if they are in the wrong order.", "Iterates through array comparing arr[j] and arr[j+1], swapping when needed until array is sorted.", "O(n)", "O(n²)", "O(n²)", "O(1)", true, true);
        addMem("Selection Sort", "Sorting", "Selection Sort divides the array into sorted and unsorted regions, repeatedly selecting the smallest unsorted element.", "Finds minimum element in unsorted portion and swaps with first element of unsorted portion.", "O(n²)", "O(n²)", "O(n²)", "O(1)", false, true);
        addMem("Insertion Sort", "Sorting", "Insertion Sort builds the sorted array one element at a time by inserting elements into correct position.", "Iterates through elements and shifts larger elements to right to insert key.", "O(n)", "O(n²)", "O(n²)", "O(1)", true, true);
        addMem("Merge Sort", "Sorting", "Merge Sort is an efficient, divide-and-conquer sorting algorithm.", "Recursively splits array in half, sorts sub-arrays, and merges them.", "O(n log n)", "O(n log n)", "O(n log n)", "O(n)", true, false);
        addMem("Quick Sort", "Sorting", "Quick Sort partitions array around a chosen pivot element recursively.", "Picks pivot, places smaller elements to left and greater to right.", "O(n log n)", "O(n log n)", "O(n²)", "O(log n)", false, true);
        addMem("Heap Sort", "Sorting", "Heap Sort builds a Binary Max Heap and repeatedly extracts maximum value.", "Transforms array into max heap, extracts root to end, and heapifies.", "O(n log n)", "O(n log n)", "O(n log n)", "O(1)", false, true);

        addMem("Linear Search", "Searching", "Linear Search checks every element sequentially until target is found.", "Loops from index 0 to n-1 matching target value.", "O(1)", "O(n)", "O(n)", "O(1)", true, true);
        addMem("Binary Search", "Searching", "Binary Search divides sorted search space in half repeatedly.", "Compares target with middle element; eliminates half the array each step.", "O(1)", "O(log n)", "O(log n)", "O(1)", true, true);
        addMem("Jump Search", "Searching", "Jump Search checks fixed block steps in a sorted array then performs linear search.", "Jumps ahead by √n steps, then linear searches backward.", "O(1)", "O(√n)", "O(√n)", "O(1)", true, true);

        addMem("Breadth-First Search (BFS)", "Graph", "BFS traverses graph level by level using a FIFO queue.", "Visits source, enqueues neighbors, explores breadth before depth.", "O(V + E)", "O(V + E)", "O(V + E)", "O(V)", true, false);
        addMem("Depth-First Search (DFS)", "Graph", "DFS explores graph branches deeply before backtracking using a stack.", "Visits source, explores deep path recursively until dead end.", "O(V + E)", "O(V + E)", "O(V + E)", "O(V)", true, false);
        addMem("Dijkstra Shortest Path", "Graph", "Dijkstra computes single-source shortest path in weighted graph with non-negative weights.", "Uses Priority Queue to greedily pick closest unvisited node and relax edges.", "O((V + E) log V)", "O((V + E) log V)", "O((V + E) log V)", "O(V)", true, false);
        addMem("Prim Minimum Spanning Tree", "Graph", "Prim builds minimum spanning tree for connected weighted undirected graph.", "Grows MST vertex by vertex adding lowest weight edge across cut.", "O(E log V)", "O(E log V)", "O(E log V)", "O(V)", true, false);
        addMem("Kruskal MST", "Graph", "Kruskal builds MST by sorting edges and joining components with Disjoint Set.", "Sorts edges by weight, adds edge if it does not form a cycle.", "O(E log E)", "O(E log E)", "O(E log E)", "O(V + E)", true, false);
    }

    private static void addMem(String name, String cat, String desc, String how, String best, String avg, String worst, String space, boolean stable, boolean inPlace) {
        Algorithm a = new Algorithm(memoryAlgos.size() + 1, name, cat, desc, best, avg, worst, space, stable, inPlace);
        a.setHowItWorks(how);
        memoryAlgos.put(name.toLowerCase(), a);
    }

    public List<Algorithm> getAllAlgorithms() {
        List<Algorithm> list = new ArrayList<>();
        String sql = "SELECT * FROM algorithms ORDER BY category, name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Algorithm a = mapResultSet(rs);
                list.add(a);
            }
        } catch (Exception e) {
            return new ArrayList<>(memoryAlgos.values());
        }
        return list.isEmpty() ? new ArrayList<>(memoryAlgos.values()) : list;
    }

    public List<Algorithm> getAlgorithmsByCategory(String category) {
        List<Algorithm> list = new ArrayList<>();
        String sql = "SELECT * FROM algorithms WHERE LOWER(category) = LOWER(?) ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSet(rs));
                }
            }
        } catch (Exception e) {
            for (Algorithm a : memoryAlgos.values()) {
                if (a.getCategory().equalsIgnoreCase(category)) {
                    list.add(a);
                }
            }
        }
        return list;
    }

    public Algorithm getAlgorithmByName(String name) {
        if (name == null) return null;
        String trimmed = name.trim();
        String sql = "SELECT * FROM algorithms WHERE LOWER(name) = LOWER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trimmed);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapResultSet(rs);
            }
        } catch (Exception e) {
            // fallback
        }
        
        // Exact match from memory
        Algorithm exact = memoryAlgos.get(trimmed.toLowerCase());
        if (exact != null) return exact;

        // Substring / alias match from memory
        String lower = trimmed.toLowerCase();
        for (Map.Entry<String, Algorithm> entry : memoryAlgos.entrySet()) {
            String key = entry.getKey();
            if (key.equalsIgnoreCase(lower) || key.contains(lower) || lower.contains(key)) {
                return entry.getValue();
            }
        }

        // Specific acronym & naming aliases
        if (lower.equals("bfs") || lower.contains("breadth")) {
            return memoryAlgos.get("breadth-first search (bfs)");
        }
        if (lower.equals("dfs") || lower.contains("depth")) {
            return memoryAlgos.get("depth-first search (dfs)");
        }
        if (lower.contains("dijkstra")) {
            return memoryAlgos.get("dijkstra shortest path");
        }
        if (lower.contains("prim")) {
            return memoryAlgos.get("prim minimum spanning tree");
        }
        if (lower.contains("kruskal")) {
            return memoryAlgos.get("kruskal mst");
        }
        if (lower.contains("binary")) {
            return memoryAlgos.get("binary search");
        }
        if (lower.contains("linear")) {
            return memoryAlgos.get("linear search");
        }
        if (lower.contains("jump")) {
            return memoryAlgos.get("jump search");
        }

        return null;
    }

    public boolean addAlgorithm(Algorithm a) {
        String sql = "INSERT INTO algorithms (name, category, description, how_it_works, complexity_best, complexity_avg, complexity_worst, space_complexity, is_stable, is_in_place) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, a.getName());
            pstmt.setString(2, a.getCategory());
            pstmt.setString(3, a.getDescription());
            pstmt.setString(4, a.getHowItWorks());
            pstmt.setString(5, a.getComplexityBest());
            pstmt.setString(6, a.getComplexityAvg());
            pstmt.setString(7, a.getComplexityWorst());
            pstmt.setString(8, a.getSpaceComplexity());
            pstmt.setBoolean(9, a.isStable());
            pstmt.setBoolean(10, a.isInPlace());
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                memoryAlgos.put(a.getName().toLowerCase(), a);
                return true;
            }
        } catch (Exception e) {
            memoryAlgos.put(a.getName().toLowerCase(), a);
            return true;
        }
        return false;
    }

    private Algorithm mapResultSet(ResultSet rs) throws SQLException {
        Algorithm a = new Algorithm();
        a.setId(rs.getInt("id"));
        a.setName(rs.getString("name"));
        a.setCategory(rs.getString("category"));
        a.setDescription(rs.getString("description"));
        a.setHowItWorks(rs.getString("how_it_works"));
        a.setComplexityBest(rs.getString("complexity_best"));
        a.setComplexityAvg(rs.getString("complexity_avg"));
        a.setComplexityWorst(rs.getString("complexity_worst"));
        a.setSpaceComplexity(rs.getString("space_complexity"));
        a.setStable(rs.getBoolean("is_stable"));
        a.setInPlace(rs.getBoolean("is_in_place"));
        return a;
    }
}
