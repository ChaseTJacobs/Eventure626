package com.example.chasejacobs.eventure;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.sql.Date;
import java.util.Random;

public class createPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_page);
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
                    &&!peopleLimit.getText().toString().equals("")) {
                try {
                    events newEvent = new events();
                    newEvent.setPeopleLimit(Integer.parseInt(peopleLimit.getText().toString()));
                    Random rand = new Random();
                    newEvent.setEventID(rand.nextInt(2000) + 1);
                    newEvent.setEventName(eventName.getText().toString());
                    newEvent.setLocation(location.getText().toString());
                    newEvent.setCreatorName(creatorsName.getText().toString());
                    newEvent.setDescription(description.getText().toString());
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
