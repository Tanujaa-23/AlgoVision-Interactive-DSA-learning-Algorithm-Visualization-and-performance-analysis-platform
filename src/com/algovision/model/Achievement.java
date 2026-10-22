package com.algovision.model;

import java.sql.Timestamp;

public class Achievement {
    private int id;
    private String badgeKey;
    private String title;
    private String description;
    private String iconClass;
    private boolean earned;
    private Timestamp earnedAt;

    public Achievement() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBadgeKey() { return badgeKey; }
    public void setBadgeKey(String badgeKey) { this.badgeKey = badgeKey; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIconClass() { return iconClass; }
    public void setIconClass(String iconClass) { this.iconClass = iconClass; }

    public boolean isEarned() { return earned; }
    public void setEarned(boolean earned) { this.earned = earned; }

    public Timestamp getEarnedAt() { return earnedAt; }
    public void setEarnedAt(Timestamp earnedAt) { this.earnedAt = earnedAt; }
}
