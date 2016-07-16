package com.example.chasejacobs.eventure;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Random;

/**
 * Activity for creating a new Event.
 * <p/>
 * This activity is used to create a new Event, and adding it to the database. It will ask the user
 * for all of the information needed in order to create a new event.
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
    private static final String errorTag = "createPage";
    Firebase mRef;
    private String filename = "yourGames";
    ConnectivityManager connMgr;
    NetworkInfo networkInfo;
    LocationManager manager;
    MyLocListener loc;
    Location myLoc;
    Button button;
    Button timeButton;
    int year, month, day;
    static final int DIALOG_ID = 0;
    static final int TIME_DIALOG_ID = 1;
    int hour, minute;
    private String dateString;
    EditText date;
    EditText time;
    private String timeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_page);
        spinner = (Spinner) findViewById(R.id.spinner);
        categories[0] = "Select Category";
        categories[1] = "Sports";
        categories[2] = "Outdoors";
        categories[3] = "Board Games";
        categories[4] = "Parties";
        categories[5] = "Food";
        categories[6] = "Business";
        categories[7] = "Charity";
        categories[8] = "Other";
        connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        loc = new MyLocListener();
        mRef = new Firebase("https://eventure-8fca3.firebaseio.com/event1");
        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        dateString = "";
        timeString = "";
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            createGPSErrorDialog();
        } else {
            if (networkInfo != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.INTERNET}, 1);
                    }
                }
            }
        }
        if (networkInfo == null) {
            createNetErrorDialog();
        }
        showDialogOnButtonClick();
        showTimePickerDialog();
    }

    public void showTimeDialog(View v) {
        if (v.getId() == R.id.setTimeButton) {
            showTimePickerDialog();
        }
    }

    public void showTimePickerDialog() {
        timeButton = (Button) findViewById(R.id.setTimeButton);
        timeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog(TIME_DIALOG_ID);
                    }
                }
        );
    }

    protected TimePickerDialog.OnTimeSetListener kTimePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
                    hour = hourOfDay;
                    minute = minuteOfDay;
                    if (hour > 12) {
                        if ((hour - 12) < 10 && minute < 10) {
                            timeString = "0" + (hour - 12) + ":" + "0" + minute + " PM";
                        } else if ((hour - 12) < 10 && minute > 10) {
                            timeString = "0" + (hour - 12) + ":" + minute + " PM";
                        } else if (hour > 10 && minute > 10) {
                            timeString = hour + ":" + minute + " PM";
                        } else {
                            timeString = (hour - 12) + ":" + "0" + minute + " PM";
                        }
                    } else {
                        if (hour < 10 && minute < 10) {
                            timeString = "0" + hour + ":" + "0" + minute + " AM";
                        } else if (hour < 10 && minute > 10) {
                            timeString = "0" + hour + ":" + minute + " AM";
                        } else if (hour > 10 && minute > 10) {
                            timeString = hour + ":" + minute + " AM";
                        } else {
                            timeString = hour + ":" + "0" + minute + " AM";
                        }
                    }
                    time = (EditText) findViewById(R.id.timeInput);
                    time.setText(timeString);
                }
            };

    public void showDialog(View v) {
        showDialogOnButtonClick();
    }

    public void showDialogOnButtonClick() {
        button = (Button) findViewById(R.id.dateButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int yearX, int monthOfYear, int dayOfMonth) {
            year = yearX;
            month = monthOfYear + 1;
            day = dayOfMonth;
            dateString = month + "/" + day + "/" + year;
            date = (EditText) findViewById(R.id.dateInput);
            date.setText(dateString);
        }
    };

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_ID) {
            return new DatePickerDialog(this, dpickerListener, year, month, day);
        } else if (id == TIME_DIALOG_ID) {
            return new TimePickerDialog(this, kTimePickerListener, hour, minute, false);
        }
        return null;
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
                                createPage.this.finish();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void getLocation() {
        boolean requestedPermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
                requestedPermission = true;
            }
        }
        if (requestedPermission == false) {
            manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            myLoc = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loc);
        }
    }

    /**
     * This function will display the error message if you are not connected to the internet.
     * It will give the user the option to connect and if the choose not to, it will quit the app.
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
                                createPage.this.finish();
                            }
                        }
                );
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onButtonClickHome(View a) {
        if (a.getId() == R.id.homeButton) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;
        categorySelected = myText.getText().toString();
        if (!categorySelected.equals("Select Category")) {
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
    public void createEvent(View a) {
        if (a.getId() == R.id.createButton) {
            EditText eventName = (EditText) findViewById(R.id.eventNameInput);
            EditText location = (EditText) findViewById(R.id.locationInput);
            EditText creatorsName = (EditText) findViewById(R.id.creatorNameInput);
            EditText description = (EditText) findViewById(R.id.descriptionInput);
            EditText peopleLimit = (EditText) findViewById(R.id.peopleLimitInput);
            getLocation();
            connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            networkInfo = connMgr.getActiveNetworkInfo();

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                createGPSErrorDialog();
            } else if (networkInfo == null) {
                createNetErrorDialog();
            } else {
                if (myLoc != null) {
                    if (!eventName.getText().toString().equals("")
                            && !location.getText().toString().equals("")
                            && !time.getText().toString().equals("")
                            && !creatorsName.getText().toString().equals("")
                            && !description.getText().toString().equals("")
                            && !date.getText().toString().equals("")
                            && !peopleLimit.getText().toString().equals("")
                            && !categorySelected.equals("Select Category")) {
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
                            newEvent.setLatitude((long) myLoc.getLatitude());
                            newEvent.setLongitute((long) myLoc.getLongitude());
                            newEvent.setCategory(categorySelected);
                            Log.i("HAPPENS", "This happens, so it probably saves");
                            mRef = new Firebase("https://eventure-8fca3.firebaseio.com/events/" + newEvent.getCategory() + Integer.toString(newEvent.getEventID()));
                            newEvent.setKey("https://eventure-8fca3.firebaseio.com/events/" + newEvent.getCategory() + Integer.toString(newEvent.getEventID()));
                            String EventString = "Event: " + newEvent.getEventName() + "\nDate: " + newEvent.getDate() + "\nTime: " + newEvent.getTime() + "\nLocation: " + newEvent.getLocation();
                            mRef.setValue(newEvent);
                            mRef.child("peopleGoing").setValue(newEvent.getPeopleGoing());
                            //read old file
                            String message = "";
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
                            //write new file
                            Log.i("Yes", "Load files");
                            try {
                                FileOutputStream outputStream = openFileOutput(filename, MODE_PRIVATE);
                                outputStream.write(message.getBytes());
                                outputStream.close();
                                Intent i = new Intent(createPage.this, MainActivity.class);
                                startActivity(i);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException o) {
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
                    } else if (!eventName.getText().toString().equals("")
                            && !location.getText().toString().equals("")
                            && !time.getText().toString().equals("")
                            && !creatorsName.getText().toString().equals("")
                            && !description.getText().toString().equals("")
                            && !date.getText().toString().equals("")
                            && !peopleLimit.getText().toString().equals("")
                            && categorySelected.equals("Select Category")) {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                        myAlert.setMessage("Please select a category!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        myAlert.show();
                    } else {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                        myAlert.setMessage("Please fill out all text fields!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create();
                        myAlert.show();
                    }
                } else {
                    Toast.makeText(this, "Finding Location", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}