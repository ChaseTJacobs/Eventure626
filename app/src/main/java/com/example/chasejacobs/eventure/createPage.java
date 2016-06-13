package com.example.chasejacobs.eventure;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.Random;

public class createPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    private String categories[] = new String[9];
    private ArrayAdapter<String> adapter;
    private String categorySelected = new String();
    private int hourChecker;
    private int minuteChecker;

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

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;
        categorySelected = myText.getText().toString();
        if(!categorySelected.equals("Select Category")) {
            Toast.makeText(this, "You selected " + myText.getText(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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
                if(minuteChecker <= 0
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
                    try {
                        events newEvent = new events();
                        newEvent.setPeopleLimit(Integer.parseInt(peopleLimit.getText().toString()));
                        Random rand = new Random();
                        newEvent.setEventID(rand.nextInt(2000) + 1);
                        newEvent.setEventName(eventName.getText().toString());
                        newEvent.setLocation(location.getText().toString());
                        newEvent.setCreatorName(creatorsName.getText().toString());
                        newEvent.setDescription(description.getText().toString());
                        newEvent.setCategory(categorySelected);
                    } catch (NumberFormatException e) {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                        myAlert.setMessage("Please type in a number for people limit!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
                    myAlert.setMessage("Please enter time in the following format:\nhh:mm AM or PM").setPositiveButton("OK", new DialogInterface.OnClickListener() {
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
