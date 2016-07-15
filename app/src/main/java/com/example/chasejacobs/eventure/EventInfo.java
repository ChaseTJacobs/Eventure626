package com.example.chasejacobs.eventure;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
        
        //// TODO: 7/14/16 There needs to be some way to display all this information (not the key though) on the EventInfo page 
    }

    public void joinEvent(View a){
        mRef = new Firebase(test.getKey());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                events joinEvent;
                joinEvent = dataSnapshot.getValue(events.class);
                Log.i("stuff", dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
