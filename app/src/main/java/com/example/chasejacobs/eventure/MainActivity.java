package com.example.chasejacobs.eventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ArrayList<events> yourEvents;
    Firebase mRef;
    TextView newText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yourEvents = new ArrayList<events>();
    }

    protected void onStart(){
        super.onStart();
        newText = (TextView)findViewById(R.id.newTest);
        mRef = new Firebase("https://eventure-8fca3.firebaseio.com/testing");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                newText.setText(text);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    public void onButtonClick(View a){
        if (a.getId() == R.id.testNext){
            Intent i = new Intent (this, createPage.class);
            startActivity(i);
        }
    }

    public void onButtonClickTest(View a){
        if(a.getId() == R.id.eventInfo){
            Intent i = new Intent (this, EventInfo.class);
            startActivity(i);
        }
    }

    public void searchPage(View a){
        if (a.getId() == R.id.searchEvents){
            Intent i = new Intent(this, SearchPage.class);
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

    public void unitTestDatabase(View a){
        if (a.getId() == R.id.unitTestDatabase){
            mRef = new Firebase("https://eventure-8fca3.firebaseio.com/testing");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("testing");

            newText = (TextView)findViewById(R.id.newTest);
            String text = newText.getText().toString();

            myRef.setValue(text);
        }
    }
}