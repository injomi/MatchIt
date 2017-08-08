package com.example.matchit.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used for firebase JSON conversion
 */

public class Event {
    int eventID;
    String category;
    String endDate;
    String startDate;
    String hostName;
    String location;
    String name;
    HashMap<String,Session> sessions;
    String description;

    public Event(){
        name = "";
        sessions = new HashMap<>();
        eventID = 0;
        category = "";
        endDate = "";
        startDate = "";
        hostName = "";
        location = "";
        description = "";
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String,Session> getSessions() {
        return sessions;
    }

    public void setSessions(HashMap<String,Session> sessions) {
        this.sessions = sessions;
    }

    public void addSession(Session session){
        this.sessions.put(String.valueOf(session.getSessionID()),session);
    }
    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
