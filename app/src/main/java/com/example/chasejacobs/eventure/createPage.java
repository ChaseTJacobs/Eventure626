package com.example.chasejacobs.eventure;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.util.Random;

/**
 * Activity for creating a new Event.
 *
 * This activity is used to create a new Event, and adding it to the database. It will ask the user
 *  for all of the information needed in order to create a new event.
 *
 * @author Chase Jacobs, Luke Iannucci
 * @version 2016.1.0.0
 * @since 1.0
 */
public class createPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    private String categories[] = new String[9];
    private ArrayAdapter<String> adapter;
    private String categorySelected = new String();
    private int hourChecker;
    private int minuteChecker;
    private int dayChecker;
    private int monthChecker;
    private int yearChecker;
    private static final String errorTag = "createPage";
    Firebase mRef;
    private String filename = "yourGames";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_page);
        spinner = (Spinner) findViewById(R.id.spinner);
        categories[0] = "Select Category";
        categories[1]= "Sports";
        categories[2] = "Outdoors";
        categories[3] = "Board Games";
        categories[4] = "Parties";
        categories[5] = "Food";
        categories[6] = "Business";
        categories[7] = "Charity";
        categories[8] = "Other";

        mRef = new Firebase("https://eventure-8fca3.firebaseio.com/event1");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    public void onButtonClickHome(View a){
        if (a.getId() == R.id.homeButton){
            Intent i = new Intent (this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;
        categorySelected = myText.getText().toString();
        if(!categorySelected.equals("Select Category")) {
            Toast.makeText(this, "You selected " + myText.getText(), Toast.LENGTH_SHORT).show();
            Log.i(errorTag, myText.getText().toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This function will create a new event, and validate the users input.
     *
     * @param a the view
     */
    public void createEvent(View a){
        if (a.getId() == R.id.createButton){
            EditText eventName = (EditText)findViewById(R.id.eventNameInput);
            EditText location = (EditText)findViewById(R.id.locationInput);
            EditText time = (EditText)findViewById(R.id.timeInput);
            EditText creatorsName = (EditText)findViewById(R.id.creatorNameInput);
            EditText description = (EditText)findViewById(R.id.descriptionInput);
            EditText date = (EditText)findViewById(R.id.dateInput);
            EditText peopleLimit = (EditText)findViewById(R.id.peopleLimitInput);

            if(!eventName.getText().toString().equals("")
                    &&!location.getText().toString().equals("")
                    &&!time.getText().toString().equals("")
                    &&!creatorsName.getText().toString().equals("")
                    &&!description.getText().toString().equals("")
                    &&!date.getText().toString().equals("")
                    &&!peopleLimit.getText().toString().equals("")
                    &&!categorySelected.equals("Select Category")) {
                String checkTime = time.getText().toString();
                String checkDate = date.getText().toString();
                if(checkDate.length() == 10 && checkTime.length() == 8) {
                    try {
                        dayChecker = Integer.parseInt(checkDate.substring(3, 5));
                        monthChecker = Integer.parseInt(checkDate.substring(0, 2));
                        yearChecker = Integer.parseInt(checkDate.substring(6, 10));
                        Log.i(errorTag, checkDate);
                    } catch (NumberFormatException e) {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                        myAlert.setMessage("Please type enter a valid date!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        myAlert.show();
                    }
                    try {
                        hourChecker = Integer.parseInt(checkTime.substring(0, 2));
                        minuteChecker = Integer.parseInt(checkTime.substring(3, 5));
                    }
                    catch (NumberFormatException e) {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                        myAlert.setMessage("Please type enter a valid time!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        myAlert.show();
                    }
                    if (dayChecker <= 0
                            || dayChecker > 31
                            || monthChecker <= 0
                            || monthChecker > 12
                            || yearChecker < 2016
                            || yearChecker > 2030){
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                        myAlert.setMessage("Please enter a valid date!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        myAlert.show();
                    }
                    else if(minuteChecker < 0
                            || minuteChecker > 59
                            || hourChecker <= 0
                            || hourChecker > 12) {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                        myAlert.setMessage("Please enter a valid time!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        myAlert.show();
                    }
                    else if(checkTime.length() == 8
                            && checkTime.charAt(2) == ':'
                            && checkTime.charAt(5) == ' '
                            && (checkTime.substring(6, 8).toUpperCase().equals("PM")
                            ||checkTime.substring(6, 8).toUpperCase().equals("AM"))) {
                        if (checkDate.length() == 10
                                && checkDate.charAt(2) == '/'
                                && checkDate.charAt(5) == '/'
                                ) {
                            try {
                                events newEvent = new events();
                                newEvent.setPeopleLimit(Integer.parseInt(peopleLimit.getText().toString()));
                                Random rand = new Random();
                                newEvent.setEventID(rand.nextInt(2000) + 1);
                                newEvent.setEventName(eventName.getText().toString());
                                newEvent.setLocation(location.getText().toString());
                                newEvent.setCreatorName(creatorsName.getText().toString());
                                newEvent.setDescription(description.getText().toString());
                                newEvent.setTime(time.getText().toString());
                                newEvent.setCreatorName(creatorsName.getText().toString());
                                newEvent.setDate(date.getText().toString());
                                newEvent.setPeopleLimit(Integer.parseInt(peopleLimit.getText().toString()));


                                //// TODO: 7/14/16  add longitude and latitude to newEvent :D



                                newEvent.setCategory(categorySelected);
                                Log.i("HAPPENS","This happens, so it probably saves");
                                mRef = new Firebase("https://eventure-8fca3.firebaseio.com/" + newEvent.getCategory() + Integer.toString(newEvent.getEventID()));
                                newEvent.setKey("https://eventure-8fca3.firebaseio.com/" + newEvent.getCategory() + Integer.toString(newEvent.getEventID()));
                                String tempS = "https://eventure-8fca3.firebaseio.com/" + newEvent.getCategory() + Integer.toString(newEvent.getEventID()) + "\n";
                                mRef.setValue(newEvent);
                                mRef.child("peopleGoing").setValue(newEvent.getPeopleGoing());
                                //read old file
                                String message = "";

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
                                message = message + tempS + "\n";
                                //write new file
                                Log.i("Yes", "Load files");
                                try {
                                    FileOutputStream outputStream = openFileOutput(filename,MODE_PRIVATE);
                                    outputStream.write(message.getBytes());
                                    outputStream.close();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch(IOException o){
                                    o.printStackTrace();
                                }
                            } catch (NumberFormatException e) {
                                AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                                myAlert.setMessage("Please type in a number for people limit!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create();
                                myAlert.show();
                                Log.e(errorTag, e.getMessage());
                            }
                        }
                        else {
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                            myAlert.setMessage("Please enter date in the following format:\ndd/mm/yyyy").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create();
                            myAlert.show();
                        }
                    }
                    else {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                        myAlert.setMessage("Please enter the time in the following format:\nhh:mm AM or PM").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        myAlert.show();
                    }
                }
                else if(checkDate.length() != 10
                        && checkTime.length() == 8){
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                    myAlert.setMessage("Please enter the date in the following format:\nmm/dd/yyyy").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    myAlert.show();
                }
                else if(checkDate.length() == 10
                        && checkTime.length() != 8){
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                    myAlert.setMessage("Please enter the time in the following format:\nhh:mm AM or PM").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    myAlert.show();
                }
                else {
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                    myAlert.setMessage("Please enter the time in the following format:\nhh:mm AM or PM\nAnd the date in the following format:\nmm/dd/yyyy").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    myAlert.show();
                }
            }

            else if (!eventName.getText().toString().equals("")
                    &&!location.getText().toString().equals("")
                    &&!time.getText().toString().equals("")
                    &&!creatorsName.getText().toString().equals("")
                    &&!description.getText().toString().equals("")
                    &&!date.getText().toString().equals("")
                    &&!peopleLimit.getText().toString().equals("")
                    &&categorySelected.equals("Select Category")) {
                AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                myAlert.setMessage("Please select a category!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                myAlert.show();
            }
            else {
                AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                myAlert.setMessage("Please fill out all text fields!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
                myAlert.show();
            }


            // TODO: 6/9/16 save event to server

        }
    }
}
