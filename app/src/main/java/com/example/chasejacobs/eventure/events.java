package com.example.chasejacobs.eventure;

import android.util.Log;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;

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
    private ArrayList<String> peopleGoing;
    private String category;
    private String description;
    private int eventID;
    public JSONObject jsonObject = new JSONObject();

    public void createJSON(){
        try{
            jsonObject.put("eventName", eventName);
            jsonObject.put("creatorName", creatorName);
            jsonObject.put("location", location);
            jsonObject.put("date", date);
            jsonObject.put("time", time);
            jsonObject.put("numPeopleGoing", numPeopleGoing);
            jsonObject.put("peopleLimit", peopleLimit);
            JSONObject jPeopleGoing = new JSONObject();
            int temp = peopleGoing.size();
            for (int i = 0; i < temp; i++){
                jPeopleGoing.put("person" + i, peopleGoing.get(i));
            }
            jsonObject.put("category", category);
            jsonObject.put("description", description);
            jsonObject.put("eventID", eventID);
            jsonObject.put("peopleGoing", jPeopleGoing);
        }
        catch (Exception o){
            System.out.println(o);
        }
    }
    /*
    public void saveToDatabase(){
        Gson gson = new Gson();
        YourPOJO myPOJO = gson.fromJson(jsonString, YourPOJO.class);
    }
    */
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
    public void setPeopleGoing(ArrayList<String> listOfPeople){
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

    public void addPersonGoing(String person){
        this.peopleGoing.add(person);
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
    public ArrayList<String> getPeopleGoing() {
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
}
