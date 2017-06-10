package com.example.matchit.model;

import java.util.ArrayList;

/**
 * Created by Ajimal on 6/11/2017.
 */

public class Event {
    String name;
    ArrayList<Session> sessions;
    public Event(){
        name = "";
        sessions = new ArrayList<>();
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Session> sessions) {
        this.sessions = sessions;
    }

    public void addSession(Session session){
        this.sessions.add(session);
    }
}
