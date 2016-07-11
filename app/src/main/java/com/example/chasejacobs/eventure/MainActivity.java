package com.example.chasejacobs.eventure;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            createGPSErrorDialog();
        }
        if (networkInfo == null) {
            createNetErrorDialog();
        }
        getLocation();

    }

    protected void createGPSErrorDialog() {
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

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
        }
        TextView tv = (TextView) findViewById(R.id.gps);
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        MyLocListener loc = new MyLocListener();
        Location myLoc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loc);
        tv.setText("Latitude: " + myLoc.getLatitude() + "\nLongitude: " + myLoc.getLongitude());
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
            } else if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                createGPSErrorDialog();
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
            } else if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                createGPSErrorDialog();
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
            } else if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                createGPSErrorDialog();
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
            }
            else if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                createGPSErrorDialog();
            }
            else {
                mRef = new Firebase("https://eventure-8fca3.firebaseio.com/testing");
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("testing");
                newText = (TextView) findViewById(R.id.newTest);
                String text = newText.getText().toString();
                myRef.setValue(text);
            }

        }
    }
}