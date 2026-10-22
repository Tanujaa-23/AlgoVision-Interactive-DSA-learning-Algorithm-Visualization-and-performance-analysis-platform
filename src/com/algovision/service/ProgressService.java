package com.algovision.service;

import com.algovision.dao.ProgressDAO;
import com.algovision.model.Achievement;
import com.algovision.model.UserProgress;

import java.util.List;

public class ProgressService {

    private final ProgressDAO progressDAO = new ProgressDAO();

    public List<UserProgress> getUserProgress(int userId) {
        return progressDAO.getUserProgressList(userId);
    }

    public List<Achievement> getUserAchievements(int userId) {
        return progressDAO.getUserAchievements(userId);
    }

    public void updateProgress(int userId, String topic) {
        progressDAO.incrementTopicProgress(userId, topic);
    }

    public void recordAlgorithmProgress(int userId, String topic, String algoName) {
        progressDAO.recordAlgorithmProgress(userId, topic, algoName);
    }
}
