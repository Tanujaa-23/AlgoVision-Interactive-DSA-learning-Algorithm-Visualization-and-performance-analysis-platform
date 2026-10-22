package com.algovision.dao;

import com.algovision.model.Question;
import com.algovision.model.QuizResult;
import com.algovision.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    private static final List<Question> memoryQuestions = new ArrayList<>();
    private static final List<QuizResult> memoryResults = new ArrayList<>();

    static {
        seedQuestions();
    }

    private static void seedQuestions() {
        addQ("What is the worst-case time complexity of Quick Sort?", "O(n log n)", "O(n)", "O(n²)", "O(2ⁿ)", "C", "Quick Sort has worst case O(n²) when bad pivots are selected.", "Sorting", "Easy");
        addQ("Which sorting algorithm guarantees O(n log n) worst-case time complexity and is stable?", "Quick Sort", "Merge Sort", "Heap Sort", "Bubble Sort", "B", "Merge Sort is always O(n log n) and stable.", "Sorting", "Medium");
        addQ("What data structure is primarily used to implement Breadth-First Search (BFS)?", "Stack", "Queue", "Priority Queue", "Binary Search Tree", "B", "BFS uses a FIFO Queue for level order traversal.", "Graph", "Easy");
        addQ("What is the space complexity of Heap Sort?", "O(n)", "O(n log n)", "O(1)", "O(log n)", "C", "Heap Sort is an in-place algorithm with O(1) auxiliary space.", "Sorting", "Medium");
        addQ("In Dijkstra algorithm, what condition must edge weights satisfy?", "Negative weights allowed", "Non-negative edge weights", "Graph must be unweighted", "Graph must be tree", "B", "Dijkstra requires non-negative edge weights.", "Graph", "Medium");
        addQ("What is the average time complexity of searching in a balanced BST?", "O(1)", "O(n)", "O(log n)", "O(n log n)", "C", "Balanced BST height is log n.", "Trees", "Easy");
    }

    private static void addQ(String q, String a, String b, String c, String d, String correct, String exp, String topic, String diff) {
        Question quest = new Question();
        quest.setId(memoryQuestions.size() + 1);
        quest.setQuestion(q);
        quest.setOptionA(a);
        quest.setOptionB(b);
        quest.setOptionC(c);
        quest.setOptionD(d);
        quest.setCorrectOption(correct);
        quest.setExplanation(exp);
        quest.setTopic(topic);
        quest.setDifficulty(diff);
        memoryQuestions.add(quest);
    }

    public List<Question> getQuestionsByTopic(String topic) {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM quiz_questions WHERE LOWER(topic) = LOWER(?) OR ? = 'All'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, topic);
            pstmt.setString(2, topic);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapQuestion(rs));
                }
            }
        } catch (Exception e) {
            for (Question q : memoryQuestions) {
                if ("All".equalsIgnoreCase(topic) || q.getTopic().equalsIgnoreCase(topic)) {
                    list.add(q);
                }
            }
        }
        return list.isEmpty() ? memoryQuestions : list;
    }

    public boolean saveQuizResult(QuizResult result) {
        String sql = "INSERT INTO quiz_results (user_id, topic, total_questions, correct_answers, wrong_answers, percentage, time_taken_seconds) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, result.getUserId());
            pstmt.setString(2, result.getTopic());
            pstmt.setInt(3, result.getTotalQuestions());
            pstmt.setInt(4, result.getCorrectAnswers());
            pstmt.setInt(5, result.getWrongAnswers());
            pstmt.setDouble(6, result.getPercentage());
            pstmt.setInt(7, result.getTimeTakenSeconds());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) result.setId(rs.getInt(1));
                }
                memoryResults.add(result);
                return true;
            }
        } catch (Exception e) {
            result.setId(memoryResults.size() + 1);
            memoryResults.add(result);
            return true;
        }
        return false;
    }

    public List<QuizResult> getResultsByUser(int userId) {
        List<QuizResult> list = new ArrayList<>();
        String sql = "SELECT * FROM quiz_results WHERE user_id = ? ORDER BY taken_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    QuizResult r = new QuizResult();
                    r.setId(rs.getInt("id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setTopic(rs.getString("topic"));
                    r.setTotalQuestions(rs.getInt("total_questions"));
                    r.setCorrectAnswers(rs.getInt("correct_answers"));
                    r.setWrongAnswers(rs.getInt("wrong_answers"));
                    r.setPercentage(rs.getDouble("percentage"));
                    r.setTimeTakenSeconds(rs.getInt("time_taken_seconds"));
                    r.setTakenAt(rs.getTimestamp("taken_at"));
                    list.add(r);
                }
            }
        } catch (Exception e) {
            for (QuizResult r : memoryResults) {
                if (r.getUserId() == userId) list.add(r);
            }
        }
        return list;
    }

    private Question mapQuestion(ResultSet rs) throws SQLException {
        Question q = new Question();
        q.setId(rs.getInt("id"));
        q.setQuestion(rs.getString("question"));
        q.setOptionA(rs.getString("option_a"));
        q.setOptionB(rs.getString("option_b"));
        q.setOptionC(rs.getString("option_c"));
        q.setOptionD(rs.getString("option_d"));
        q.setCorrectOption(rs.getString("correct_option"));
        q.setExplanation(rs.getString("explanation"));
        q.setTopic(rs.getString("topic"));
        q.setDifficulty(rs.getString("difficulty"));
        return q;
    }
}
