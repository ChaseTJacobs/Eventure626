package com.example.chasejacobs.eventure;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Activity for displaying a certain event
 *
 * This activity is used to the corresponding event that the user clicked on
 *
 * @author Chase Jacobs, Luke Iannucci
 * @version 2016.0.0.1
 * @since 1.0
 */
public class EventInfo extends AppCompatActivity {
    events test;
    Firebase mRef;
    private ListView lv;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        test = new events();
        Bundle bundle = getIntent().getExtras();
        test.setDescription(bundle.getString("description"));
        test.setEventName(bundle.getString("eventName"));
        test.setCategory(bundle.getString("category"));
        test.setPeopleLimit(Integer.parseInt(bundle.getString("peopleLimit")));
        test.setEventID(Integer.parseInt(bundle.getString("eventID")));
        test.setCreatorName(bundle.getString("creatorName"));
        test.setLocation(bundle.getString("location"));
        test.setKey(bundle.getString("key"));
        test.setTime(bundle.getString("time"));
        test.setDate(bundle.getString("date"));
        lv = (ListView) findViewById(R.id.eventListView);
        String[] eventInfo = new String[8];
        eventInfo[0] = "Event Name: "+ test.getEventName();
        eventInfo[1] = "Creator Name: "+ test.getCreatorName();
        eventInfo[2] = "Location: " + test.getLocation();
        eventInfo[3] = "Date: " + test.getDate();
        eventInfo[4] = "Time: " + test.getTime();
        eventInfo[5] = "Description: " + test.getDescription();
        eventInfo[6] = "People Limit: " + test.getPeopleLimit();
        eventInfo[7] = "Category: " + test.getCategory();
        adapter = new ArrayAdapter<String>(EventInfo.this, android.R.layout.simple_list_item_1, eventInfo);
        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);
    }

    public void onBackButton(View a){
        if(a.getId() == R.id.backButton){
            Intent i = new Intent(this, SearchPage.class);
            startActivity(i);
        }
    }

    public void joinEvent(View a){

        final EditText name = (EditText) findViewById(R.id.userName);
        final EditText peopleGoing = (EditText) findViewById(R.id.peopleGoing);
        if(name.getText().toString().equals("")
                && peopleGoing.getText().toString().equals("")){
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("Please fill out all text fields!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
            myAlert.show();
        }
        else if(!name.getText().toString().equals("")
                && peopleGoing.getText().toString().equals("")){
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("Please fill out the number of people going!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
            myAlert.show();
        }
        else if(name.getText().toString().equals("")
                && !peopleGoing.getText().toString().equals("")){
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("Please fill out your name!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
            myAlert.show();
        }
        else {
            mRef = new Firebase(test.getKey());
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    events joinEvent;
                    joinEvent = dataSnapshot.getValue(events.class);
                    joinEvent.addPersonGoing(name.getText().toString());
                    Log.i("stuff", dataSnapshot.getValue().toString());
                    mRef.setValue(joinEvent);
                    String message = "";
                    String EventString = "Event: " + test.getEventName() + "\nDate: " + test.getDate() + "\nTime: " + test.getTime() +  "\nLocation: " + test.getLocation();
                    try {
                        FileInputStream fileInput = openFileInput("yourGames");
                        InputStreamReader readString = new InputStreamReader(fileInput);
                        BufferedReader bReader = new BufferedReader(readString);
                        StringBuffer sBuffer = new StringBuffer();
                        while ((message = bReader.readLine()) != null) {
                            sBuffer.append(message + "\n");
                        }
                        message = sBuffer.toString();
                    } catch (FileNotFoundException o) {
                        o.printStackTrace();
                    } catch (IOException c) {
                        c.printStackTrace();
                    }
                    message = message + EventString + "\n";
                    try {
                        FileOutputStream outputStream = openFileOutput("yourGames", MODE_PRIVATE);
                        outputStream.write(message.getBytes());
                        outputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException o) {
                        o.printStackTrace();
                    }
                    Intent i = new Intent(EventInfo.this, MainActivity.class);
                    startActivity(i);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

    }

}
