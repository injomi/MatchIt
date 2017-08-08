package com.example.matchit.app;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Map<String,String> data = remoteMessage.getData();
        for(String key : data.keySet()){
            Log.d(TAG, "Data: Key=" + key + ", value=" + data.get(key) );
        }
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}