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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for searching.
 *
 * This activity is used to search Events based on user input. It will contact the server and get
 * results that way.
 *
 * @author Chase Jacobs, Luke Iannucci
 * @version 2016.1.0.0
 * @since 1.0
 */
public class SearchPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    private String categories[] = new String[9];
    private ArrayAdapter<String> categorieAdapter;
    private String categorySelected = new String();
    Firebase mRef;
    private ArrayAdapter<String> adapter;

    String[] eventName;
    String[] eventCategory;
    ArrayAdapter<String> eventAdapter;
    private static final String errTag = "SearchPage";
    private ListView lv;
    private ArrayAdapter<String> eventListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        spinner = (Spinner) findViewById(R.id.spinnerSearch);
        categories[0] = "Select Category";
        categories[1]= "Sports";
        categories[2] = "Outdoors";
        categories[3] = "Board Games";
        categories[4] = "Parties";
        categories[5] = "Food";
        categories[6] = "Business";
        categories[7] = "Charity";
        categories[8] = "Other";

        categorieAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        categorieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categorieAdapter);
        spinner.setOnItemSelectedListener(this);
        lv = (ListView) findViewById(R.id.listResults);
    }

    /**
     *  This function will select a category from the drop down box, and notify the user which category they selected.
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView myText = (TextView) view;
        categorySelected = myText.getText().toString();
        if(!categorySelected.equals("Select Category")) {
            Toast.makeText(this, "You selected " + myText.getText(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onButtonClickHome(View a){
        if (a.getId() == R.id.homeButton){
            Intent i = new Intent (this, MainActivity.class);
            startActivity(i);
        }
    }

    public void unitTestLoadResults(View A){

        mRef = new Firebase("https://eventure-8fca3.firebaseio.com");

        if (categorySelected.equals("Select Category")){
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("Please select a category!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
            myAlert.show();
        }
        else{
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<events> searchResults = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Log.i("Category Selected", categorySelected);
                        Log.i("CategoryFromChild", child.getValue(events.class).getCategory());
                        if (child.getValue(events.class).getCategory().equals(categorySelected)){
                            searchResults.add(child.getValue(events.class));
                            Log.i("Search Results", child.getValue().toString());
                            Log.i("ThisWorks","ayyyyy");
                        }

                    }
                    final int listSize = searchResults.size();
                    String[] eventString = new String[listSize];
                    for (int i = 0; i < listSize; i++){
                        eventString[i] = "";
                    }
                    for (int i = 0; i < listSize; i++){
                        eventString[i] = searchResults.get(i).getEventName() + "\n" + searchResults.get(i).getTime() + "\n" + searchResults.get(i).getLocation();
                    }

                    adapter = new ArrayAdapter<String>(SearchPage.this, android.R.layout.simple_list_item_1, eventString);
                    lv.setAdapter(adapter);
                    lv.setTextFilterEnabled(true);

                    lv.setOnItemClickListener(new AdapterView.OnItemSelectedListener() {

                    }););
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }


        //mainList = (ListView) findViewById(R.id.listResults);

        eventAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,eventName);
        mainList.setAdapter(eventAdapter);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(errTag, NewEvent1.getEventName());
                Bundle bundle = new Bundle();
                bundle.putString("eventName", NewEvent1.getEventName());
                bundle.putString("category", NewEvent1.getCategory());
                bundle.putString("peopleLimit", String.valueOf(NewEvent1.getPeopleLimit()));
                bundle.putString("eventID", String.valueOf(NewEvent1.getEventID()));
                bundle.putString("creatorName", NewEvent1.getCreatorName());
                bundle.putString("location", NewEvent1.getLocation());
                bundle.putString("description", NewEvent1.getDescription());

                Intent i = new Intent(SearchPage.this, EventInfo.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });


    }

    public void sendEvent(events temp){

    }
}
