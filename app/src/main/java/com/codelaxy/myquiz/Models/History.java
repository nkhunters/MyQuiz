package com.codelaxy.myquiz.Models;

public class History {

    private String id, amount, date_time, status;

    public History(String id, String amount, String date_time, String status) {
        this.id = id;
        this.amount = amount;
        this.date_time = date_time;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getDate_time() {
        return date_time;
    }

    public String getStatus() {
        return status;
    }
}
