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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

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
    MyLocListener loc;
    Location myLoc;
    private ListView lv;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        yourEvents = new ArrayList<events>();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lv = (ListView) findViewById(R.id.listView);
        loc = new MyLocListener();
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            createGPSErrorDialog();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 1);
                }
            }
        }
        if (networkInfo == null) {
            createNetErrorDialog();
        }
        loadFiles();
    }

    public void eventListStorage(List<events> eventList){
        final int listSize = eventList.size();
        String[] eventString = new String[listSize];
        for (int i = 0; i < listSize; i++){
            eventString[i] = "";
        }
        for (int i = 0; i < listSize; i++){
            eventString[i] = eventList.get(i).getEventName() + "\n" + eventList.get(i).getTime() + "\n" + eventList.get(i).getLocation();
        }

        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, eventString);
        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);
    }

    public void testListDisplay(View v){
        events event = new events();
        events event1 = new events();
        events event2 = new events();
        events event3 = new events();
        events event4 = new events();

        List<events> listOfEvents = new ArrayList<events>();

        event.setEventName("Basketball");
        event.setTime("08:00 PM");
        event.setLocation("I-Center");

        event1.setEventName("Pokemon");
        event1.setTime("09:00 PM");
        event1.setLocation("The Garden");

        event2.setEventName("Frisbee");
        event2.setTime("07:00 PM");
        event2.setLocation("Porter Park");

        event3.setEventName("Taco Party");
        event3.setTime("01:00 PM");
        event3.setLocation("Tacontento");

        event4.setEventName("Concert");
        event4.setTime("10:00 PM");
        event4.setLocation("Stadium");
        listOfEvents.add(event);
        listOfEvents.add(event1);
        listOfEvents.add(event2);
        listOfEvents.add(event3);
        listOfEvents.add(event4);
        listOfEvents.add(event);
        listOfEvents.add(event1);
        listOfEvents.add(event2);
        listOfEvents.add(event3);
        listOfEvents.add(event4);

        eventListStorage(listOfEvents);
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

    public void getLocation() {
        boolean requestedPermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
                requestedPermission = true;
            }
        }
        if(requestedPermission == false) {
            manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            myLoc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loc);
            if (myLoc != null) {
                TextView tv = (TextView) findViewById(R.id.gps);
                tv.setText("Latitude: " + myLoc.getLatitude() + "\nLongitude: " + myLoc.getLongitude());
            } else {
                Toast.makeText(this, "Finding Location", Toast.LENGTH_LONG).show();
            }
        }
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

    /*
    public void testGPS (View v){
        if (v.getId() == R.id.testGPS){
            getLocation();
        }
    }
    */
    /**
     * On the start of the app it will try and connect you to the database
     */
    protected void onStart() {
        super.onStart();
        //newText = (TextView) findViewById(R.id.newTest);
        mRef = new Firebase("https://eventure-8fca3.firebaseio.com/testing");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                //newText.setText(text);
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
            }
            else if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                createGPSErrorDialog();
            }
            else {
                mRef = new Firebase("https://eventure-8fca3.firebaseio.com/testing");
                newText = (TextView) findViewById(R.id.newTest);
                String text = newText.getText().toString();
                mRef.setValue(text);
            }

        }
    }
}