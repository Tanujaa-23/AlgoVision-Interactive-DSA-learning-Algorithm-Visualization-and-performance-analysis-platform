package com.algovision.dao;

import com.algovision.model.Achievement;
import com.algovision.model.UserProgress;
import com.algovision.util.DBConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProgressDAO {

    private static final File STORAGE_FILE = new File(System.getProperty("user.home") + File.separator + "algovision_progress.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final Map<Integer, Map<String, UserProgress>> memoryProgress = new ConcurrentHashMap<>();
    private static final List<Achievement> allAchievements = new ArrayList<>();

    static {
        allAchievements.add(createAchievement(1, "first_algo", "First Algorithm", "Visualized your first algorithm on AlgoVision Pro", "bi bi-lightning-charge-fill"));
        allAchievements.add(createAchievement(2, "sorting_master", "Sorting Master", "Executed all 6 sorting algorithms step-by-step", "bi bi-sort-numeric-down"));
        allAchievements.add(createAchievement(3, "graph_explorer", "Graph Explorer", "Ran graph traversals and shortest path algorithms", "bi bi-diagram-3-fill"));
        allAchievements.add(createAchievement(4, "quiz_master", "Quiz Master", "Scored 100% on a DSA topic quiz", "bi bi-patch-check-fill"));
        allAchievements.add(createAchievement(5, "challenge_solver", "Problem Solver", "Solved 10 coding challenges successfully", "bi bi-code-slash"));
        allAchievements.add(createAchievement(6, "streak_7", "7 Day Streak", "Logged in and practiced DSA for 7 consecutive days", "bi bi-fire"));

        loadProgressFromFile();
    }

    private static synchronized void saveProgressToFile() {
        try {
            if (STORAGE_FILE.getParentFile() != null && !STORAGE_FILE.getParentFile().exists()) {
                STORAGE_FILE.getParentFile().mkdirs();
            }
            try (Writer writer = new FileWriter(STORAGE_FILE)) {
                gson.toJson(memoryProgress, writer);
            }
        } catch (Exception e) {
            System.err.println("[ProgressDAO] Warning: Failed to save progress records: " + e.getMessage());
        }
    }

    private static final Map<Integer, Map<String, Set<String>>> userCompletedAlgorithms = new ConcurrentHashMap<>();

    private static synchronized void loadProgressFromFile() {
        if (!STORAGE_FILE.exists()) {
            return;
        }
        try (Reader reader = new FileReader(STORAGE_FILE)) {
            Type mapType = new TypeToken<Map<Integer, Map<String, UserProgress>>>() {}.getType();
            Map<Integer, Map<String, UserProgress>> loaded = gson.fromJson(reader, mapType);
            if (loaded != null) {
                // If there were records under user 0, merge into user 2 (student)
                if (loaded.containsKey(0)) {
                    Map<String, UserProgress> u0 = loaded.get(0);
                    Map<String, UserProgress> u2 = loaded.computeIfAbsent(2, k -> new LinkedHashMap<>());
                    for (Map.Entry<String, UserProgress> entry : u0.entrySet()) {
                        String topic = entry.getKey();
                        UserProgress p0 = entry.getValue();
                        int limit = getTopicLimit(topic);
                        int count = Math.min(limit, p0.getCompletedCount());
                        UserProgress p2 = u2.get(topic);
                        if (p2 == null) {
                            p2 = new UserProgress(2, topic, count, limit);
                            u2.put(topic, p2);
                        } else {
                            p2.setCompletedCount(Math.min(limit, Math.max(p2.getCompletedCount(), count)));
                            p2.setTotalCount(limit);
                        }
                    }
                }
                memoryProgress.putAll(loaded);
            }
        } catch (Exception e) {
            System.err.println("[ProgressDAO] Warning: Failed to load progress records: " + e.getMessage());
        }
    }

    public static int getTopicLimit(String topic) {
        if ("Sorting".equalsIgnoreCase(topic)) return 6;
        if ("Searching".equalsIgnoreCase(topic)) return 3;
        if ("Graph".equalsIgnoreCase(topic)) return 5;
        if ("Data Structures".equalsIgnoreCase(topic)) return 4;
        return 5;
    }

    private static Achievement createAchievement(int id, String key, String title, String desc, String icon) {
        Achievement a = new Achievement();
        a.setId(id);
        a.setBadgeKey(key);
        a.setTitle(title);
        a.setDescription(desc);
        a.setIconClass(icon);
        return a;
    }

    private void ensureDefaultTopics(int userId) {
        int targetUser = userId <= 0 ? 2 : userId;
        memoryProgress.computeIfAbsent(targetUser, k -> {
            Map<String, UserProgress> map = new LinkedHashMap<>();
            map.put("Sorting", new UserProgress(targetUser, "Sorting", 0, 6));
            map.put("Searching", new UserProgress(targetUser, "Searching", 0, 3));
            map.put("Graph", new UserProgress(targetUser, "Graph", 0, 5));
            map.put("Data Structures", new UserProgress(targetUser, "Data Structures", 0, 4));
            return map;
        });
    }

    public void recordAlgorithmProgress(int userId, String topic, String algoName) {
        int targetUser = userId <= 0 ? 2 : userId;
        ensureDefaultTopics(targetUser);

        int totalLimit = getTopicLimit(topic);
        if (algoName != null && !algoName.trim().isEmpty()) {
            userCompletedAlgorithms
                .computeIfAbsent(targetUser, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(topic, t -> ConcurrentHashMap.newKeySet())
                .add(algoName.trim().toLowerCase());
        }

        Map<String, UserProgress> userMap = memoryProgress.get(targetUser);
        UserProgress current = userMap.get(topic);
        int distinctDone = userCompletedAlgorithms
            .getOrDefault(targetUser, Collections.emptyMap())
            .getOrDefault(topic, Collections.emptySet()).size();

        int accurateCount = Math.min(totalLimit, Math.max(1, distinctDone));
        if (current == null) {
            current = new UserProgress(targetUser, topic, accurateCount, totalLimit);
            userMap.put(topic, current);
        } else {
            current.setCompletedCount(Math.min(totalLimit, Math.max(current.getCompletedCount(), accurateCount)));
            current.setTotalCount(totalLimit);
        }

        if (targetUser == 2) {
            memoryProgress.put(1, userMap);
        }

        saveProgressToFile();
    }

    public void incrementTopicProgress(int userId, String topic) {
        recordAlgorithmProgress(userId, topic, "action_" + System.currentTimeMillis());
    }

    public List<UserProgress> getUserProgressList(int userId) {
        int targetUser = userId <= 0 ? 2 : userId;
        ensureDefaultTopics(targetUser);

        // Sync with actual executions performed in ExecutionDAO
        List<com.algovision.model.Execution> execs = ExecutionDAO.getMemoryExecutions();
        for (com.algovision.model.Execution ex : execs) {
            if (ex.getUserId() == targetUser || ex.getUserId() <= 0 || (targetUser == 2 && ex.getUserId() == 1) || (targetUser == 1 && ex.getUserId() == 2)) {
                String cat = ex.getCategory();
                String name = ex.getAlgorithmName();
                if (cat != null && name != null) {
                    userCompletedAlgorithms
                        .computeIfAbsent(targetUser, k -> new ConcurrentHashMap<>())
                        .computeIfAbsent(cat, t -> ConcurrentHashMap.newKeySet())
                        .add(name.trim().toLowerCase());
                }
            }
        }

        Map<String, UserProgress> memoryMap = memoryProgress.get(targetUser);
        for (Map.Entry<String, UserProgress> entry : memoryMap.entrySet()) {
            String topic = entry.getKey();
            UserProgress up = entry.getValue();
            int totalLimit = getTopicLimit(topic);
            up.setTotalCount(totalLimit);

            Set<String> distinctDone = userCompletedAlgorithms
                .getOrDefault(targetUser, Collections.emptyMap())
                .getOrDefault(topic, Collections.emptySet());

            int accurateCount = Math.min(totalLimit, Math.max(up.getCompletedCount(), distinctDone.size()));
            up.setCompletedCount(accurateCount);
        }

        saveProgressToFile();
        return new ArrayList<>(memoryMap.values());
    }

    public List<Achievement> getUserAchievements(int userId) {
        int targetUser = userId <= 0 ? 2 : userId;
        List<Achievement> result = new ArrayList<>();
        Set<String> earnedKeys = new HashSet<>();

        List<UserProgress> userProgress = getUserProgressList(targetUser);
        for (UserProgress p : userProgress) {
            if (p.getCompletedCount() > 0) {
                earnedKeys.add("first_algo");
            }
            if ("Sorting".equalsIgnoreCase(p.getTopic()) && p.getCompletedCount() >= 6) {
                earnedKeys.add("sorting_master");
            }
            if ("Graph".equalsIgnoreCase(p.getTopic()) && p.getCompletedCount() >= 1) {
                earnedKeys.add("graph_explorer");
            }
        }

        earnedKeys.add("streak_7"); // Demo student has 7 day streak

        String sql = "SELECT a.badge_key, ua.earned_at FROM user_achievements ua JOIN achievements a ON ua.achievement_id = a.id WHERE ua.user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, targetUser);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    earnedKeys.add(rs.getString("badge_key"));
                }
            }
        } catch (Exception e) {
            // Handled in memory
        }

        for (Achievement ach : allAchievements) {
            Achievement copy = createAchievement(ach.getId(), ach.getBadgeKey(), ach.getTitle(), ach.getDescription(), ach.getIconClass());
            if (earnedKeys.contains(ach.getBadgeKey())) {
                copy.setEarned(true);
            }
            result.add(copy);
        }
        return result;
    }
}
