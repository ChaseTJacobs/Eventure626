package com.example.chasejacobs.eventure;

import com.firebase.client.Firebase;

/**
 * Created by chasejacobs on 6/13/16.
 */
public class DatabaseF extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
