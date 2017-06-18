package com.example.matchit.model;

/**
 * Created by Ajimal on 6/11/2017.
 */

public class Session {

    String date;
    String time;
    int volunteerMax;
    int volunteerNo;
    int sessionID;

    public Session(){
        date = "";
        time = "";
        volunteerMax = 0;
        volunteerNo = 0;
        sessionID = 0;
    }

    public Session(String d, String t, int vm, int vn, int id){
        date = d;
        time = t;
        volunteerNo = vn;
        volunteerMax = vm;
        sessionID = id;
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

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }



}
