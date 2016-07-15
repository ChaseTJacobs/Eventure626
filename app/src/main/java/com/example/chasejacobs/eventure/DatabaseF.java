package com.example.chasejacobs.eventure;

import com.firebase.client.Firebase;

/**
 * This class implements the Firebase database and links it into the rest of the app.
 *
 * @Author Cgit ase Jacobs
 */
public class DatabaseF extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
