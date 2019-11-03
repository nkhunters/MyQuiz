package com.codelaxy.myquiz.Models;

import java.util.ArrayList;

public class QuestionResponse {

    private Boolean error;
    private ArrayList<Question> questions;

    public QuestionResponse(Boolean error, ArrayList<Question> questions) {
        this.error = error;
        this.questions = questions;
    }

    public Boolean getError() {
        return error;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

}
