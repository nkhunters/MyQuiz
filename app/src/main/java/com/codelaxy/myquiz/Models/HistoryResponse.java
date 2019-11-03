package com.codelaxy.myquiz.Models;

import java.util.ArrayList;

public class HistoryResponse {

    private Boolean error;
    private ArrayList<History> reward_history;

    public HistoryResponse(Boolean error, ArrayList<History> reward_history) {
        this.error = error;
        this.reward_history = reward_history;
    }

    public Boolean getError() {
        return error;
    }

    public ArrayList<History> getReward_history() {
        return reward_history;
    }
}
