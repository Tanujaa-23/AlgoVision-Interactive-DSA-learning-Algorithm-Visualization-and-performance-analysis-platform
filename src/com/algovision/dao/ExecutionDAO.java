package com.algovision.dao;

import com.algovision.model.Execution;
import com.algovision.util.DBConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * DAO Pattern implementation for recorded algorithm execution metrics
 * with automated persistent file storage fallback across restarts.
 */
public class ExecutionDAO {

    private static final File STORAGE_FILE = new File(System.getProperty("user.home") + File.separator + "algovision_executions.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final List<Execution> memoryExecutions = new CopyOnWriteArrayList<>();

    static {
        loadExecutionsFromFile();
        boolean hasGraph = false;
        for (Execution ex : memoryExecutions) {
            if ("Graph".equalsIgnoreCase(ex.getCategory())) {
                hasGraph = true;
                break;
            }
        }
        if (memoryExecutions.isEmpty() || !hasGraph) {
            seedDefaultDemoExecutions();
        }
    }

    private static synchronized void saveExecutionsToFile() {
        try {
            if (STORAGE_FILE.getParentFile() != null && !STORAGE_FILE.getParentFile().exists()) {
                STORAGE_FILE.getParentFile().mkdirs();
            }
            try (Writer writer = new FileWriter(STORAGE_FILE)) {
                gson.toJson(memoryExecutions, writer);
            }
        } catch (Exception e) {
            System.err.println("[ExecutionDAO] Warning: Failed to save execution history: " + e.getMessage());
        }
    }

    public static synchronized void loadExecutionsFromFile() {
        if (!STORAGE_FILE.exists()) return;
        try (Reader reader = new FileReader(STORAGE_FILE)) {
            Type listType = new TypeToken<List<Execution>>() {}.getType();
            List<Execution> loaded = gson.fromJson(reader, listType);
            if (loaded != null && !loaded.isEmpty()) {
                memoryExecutions.clear();
                memoryExecutions.addAll(loaded);
            }
        } catch (Exception e) {
            System.err.println("[ExecutionDAO] Warning: Failed to load execution history: " + e.getMessage());
        }
    }

    private static void seedDefaultDemoExecutions() {
        // Only seed if empty or missing graph executions
        List<Execution> seeded = new ArrayList<>();
        long now = System.currentTimeMillis();

        Execution e1 = new Execution();
        e1.setId(1);
        e1.setUserId(2);
        e1.setAlgorithmName("Bubble Sort");
        e1.setInputData("5, 2, 8, 1, 3");
        e1.setStepCount(12);
        e1.setComparisonCount(10);
        e1.setSwapCount(4);
        e1.setExecutionTimeMs(1.20);
        e1.setExecutedAt(new Timestamp(now - 7200000));
        seeded.add(e1);

        Execution e2 = new Execution();
        e2.setId(2);
        e2.setUserId(2);
        e2.setAlgorithmName("Binary Search");
        e2.setInputData("Target: 8, Array: [1, 2, 5, 8, 12, 16]");
        e2.setStepCount(4);
        e2.setComparisonCount(3);
        e2.setSwapCount(0);
        e2.setExecutionTimeMs(0.45);
        e2.setExecutedAt(new Timestamp(now - 5400000));
        seeded.add(e2);

        Execution e3 = new Execution();
        e3.setId(3);
        e3.setUserId(2);
        e3.setAlgorithmName("Breadth-First Search (BFS)");
        e3.setInputData("Start: A, Graph: A-B(4), A-C(2), B-D(5), C-D(8)");
        e3.setStepCount(4);
        e3.setComparisonCount(4);
        e3.setSwapCount(0);
        e3.setExecutionTimeMs(2.10);
        e3.setExecutedAt(new Timestamp(now - 3600000));
        seeded.add(e3);

        Execution e4 = new Execution();
        e4.setId(4);
        e4.setUserId(2);
        e4.setAlgorithmName("Quick Sort");
        e4.setInputData("[29, 10, 14, 37, 13]");
        e4.setStepCount(16);
        e4.setComparisonCount(14);
        e4.setSwapCount(5);
        e4.setExecutionTimeMs(0.85);
        e4.setExecutedAt(new Timestamp(now - 1800000));
        seeded.add(e4);

        Execution e5 = new Execution();
        e5.setId(5);
        e5.setUserId(2);
        e5.setAlgorithmName("Dijkstra Shortest Path");
        e5.setInputData("Start: A, Target: D, Weights: Non-negative");
        e5.setStepCount(5);
        e5.setComparisonCount(5);
        e5.setSwapCount(0);
        e5.setExecutionTimeMs(1.75);
        e5.setExecutedAt(new Timestamp(now - 600000));
        seeded.add(e5);

        for (Execution se : seeded) {
            boolean exists = false;
            for (Execution ex : memoryExecutions) {
                if (ex.getAlgorithmName().equalsIgnoreCase(se.getAlgorithmName())) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                se.setId(memoryExecutions.size() + 1);
                memoryExecutions.add(se);
            }
        }

        saveExecutionsToFile();
    }

    public static List<Execution> getMemoryExecutions() {
        loadExecutionsFromFile();
        return Collections.unmodifiableList(memoryExecutions);
    }

    public boolean recordExecution(Execution exec) {
        if (exec.getUserId() <= 0) {
            exec.setUserId(2);
        }
        if (exec.getExecutedAt() == null) {
            exec.setExecutedAt(new Timestamp(System.currentTimeMillis()));
        }

        loadExecutionsFromFile();

        String sql = "INSERT INTO algorithm_executions (user_id, algorithm_name, input_data, step_count, comparison_count, swap_count, execution_time_ms) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, exec.getUserId());
            pstmt.setString(2, exec.getAlgorithmName());
            pstmt.setString(3, exec.getInputData());
            pstmt.setInt(4, exec.getStepCount());
            pstmt.setInt(5, exec.getComparisonCount());
            pstmt.setInt(6, exec.getSwapCount());
            pstmt.setDouble(7, exec.getExecutionTimeMs());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) exec.setId(rs.getInt(1));
                }
                memoryExecutions.add(exec);
                saveExecutionsToFile();
                return true;
            }
        } catch (Exception e) {
            // DB fallback
        }

        exec.setId(memoryExecutions.size() + 1);
        memoryExecutions.add(exec);
        saveExecutionsToFile();
        return true;
    }

    public List<Execution> getRecentExecutionsByUser(int userId, int limit) {
        loadExecutionsFromFile();
        List<Execution> list = new ArrayList<>();
        String sql = "SELECT * FROM algorithm_executions WHERE user_id = ? ORDER BY executed_at DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Execution e = new Execution();
                    e.setId(rs.getInt("id"));
                    e.setUserId(rs.getInt("user_id"));
                    e.setAlgorithmName(rs.getString("algorithm_name"));
                    e.setInputData(rs.getString("input_data"));
                    e.setStepCount(rs.getInt("step_count"));
                    e.setComparisonCount(rs.getInt("comparison_count"));
                    e.setSwapCount(rs.getInt("swap_count"));
                    e.setExecutionTimeMs(rs.getDouble("execution_time_ms"));
                    e.setExecutedAt(rs.getTimestamp("executed_at"));
                    list.add(e);
                }
            }
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            // DB fallback
        }

        for (int i = memoryExecutions.size() - 1; i >= 0 && list.size() < limit; i--) {
            Execution ex = memoryExecutions.get(i);
            if (ex.getUserId() == userId) {
                list.add(ex);
            } else if (userId == 2 && (ex.getUserId() == 1 || ex.getUserId() <= 0)) {
                list.add(ex);
            }
        }
        return list;
    }

    public int getDistinctAlgorithmCountByUser(int userId) {
        loadExecutionsFromFile();
        Set<String> distinct = new HashSet<>();
        String sql = "SELECT DISTINCT algorithm_name FROM algorithm_executions WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("algorithm_name");
                    if (name != null && !name.trim().isEmpty()) {
                        distinct.add(name.trim().toLowerCase());
                    }
                }
            }
        } catch (Exception e) {
            // DB fallback
        }

        for (Execution ex : memoryExecutions) {
            if (ex.getUserId() == userId) {
                if (ex.getAlgorithmName() != null && !ex.getAlgorithmName().trim().isEmpty()) {
                    distinct.add(ex.getAlgorithmName().trim().toLowerCase());
                }
            } else if (userId == 2 && (ex.getUserId() == 1 || ex.getUserId() <= 0)) {
                if (ex.getAlgorithmName() != null && !ex.getAlgorithmName().trim().isEmpty()) {
                    distinct.add(ex.getAlgorithmName().trim().toLowerCase());
                }
            }
        }
        return distinct.size();
    }

    public int getTotalExecutionCountByUser(int userId) {
        loadExecutionsFromFile();
        int dbCount = 0;
        boolean dbSuccess = false;
        String sql = "SELECT COUNT(*) FROM algorithm_executions WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    dbCount = rs.getInt(1);
                    dbSuccess = true;
                }
            }
        } catch (Exception e) {
            // DB fallback
        }

        int memCount = 0;
        for (Execution ex : memoryExecutions) {
            if (ex.getUserId() == userId) {
                memCount++;
            } else if (userId == 2 && (ex.getUserId() == 1 || ex.getUserId() <= 0)) {
                memCount++;
            }
        }
        if (dbSuccess) {
            return Math.max(dbCount, memCount);
        }
        return memCount;
    }

    public int getTotalExecutionCount() {
        loadExecutionsFromFile();
        String sql = "SELECT COUNT(*) FROM algorithm_executions";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next() && rs.getInt(1) > 0) return rs.getInt(1);
        } catch (Exception e) {
            return memoryExecutions.size();
        }
        return memoryExecutions.size();
    }
}
