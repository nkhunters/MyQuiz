package com.codelaxy.myquiz.Models;

public class DefaultResponse {

    private Boolean error;
    private String message;

    public DefaultResponse(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
