package com.example.matchit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matchit.activity.LoginActivity;
import com.example.matchit.app.AppConfig;
import com.example.matchit.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joel on 13/6/17.
 */

public class EventDetails extends Fragment implements  View.OnClickListener {

    public static final String EVENT_ID_KEY = "eventID";
    View myView;
    Button btnViewCompanyInfo;
    Button regEvent;
    Button cancelEvent;
    TextView tv_startTime;
    TextView tv_endTime;
    TextView tv_description;
    TextView tv_category;
    TextView tv_name;
    TextView tv_location;
    TextView tv_org;
    Spinner sp_sessions;

    JSONObject jObj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.event_details, container, false);

        btnViewCompanyInfo = (Button) myView.findViewById(R.id.viewOrganizationInfo);
        btnViewCompanyInfo.setOnClickListener(this);
        regEvent = (Button) myView.findViewById(R.id.buttonReg);
        regEvent.setOnClickListener(this);
        cancelEvent = (Button) myView.findViewById(R.id.buttonCancel);
        cancelEvent.setOnClickListener(this);
        tv_startTime = (TextView)myView.findViewById(R.id.eventStartTime);
        tv_endTime = (TextView)myView.findViewById(R.id.eventEndTime);
        tv_description = (TextView)myView.findViewById(R.id.eventDescription);
        tv_category = (TextView)myView.findViewById(R.id.eventCategory);
        sp_sessions = (Spinner)myView.findViewById(R.id.spinnerSession);
        sp_sessions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if(jObj.getJSONArray("sessions").getJSONObject(i).getString("status").equals("registered")){
                        cancelEvent.setVisibility(View.GONE);
                        regEvent.setVisibility(View.VISIBLE);
                    }
                    else{
                        cancelEvent.setVisibility(View.VISIBLE);
                        regEvent.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    Log.i("Test",e.toString());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        tv_location = (TextView)myView.findViewById(R.id.eventLocation);
        tv_name = (TextView)myView.findViewById(R.id.eventName);
        tv_org = (TextView)myView.findViewById(R.id.organizedBy);

        //return super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int eventID = bundle.getInt(EVENT_ID_KEY, 0);
            Log.i("Test","fragment argument of eventID -> " + eventID);
            String uid = ((HomeScreen)this.getActivity()).user.get("uid");
            //Log.i("Test","fragment argument of eventID -> " + eventID + ", UID -> " + uid + "//");
            getEventDetails(eventID,uid);
        }
        return myView;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.viewOrganizationInfo:
                FragmentManager fragmentManager = ((HomeScreen)v.getContext()).getFragmentManager();
                OrganizationInfo organizationInfo = new OrganizationInfo();

                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , organizationInfo)
                        .commit();
                break;
            case R.id.buttonReg:
                Log.i("Test","Register");
                break;
            case R.id.buttonCancel:
                Log.i("Test","Cancel");
                break;
        }
    }

    private void getEventDetails(final int eventID, final String uid){
        final Context context = this.getActivity();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_EVENTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Test", "Service Response: " + response.toString());
                try {
                    jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        //success
                        fillInformationToUI();
                    } else {
                        Toast.makeText(context, "Event not found", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Test", "Login Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventID", String.valueOf(eventID));
                params.put("uniqueID", uid);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "Test");
    }

    private void fillInformationToUI() throws JSONException {
        final Context context = this.getActivity();
        this.tv_category.setText(jObj.getString("event_category"));
        this.tv_description.setText(jObj.getString("event_desc"));
        this.tv_name.setText(jObj.getString("event_name"));
        this.tv_endTime.setText(jObj.getString("event_endDate"));
        this.tv_location.setText(jObj.getString("event_location"));
        this.tv_startTime.setText(jObj.getString("event_startDate"));

        ArrayList<String> sessionList = new ArrayList();
        JSONArray sessions = jObj.getJSONArray("sessions");
        for(int i =0; i<sessions.length(); i++){
            JSONObject session = sessions.getJSONObject(i);
            String date = session.getString("event_date");
            String startTime = session.getString("event_startTime");
            String endTime = session.getString("event_endTime");
            sessionList.add(date + " " + startTime+"-"+endTime);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sessionList);
        sp_sessions.setAdapter(adapter);
    }
}
