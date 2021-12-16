package com.mathquiz.mathquiz.model;

public class Player {

    String name;
    Integer score;
    String decs;

    public Player() {
    }

    public Player(String name, Integer score, String decs) {
        this.name = name;
        this.score = score;
        this.decs = decs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getDecs() {
        return decs;
    }

    public void setDecs(String decs) {
        this.decs = decs;
    }
}
