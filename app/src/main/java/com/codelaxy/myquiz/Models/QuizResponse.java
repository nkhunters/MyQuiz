package com.codelaxy.myquiz.Models;

import java.util.ArrayList;

public class QuizResponse {

    private Boolean error;
    private ArrayList<Quiz> quizes;

    public QuizResponse(Boolean error, ArrayList<Quiz> quizes) {
        this.error = error;
        this.quizes = quizes;
    }

    public Boolean getError() {
        return error;
    }

    public ArrayList<Quiz> getQuizes() {
        return quizes;
    }
}
