package com.example.matchit.model;

/**
 * Created by Ajimal on 6/11/2017.
 */

public class Session {

    String date;
    String time;
    int volunteerMax;
    int volunteerNo;

    public Session(){}

    public Session(String d, String t, int vm, int vn){
        date = d;
        time = t;
        volunteerNo = vn;
        volunteerMax = vm;
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

    public int getVolunteerMax() {
        return volunteerMax;
    }

    public void setVolunteerMax(int volunteerMax) {
        this.volunteerMax = volunteerMax;
    }

    public int getVolunteerNo() {
        return volunteerNo;
    }

    public void setVolunteerNo(int volunteerNo) {
        this.volunteerNo = volunteerNo;
    }

    public void incVolunteerNo(){
        volunteerNo++;
    }

    public void decVolunteerNo(){
        volunteerNo--;
    }



}
