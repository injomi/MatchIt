package com.example.matchit.model;

/**
 * Created by Joel on 02-Jul-17.
 */

public class OrgInfo {
    int orgID;
    String orgName;
    String orgAddress;
    int orgContact;
    String who;
    String howthey;
    String howyou;

    public OrgInfo(){
        orgName = "";
        orgID = 0;
        orgAddress = "";
        orgContact = 0;
        who = "";
        howthey = "";
        howyou = "";
    }
    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int getOrgID() {
        return orgID;
    }

    public void setOrgID(int orgID) {
        this.orgID = orgID;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public int getOrgContact() {
        return orgContact;
    }

    public void setOrgContact(int orgContact) {
        this.orgContact = orgContact;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getHowthey() {
        return howthey;
    }

    public void setHowthey(String howthey) {
        this.howthey = howthey;
    }

    public String getHowyou() {
        return howyou;
    }

    public void setHowyou(String howyou) {
        this.howyou = howyou;
    }

}
