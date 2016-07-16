package com.example.chasejacobs.eventure;

import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all of the info in an Event.
 *
 * It can also convert the individual data pieces to a JSON object
 * Created by chasejacobs on 6/6/16.
 */
public class events {
    private String eventName;
    private String creatorName;
    private String location;
    private String date;
    private String time;
    private int numPeopleGoing;
    private int peopleLimit;
    private List<String> peopleGoing = new ArrayList<>();
    private String category;
    private String description;
    private int eventID;
    private String latitude;
    private String longitute;
    private String key;

    public events(){}

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time) {this.time = time;}
    public void setNumPeopleGoing(int numPeopleGoing) {
        this.numPeopleGoing = numPeopleGoing;
    }
    public void setPeopleLimit(int peopleLimit) {
        this.peopleLimit = peopleLimit;
    }
    public void setPeopleGoing(List<String> listOfPeople){
        this.peopleGoing = listOfPeople;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitute() {
        return longitute;
    }
    public String getKey() {
        return key;
    }

    public void addPersonGoing(String person){
        this.peopleGoing.add(person);
        this.numPeopleGoing = this.numPeopleGoing + 1;
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
    public String getDate() {
        return date;
    }
    public String getTime() {return time; }
    public int getNumPeopleGoing() {
        return numPeopleGoing;
    }
    public int getPeopleLimit() {
        return peopleLimit;
    }
    public List<String> getPeopleGoing() {
        return peopleGoing;
    }
    public String getCategory() {
        return category;
    }
    public String getDescription() {
        return description;
    }
    public int getEventID() {
        return eventID;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }
    public void setKey(String key) {
        this.key = key;
    }
}
