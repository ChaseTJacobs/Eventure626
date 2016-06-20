package com.example.chasejacobs.eventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    private String categories[] = new String[9];
    private ArrayAdapter<String> adapter;
    private ListView mainList;
    private String categorySelected = new String();

    String[] eventName;
    String[] eventCategory;
    ArrayAdapter<String> eventAdapter;

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

        mainList = (ListView)findViewById(R.id.listResults);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

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
        mainList = (ListView) findViewById(R.id.listResults);
        final events NewEvent1 = new events();
        events NewEvent2 = new events();
        events NewEvent3 = new events();
        NewEvent1.setEventName("FFA Soccer");
        NewEvent1.setCategory("Sports");
        NewEvent1.setPeopleLimit(4);
        ArrayList<String> test = new ArrayList<String>();
        test.add("Bob");
        test.add("John");
        test.add("someGirl");
        NewEvent1.setPeopleGoing(test);
        NewEvent1.setDescription("A game of soccer free for all, where everyone tries to get their own goals");
        NewEvent1.setCreatorName("Chase Jacobs");
        NewEvent1.setLocation("Some field");
        NewEvent1.setNumPeopleGoing(4);
        NewEvent1.setEventID(1511329);

        NewEvent2.setEventName("Board game night");
        NewEvent2.setCategory("Board Games");
        NewEvent3.setEventName("Volunteer Food Drive");
        NewEvent3.setCategory("Charity");
        List<events> stuff = new ArrayList<events>();
        stuff.add(NewEvent1);
        stuff.add(NewEvent2);
        stuff.add(NewEvent3);

        int result = 3;
        eventName = new String[result];
        eventCategory = new String[result];

        for (int i = 0; i < result; i++){
            eventName[i] = stuff.get(i).getEventName();
            eventCategory[i] = stuff.get(i).getCategory();
            System.out.println(eventName[i]);
        }
        eventAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,eventName);
        mainList.setAdapter(eventAdapter);
        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
