package com.example.matchit.model;

/**
 * Used for the my events adapter.
 */

public class eventItemModel {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String date;
    String time;
    String status;

    public eventItemModel(String name, String date, String time, String status){
        this.name = name;
        this.date = date;
        this.time = time;
        this.status = status;
    }


}
