package com.codelaxy.myquiz.Models;

public class Quiz {

    private String id, quiz_name, level, time, reward;

    public Quiz(String id, String quiz_name, String level, String time, String reward) {
        this.id = id;
        this.quiz_name = quiz_name;
        this.level = level;
        this.time = time;
        this.reward = reward;
    }

    public String getId() {
        return id;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public String getLevel() {
        return level;
    }

    public String getTime() {
        return time;
    }

    public String getReward() {
        return reward;
    }
}
