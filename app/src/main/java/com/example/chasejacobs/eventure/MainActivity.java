package com.example.chasejacobs.eventure;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Activity for the main page
 *
 * This activity is used to display the main page and give you all the options on what to do
 *
 * @author Chase Jacobs, Luke Iannucci
 * @version 2016.0.0.1
 * @since 1.0
 */
public class MainActivity extends AppCompatActivity {
    ArrayList<events> yourEvents;
    Firebase mRef;
    TextView newText;
    TextView testText;
    private static final String errorMsg = "MainActivity";
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        yourEvents = new ArrayList<events>();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            createGPSErrorDialog();
        }

        if (networkInfo == null) {
            createNetErrorDialog();
        }
        loadFiles();

    }

    protected void loadFiles(){
        Log.i("Yes", "Load files");
        String message = "";
        testText = (TextView) findViewById(R.id.editTest);

        try{
            FileInputStream fileInput = openFileInput("yourGames");
            InputStreamReader readString = new InputStreamReader(fileInput);
            BufferedReader bReader = new BufferedReader(readString);
            StringBuffer sBuffer = new StringBuffer();
            while ((message=bReader.readLine()) != null){
                sBuffer.append(message + "\n");
            }
            message = sBuffer.toString();
        }
        catch (FileNotFoundException o){
            o.printStackTrace();
        }
        catch (IOException c){
            c.printStackTrace();
        }
        testText.setText(message);
    }

    protected void createGPSErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application needs to acces your location. Please enable your GPS location.")
                .setTitle("Unable to find your location")
                .setCancelable(false)
                .setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
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

    /**
     * This function will display the error message if you are not connected to the internet.
     * It will give the user the option to connect and if the choose not to, it will quit the app.
     *
     */
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

    /**
     * On the start of the app it will try and connect you to the database
     */
    protected void onStart() {
        super.onStart();
        newText = (TextView) findViewById(R.id.newTest);
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


    /**
     * This is the on button click that takes the user to the create an
     * event page. It checks if you are connected to the internet before
     * it takes you to that page.
     *
     * @param a
     */
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

    /**
     * This is the on button click that takes the user to the event info
     * page. It makes sure you are connected to the internet before taking you there.
     *
     * @param a
     */
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

    /**
     * This is the on button click function that will take you to the search page
     * this also check to make sure you are connected to the internet before taking you there.
     *
     * @param a
     */
    public void searchPage(View a) {
        if (a.getId() == R.id.searchEvents) {
            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null) {
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
        //event.setPeopleGoing(peopleGoing);
        event.setCategory(category);
        yourEvents.add(event);
    }
    /*
    public void unitTestAddPerson(events event) {
        event.addPersonGoing("Joe");
        int numPeople = event.getNumPeopleGoing();
        numPeople++;
        event.setNumPeopleGoing(numPeople);
    }
    */
    public void unitTestDatabase(View a) {
        if (a.getId() == R.id.unitTestDatabase) {
            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo == null) {
                createNetErrorDialog();
            } else {
                mRef = new Firebase("https://eventure-8fca3.firebaseio.com/testing");
                newText = (TextView) findViewById(R.id.newTest);
                String text = newText.getText().toString();
                mRef.setValue(text);
            }

        }
    }
}