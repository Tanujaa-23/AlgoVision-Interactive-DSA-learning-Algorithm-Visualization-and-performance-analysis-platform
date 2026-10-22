package com.algovision.dao;

import com.algovision.model.Challenge;
import com.algovision.model.Submission;
import com.algovision.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChallengeDAO {

    private static final List<Challenge> memoryChallenges = new ArrayList<>();
    private static final List<Submission> memorySubmissions = new ArrayList<>();

    static {
        seedChallenges();
    }

    private static void seedChallenges() {
        addC("Find Maximum Element", "Given an array of space-separated integers, find and return the maximum element.", "Easy", "Arrays", "Space-separated integers", "Single integer", "1 <= N <= 1000", "5 2 9 1 7", "9", "9");
        addC("Check Array Sorted", "Write a program to determine if an array of integers is sorted in non-decreasing order.", "Easy", "Arrays", "Space-separated integers", "true or false", "1 <= N <= 1000", "1 3 5 8 10", "true", "true");
        addC("Binary Search Implementation", "Given a sorted array and a target integer, return 0-based index or -1 if missing.", "Medium", "Searching", "Line 1: space separated integers. Line 2: target integer.", "Index integer", "1 <= N <= 10^5", "2 5 8 12 16 23\n12", "3", "3");
        addC("Reverse Linked List", "Given values of a singly linked list, return reversed space-separated values.", "Medium", "LinkedList", "Space-separated integers", "Space-separated integers", "1 <= N <= 1000", "1 2 3 4 5", "5 4 3 2 1", "5 4 3 2 1");
    }

    private static void addC(String title, String desc, String diff, String topic, String inf, String outf, String cons, String sampIn, String sampOut, String expOut) {
        Challenge c = new Challenge();
        c.setId(memoryChallenges.size() + 1);
        c.setTitle(title);
        c.setDescription(desc);
        c.setDifficulty(diff);
        c.setTopic(topic);
        c.setInputFormat(inf);
        c.setOutputFormat(outf);
        c.setConstraints(cons);
        c.setSampleInput(sampIn);
        c.setSampleOutput(sampOut);
        c.setExpectedOutput(expOut);
        memoryChallenges.add(c);
    }

    public List<Challenge> getAllChallenges() {
        List<Challenge> list = new ArrayList<>();
        String sql = "SELECT * FROM challenges ORDER BY id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapChallenge(rs));
            }
        } catch (Exception e) {
            return new ArrayList<>(memoryChallenges);
        }
        return list.isEmpty() ? new ArrayList<>(memoryChallenges) : list;
    }

    public Challenge getChallengeById(int id) {
        String sql = "SELECT * FROM challenges WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return mapChallenge(rs);
            }
        } catch (Exception e) {
            // fallback
        }
        for (Challenge c : memoryChallenges) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public boolean recordSubmission(Submission sub) {
        String sql = "INSERT INTO submissions (user_id, challenge_id, user_code, status, execution_time_ms) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, sub.getUserId());
            pstmt.setInt(2, sub.getChallengeId());
            pstmt.setString(3, sub.getUserCode());
            pstmt.setString(4, sub.getStatus());
            pstmt.setDouble(5, sub.getExecutionTimeMs());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) sub.setId(rs.getInt(1));
                }
                memorySubmissions.add(sub);
                return true;
            }
        } catch (Exception e) {
            sub.setId(memorySubmissions.size() + 1);
            memorySubmissions.add(sub);
            return true;
        }
        return false;
    }

    public List<Submission> getSubmissionsByUser(int userId) {
        List<Submission> list = new ArrayList<>();
        String sql = "SELECT * FROM submissions WHERE user_id = ? ORDER BY submitted_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Submission s = new Submission();
                    s.setId(rs.getInt("id"));
                    s.setUserId(rs.getInt("user_id"));
                    s.setChallengeId(rs.getInt("challenge_id"));
                    s.setUserCode(rs.getString("user_code"));
                    s.setStatus(rs.getString("status"));
                    s.setExecutionTimeMs(rs.getDouble("execution_time_ms"));
                    s.setSubmittedAt(rs.getTimestamp("submitted_at"));
                    list.add(s);
                }
            }
        } catch (Exception e) {
            for (Submission s : memorySubmissions) {
                if (s.getUserId() == userId) list.add(s);
            }
        }
        return list;
    }

    private Challenge mapChallenge(ResultSet rs) throws SQLException {
        Challenge c = new Challenge();
        c.setId(rs.getInt("id"));
        c.setTitle(rs.getString("title"));
        c.setDescription(rs.getString("description"));
        c.setDifficulty(rs.getString("difficulty"));
        c.setTopic(rs.getString("topic"));
        c.setInputFormat(rs.getString("input_format"));
        c.setOutputFormat(rs.getString("output_format"));
        c.setConstraints(rs.getString("constraints"));
        c.setSampleInput(rs.getString("sample_input"));
        c.setSampleOutput(rs.getString("sample_output"));
        c.setExpectedOutput(rs.getString("expected_output"));
        return c;
    }
}
