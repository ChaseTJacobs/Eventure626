package com.example.chasejacobs.eventure;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<events> yourEvents;
    Firebase mRef;
    TextView newText;
    private static final String errorMsg = "MainActivity";
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        yourEvents = new ArrayList<events>();
        if (networkInfo == null) {
            createNetErrorDialog();
        }
    }

    protected void createNetErrorDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You need a network connection to use this application. Please turn on mobile network or Wi-Fi in Settings.")
                .setTitle("Unable to connect")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                startActivity(i);
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }

    protected void onStart() {
        super.onStart();
        newText = (TextView) findViewById(R.id.newTest);
        mRef = new Firebase("https://eventure-860cb.firebaseio.com/testing");
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


    public void onButtonClick(View a) {
        if (a.getId() == R.id.testNext) {
            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null) {
                createNetErrorDialog();
            } else {
                Intent i = new Intent(this, createPage.class);
                startActivity(i);
            }
        }
    }

    public void onButtonClickTest(View a) {
        if (a.getId() == R.id.eventInfo) {
            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null) {
                createNetErrorDialog();
            } else {
                try {
                    Intent i = new Intent(this, EventInfo.class);
                    startActivity(i);
                } catch (Exception o) {
                    Log.e(errorMsg, o.toString());
                }
            }
        }
    }

    public void searchPage(View a) {
        if (a.getId() == R.id.searchEvents) {
            if (networkInfo == null) {
                connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = connMgr.getActiveNetworkInfo();
                createNetErrorDialog();
            } else {
                Intent i = new Intent(this, SearchPage.class);
                startActivity(i);
            }
        }
    }

    public void unitTestReceiveGames(View a) {
        String eventName = "Soccer";
        String creatorName = "Chase Jacobs";
        String location = "BYUI Lower fields";
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

    public void unitTestAddPerson(events event) {
        event.addPersonGoing("Joe");
        int numPeople = event.getNumPeopleGoing();
        numPeople++;
        event.setNumPeopleGoing(numPeople);
    }

    public void unitTestDatabase(View a) {
        if (a.getId() == R.id.unitTestDatabase) {
            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null) {
                createNetErrorDialog();
            } else {
                mRef = new Firebase("https://eventure-860cb.firebaseio.com//testing");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("testing");
                newText = (TextView) findViewById(R.id.newTest);
                String text = newText.getText().toString();
                myRef.setValue(text);
            }

        }
    }
}