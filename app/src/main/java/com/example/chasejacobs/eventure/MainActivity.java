package com.example.chasejacobs.eventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

//import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ArrayList<events> yourEvents;
    //private Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yourEvents = new ArrayList<events>();
        //Firebase.setAndroidContext(this);
    }


    public void onButtonClick(View a){
        if (a.getId() == R.id.testNext){
            Intent i = new Intent (this, createPage.class);
            startActivity(i);
        }
    }

    public void unitTestReceiveGames(View a){
        String eventName = "Soccer";
        String creatorName = "Chase Jacobs";
        String location = "BYUI Lower fields";
        //Date dateObject;
        int NumPeopleGoing = 4;
        int peopleLimit = 24;
        ArrayList<String> peopleGoing = new ArrayList<String>();
        peopleGoing.add("Bro Falin");
        peopleGoing.add("Luke");
        peopleGoing.add("Your mom");
        peopleGoing.add("Chuck");
        String category = "Sports";

        events event = new events();
        event.setEventName(eventName);
        event.setCreatorName(creatorName);
        event.setLocation(location);
        event.setNumPeopleGoing(NumPeopleGoing);
        event.setPeopleLimit(peopleLimit);
        event.setPeopleGoing(peopleGoing);
        event.setCategory(category);
        yourEvents.add(event);
    }

    public void unitTestAddPerson(events event){
        event.addPersonGoing("Joe");
        int numPeople = event.getNumPeopleGoing();
        numPeople++;
        event.setNumPeopleGoing(numPeople);
    }
}