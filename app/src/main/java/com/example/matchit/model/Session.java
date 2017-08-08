package com.example.matchit.model;

/**
 * used for firebase JSON conversion
 */

public class Session {

    String date;
    int volunteerMax;
    int volunteerNo;
    int sessionID;
    String startTime;
    String endTime;

    public Session(){
        date = "";
        volunteerMax = 0;
        volunteerNo = 0;
        sessionID = 0;
        startTime = "";
        endTime = "";
    }

    public Session(String d, int vm, int vn, int id){
        date = d;
        volunteerNo = vn;
        volunteerMax = vm;
        sessionID = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
