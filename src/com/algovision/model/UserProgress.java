package com.algovision.model;

import java.sql.Timestamp;

public class UserProgress {
    private int id;
    private int userId;
    private String topic;
    private int completedCount;
    private int totalCount;
    private Timestamp lastUpdated;

    public UserProgress() {}

    public UserProgress(int userId, String topic, int completedCount, int totalCount) {
        this.userId = userId;
        this.topic = topic;
        this.completedCount = completedCount;
        this.totalCount = totalCount;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public int getCompletedCount() { return completedCount; }
    public void setCompletedCount(int completedCount) { this.completedCount = completedCount; }

    public int getTotalCount() { return totalCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }

    public Timestamp getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(Timestamp lastUpdated) { this.lastUpdated = lastUpdated; }

    public int getPercentage() {
        if (totalCount <= 0) return 0;
        return (int) Math.min(100, Math.round(((double) completedCount / totalCount) * 100));
    }
}
