package com.example.matchit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
    EditText search;
    RVAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.home, container, false);

        //return super.onCreateView(inflater, container, savedInstanceState);
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fragmentManager = getFragmentManager();
        db = FirebaseDatabase.getInstance().getReference();
        rv = (RecyclerView)view.findViewById(R.id.rv);
        search = (EditText)view.findViewById(R.id.search_box);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        events = new ArrayList<>();
        adapter = new RVAdapter(events,getActivity());
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
                adapter = new RVAdapter(events,getActivity());
                adapter.filter(search.getText().toString());
                rv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Test", "Error: 0xA2");
            }
        });


        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                Log.i("Test","Search -> " + arg0);
                adapter.filter(arg0.toString());
                rv.invalidateItemDecorations();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {

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
