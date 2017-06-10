package com.example.matchit;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.matchit.model.Event;
import com.example.matchit.model.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by josephtyx on 4/6/17.
 */

public class    Events extends Fragment {

    View myView;
    TableRow eTable;
    TableLayout eLayout;
    DatabaseReference db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.events, container, false);
        db = FirebaseDatabase.getInstance().getReference();
//        Session s1 = new Session("11/7/2017","1 - 3 PM", 99, 0);
//        Session s2 = new Session("11/7/2017","3 - 5 PM", 100, 0);
//        Event e = new Event();
//        e.setName("Old folks");
//        e.addSession(s1);
//        e.addSession(s2);
//
//        String key = db.push().getKey();
//        db.child(key).setValue(e);
        //return super.onCreateView(inflater, container, savedInstanceState);
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Log.i("Test", "ID -> " + child.getKey());
                    Event event = child.getValue(Event.class);
                    Log.i("Test", "Name -> " + event.getName());
                    int count = 1;
                    for(Session session : event.getSessions()){
                        Log.i("Test", "Session: " + count);
                        Log.i("Test", "Date " + session.getDate());
                        Log.i("Test", "Time " + session.getTime());
                        Log.i("Test", "Max: " + session.getVolunteerMax());
                        Log.i("Test", "No.: " + session.getVolunteerNo());
                        count ++;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Test", "Error: 0xA2");
            }
        });
        return myView;
    }
}
