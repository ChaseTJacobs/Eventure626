package com.example.chasejacobs.eventure;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Activity for displaying a certain event
 *
 * This activity is used to the corresponding event that the user clicked on
 *
 * @author Chase Jacobs, Luke Iannucci
 * @version 2016.0.0.1
 * @since 1.0
 */
public class EventInfo extends AppCompatActivity {
    events test;
    Firebase mRef;
    private ListView lv;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);
        test = new events();
        Bundle bundle = getIntent().getExtras();
        test.setDescription(bundle.getString("description"));
        test.setEventName(bundle.getString("eventName"));
        test.setCategory(bundle.getString("category"));
        test.setPeopleLimit(Integer.parseInt(bundle.getString("peopleLimit")));
        test.setEventID(Integer.parseInt(bundle.getString("eventID")));
        test.setCreatorName(bundle.getString("creatorName"));
        test.setLocation(bundle.getString("location"));
        test.setKey(bundle.getString("key"));
        lv = (ListView) findViewById(R.id.listView2);
        String[] eventInfo = new String[6];
        eventInfo[0] = "Event Name: "+ test.getEventName();
        eventInfo[1] = "Creator Name: "+ test.getCreatorName();
        eventInfo[2] = "Location: " + test.getLocation();
        eventInfo[3] = "Description: " + test.getDescription();
        eventInfo[4] = "People Limit: " + test.getPeopleLimit();
        eventInfo[5] = "Category: " + test.getCategory();
        adapter = new ArrayAdapter<String>(EventInfo.this, android.R.layout.simple_list_item_1, eventInfo);
        lv.setAdapter(adapter);
        lv.setTextFilterEnabled(true);
       // ArrayAdapter<String> adapter;
        

    }

    public void joinEvent(View a){
        mRef = new Firebase(test.getKey());
        final EditText name = (EditText) findViewById(R.id.userName);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events joinEvent;
                joinEvent = dataSnapshot.getValue(events.class);
                joinEvent.addPersonGoing(name.getText().toString());
                Log.i("stuff", dataSnapshot.getValue().toString());
                mRef.setValue(joinEvent);
                Intent i = new Intent(EventInfo.this, MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
