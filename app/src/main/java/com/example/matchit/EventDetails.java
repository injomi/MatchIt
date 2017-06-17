package com.example.matchit;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Joel on 13/6/17.
 */

public class EventDetails extends Fragment implements  View.OnClickListener {

    public static final String EVENT_ID_KEY = "eventID";
    View myView;
    private Button btnViewCompanyInfo;
    private Button regEvent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.event_details, container, false);

        btnViewCompanyInfo = (Button) myView.findViewById(R.id.viewOrganizationInfo);
        btnViewCompanyInfo.setOnClickListener(this);
        regEvent = (Button) myView.findViewById(R.id.buttonReg);
        regEvent.setOnClickListener(this);

        //return super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int eventID = bundle.getInt(EVENT_ID_KEY, 0);
            Log.i("Test","fragment argument of eventID -> " + eventID);
        }
        return myView;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.viewOrganizationInfo:
                Intent intent = new Intent(getActivity(), OrganizationInfo.class);
                startActivity(intent);
                break;
        }
    }
}
