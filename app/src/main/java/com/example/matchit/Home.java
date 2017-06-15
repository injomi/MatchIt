package com.example.matchit;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by josephtyx on 4/6/17.
 */

public class Home extends Fragment {

    View myView;

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

        Button sub1 = (Button)view.findViewById(R.id.subTopic1);
        sub1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseMessaging.getInstance().subscribeToTopic("one");
            }
        });

        Button sub2 = (Button)view.findViewById(R.id.unsubTopic1);
        sub2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("one");
            }
        });

        Button sub3 = (Button)view.findViewById(R.id.subTopic2);
        sub3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseMessaging.getInstance().subscribeToTopic("two");
            }
        });

        Button sub4 = (Button)view.findViewById(R.id.unsubTopic2);
        sub4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("two");
            }
        });

//        if (getActivity().getIntent().getExtras() != null) {
//            for (String key : getActivity().getIntent().getExtras().keySet()) {
//                String value = getActivity().getIntent().getExtras().getString(key);
//                Log.d("Test", "Key2: " + key + " Value2: " + value);
//            }
//        }
    }
}
