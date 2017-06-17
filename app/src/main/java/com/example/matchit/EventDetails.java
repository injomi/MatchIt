package com.example.matchit;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Joel on 13/6/17.
 */

public class EventDetails extends Fragment implements  View.OnClickListener {

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
