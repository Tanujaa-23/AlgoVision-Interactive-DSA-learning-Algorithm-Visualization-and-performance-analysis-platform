package com.algovision.model;

import java.sql.Timestamp;

public class Algorithm {
    private int id;
    private String name;
    private String category; // Sorting, Searching, Graph, DataStructure
    private String description;
    private String howItWorks;
    private String complexityBest;
    private String complexityAvg;
    private String complexityWorst;
    private String spaceComplexity;
    private boolean isStable;
    private boolean isInPlace;
    private Timestamp createdAt;

    public Algorithm() {}

    public Algorithm(int id, String name, String category, String description, String complexityBest, String complexityAvg, String complexityWorst, String spaceComplexity, boolean isStable, boolean isInPlace) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.complexityBest = complexityBest;
        this.complexityAvg = complexityAvg;
        this.complexityWorst = complexityWorst;
        this.spaceComplexity = spaceComplexity;
        this.isStable = isStable;
        this.isInPlace = isInPlace;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHowItWorks() { return howItWorks; }
    public void setHowItWorks(String howItWorks) { this.howItWorks = howItWorks; }

    public String getComplexityBest() { return complexityBest; }
    public void setComplexityBest(String complexityBest) { this.complexityBest = complexityBest; }

    public String getComplexityAvg() { return complexityAvg; }
    public void setComplexityAvg(String complexityAvg) { this.complexityAvg = complexityAvg; }

    public String getComplexityWorst() { return complexityWorst; }
    public void setComplexityWorst(String complexityWorst) { this.complexityWorst = complexityWorst; }

    public String getSpaceComplexity() { return spaceComplexity; }
    public void setSpaceComplexity(String spaceComplexity) { this.spaceComplexity = spaceComplexity; }

    public boolean isStable() { return isStable; }
    public void setStable(boolean stable) { isStable = stable; }

    public boolean isInPlace() { return isInPlace; }
    public void setInPlace(boolean inPlace) { isInPlace = inPlace; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
