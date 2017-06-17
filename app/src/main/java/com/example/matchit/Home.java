package com.example.matchit;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.matchit.helper.RVAdapter;
import com.example.matchit.model.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by josephtyx on 4/6/17.
 */

public class Home extends Fragment {

    View myView;
    RecyclerView rv;
    ArrayList<Event> events;
    DatabaseReference db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home, container, false);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return myView;
    }
    public void sendMessage(View view)
    {
        Intent intent = new Intent(getActivity(), EventDetails.class);
        startActivity(intent);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        Log.i("Test","hit here");
        db = FirebaseDatabase.getInstance().getReference();
        rv = (RecyclerView)view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        events = new ArrayList<>();
        RVAdapter adapter = new RVAdapter(events);
        rv.setAdapter(adapter);

        db.child("/events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                events = new ArrayList<Event>();

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    Log.i("Test","Key -> " + child.getKey());
                    Event event = child.getValue(Event.class);
                    events.add(event);
                }
                rv.setAdapter(new RVAdapter(events));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Test", "Error: 0xA2");
            }
        });




//        Button sub1 = (Button)view.findViewById(R.id.subTopic1);
//        sub1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                FirebaseMessaging.getInstance().subscribeToTopic("one");
//            }
//        });
//
//        Button sub2 = (Button)view.findViewById(R.id.unsubTopic1);
//        sub2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                FirebaseMessaging.getInstance().unsubscribeFromTopic("one");
//            }
//        });
//
//        Button sub3 = (Button)view.findViewById(R.id.subTopic2);
//        sub3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                FirebaseMessaging.getInstance().subscribeToTopic("two");
//            }
//        });
//
//        Button sub4 = (Button)view.findViewById(R.id.unsubTopic2);
//        sub4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                FirebaseMessaging.getInstance().unsubscribeFromTopic("two");
//            }
//        });

//        if (getActivity().getIntent().getExtras() != null) {
//            for (String key : getActivity().getIntent().getExtras().keySet()) {
//                String value = getActivity().getIntent().getExtras().getString(key);
//                Log.d("Test", "Key2: " + key + " Value2: " + value);
//            }
//        }
    }
}
