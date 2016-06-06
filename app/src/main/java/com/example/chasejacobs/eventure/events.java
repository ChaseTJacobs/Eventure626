package com.example.chasejacobs.eventure;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by chasejacobs on 6/6/16.
 */
public class events {
    private String eventName;
    private String creatorName;
    private String location;
    private Date dateObject;
    private int numPeopleGoing;
    private int peopleLimit;
    private ArrayList<String> peopleGoing;
    private String catagory;

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setDateObject(Date dateObject) {
        this.dateObject = dateObject;
    }
    public void setNumPeopleGoing(int numPeopleGoing) {
        this.numPeopleGoing = numPeopleGoing;
    }
    public void setPeopleLimit(int peopleLimit) {
        this.peopleLimit = peopleLimit;
    }
    public void setPeopleGoing(String person){
        peopleGoing.add(person);
    }
    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getEventName() {
        return eventName;
    }
    public String getCreatorName() {
        return creatorName;
    }
    public String getLocation() {
        return location;
    }
    public Date getDateObject() {
        return dateObject;
    }
    public int getNumPeopleGoing() {
        return numPeopleGoing;
    }
    public int getPeopleLimit() {
        return peopleLimit;
    }
    public ArrayList<String> getPeopleGoing() {
        return peopleGoing;
    }
    public String getCatagory() {
        return catagory;
    }
}
