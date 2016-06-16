package com.example.chasejacobs.eventure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EventInfo extends AppCompatActivity {
    events test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        test = new events();
    }

    public void displayInfoTest(){
        test.setPeopleLimit(10);
        test.setCreatorName("Luke");
        test.setLocation("I-Center");
        test.setCategory("Sports");
        test.setDescription("Ball is life");
        test.setNumPeopleGoing(10);
        test.setEventName("BALL");
        test.setDate("06/20/2016");
        test.setTime("08:00 PM");

    }
}
