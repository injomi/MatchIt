package com.example.matchit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.example.matchit.app.AppConfig;
import com.example.matchit.app.AppController;
import com.example.matchit.helper.RVAdapter;
import com.example.matchit.model.Event;
import com.example.matchit.model.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

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
    TextView tv_description;
    TextView tv_name;
    TextView tv_location;
    Spinner sp_sessions;
    JSONObject jObj;
    int eventID;
    String UID;
    DatabaseReference db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.event_details, container, false);
        db = FirebaseDatabase.getInstance().getReference();
        btnViewCompanyInfo = (Button) myView.findViewById(R.id.viewOrganizationInfo);
        btnViewCompanyInfo.setOnClickListener(this);
        regEvent = (Button) myView.findViewById(R.id.buttonReg);
        regEvent.setOnClickListener(this);
        cancelEvent = (Button) myView.findViewById(R.id.buttonCancel);
        cancelEvent.setOnClickListener(this);
        tv_description = (TextView)myView.findViewById(R.id.eventDescription);
        sp_sessions = (Spinner)myView.findViewById(R.id.spinnerSession);
        sp_sessions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    swapButtons(jObj.getJSONArray("sessions").getJSONObject(i).getString("status"));
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

        //return super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            eventID = bundle.getInt(EVENT_ID_KEY, 0);
            Log.i("Test","fragment argument of eventID -> " + eventID);
            UID = ((HomeScreen)this.getActivity()).user.get("uid");
            //Log.i("Test","fragment argument of eventID -> " + eventID + ", UID -> " + uid + "//");
            queryEventService("getDetails",eventID,UID,0);
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
                //queryEventService("register",eventID);
                try {
                    Log.i("Test","Register");
                    int sessionID = jObj.getJSONArray("sessions").getJSONObject(
                            sp_sessions.getSelectedItemPosition()).getInt("SID");
                    queryEventService("register",eventID,UID,sessionID);
                } catch (JSONException e) {
                    Log.i("Test", "JSON error" + e.toString());
                }
                break;
            case R.id.buttonCancel:
                Log.i("Test","Cancel");
                createUnregisterDialog();
                break;
        }
    }

    private void registeredHandler(final JSONObject jRes, final int sessionID) throws JSONException {
        db.child("/events/" + eventID + "/sessions/" +sessionID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                try {
                    Session session = dataSnapshot.getValue(Session.class);
                    session.setVolunteerNo(jRes.getInt("count"));
                    db.child("/events/" + eventID + "/sessions/" +sessionID).setValue(session);
                } catch (JSONException e) {
                    Log.i("Test",e.toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Test", "Error: 0xA3");
            }
        });
        JSONObject tmp = jObj.getJSONArray("sessions").getJSONObject(
                sp_sessions.getSelectedItemPosition());
        tmp.put("participation_count",jRes.getInt("count"));
        tmp.put("status",jRes.getString("status"));
        swapButtons(jRes.getString("status"));
    }

    private void queryEventService(final String queryType, final int eventID, final String uid, final int sessionID){
        final Context context = this.getActivity();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_EVENTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Test", "Service Response: " + response.toString());
                try {
                    final JSONObject jRes = new JSONObject(response);
                    boolean error = jRes.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        //success
                        if(queryType.equals("getDetails")) {
                            jObj = new JSONObject(response);
                            fillInformationToUI();
                        }
                        else if(queryType.equals("register")) {
                            //update firebase and json
                            if(jRes.getString("status").equals("registered")){
                                registeredHandler(jRes, sessionID);
                            }
                            else if(jRes.getString("status").equals("waiting")) {
                                //waiting
                                //handle waiting (dialog boxes)
                                createWaitingDialog();
                            }
                            else{
                                //unavailable
                                Toast.makeText(context, "Sorry, this session is full. Please try another session.", Toast.LENGTH_LONG).show();
                            }
                        }
                        else if(queryType.equals("unregister"))
                            registeredHandler(jRes, sessionID); //unregister -> tricky, not only drop table but execute a php script to push notification to all waiting volunteers
                        else if(queryType.equals("waiting")){
                            registeredHandler(jRes, sessionID);
                            //handle waiting query (confirmed/unconfirmed) -> probably copy register
                        }
                    } else { //error msges TODO:fill meaningful error msges
                        if(queryType.equals("getDetails"))
                            Toast.makeText(context, "Event not found", Toast.LENGTH_LONG).show();
                        else if(queryType.equals("register")) {

                            return;
                        }
                        else if(queryType.equals("unregister"))
                            return;
                        else if(queryType.equals("waiting")){
                            return;
                        }
                    }
                } catch (JSONException e) {
                    // JSON error
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Test", "Service Error: " + error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventID", String.valueOf(eventID));
                params.put("uniqueID", uid);
                params.put("queryType", queryType);
                if(sessionID != 0){
                    params.put("sessionID", String.valueOf(sessionID));
                }
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "Test");
    }

    private void fillInformationToUI() throws JSONException {
        final Context context = this.getActivity();
        //this.tv_category.setText(jObj.getString("event_category"));
        this.tv_description.setText(jObj.getString("event_desc"));
        this.tv_name.setText(jObj.getString("event_name"));
        this.tv_location.setText(jObj.getString("event_location"));

        ArrayList<String> sessionList = new ArrayList();
        JSONArray sessions = jObj.getJSONArray("sessions");
        for(int i =0; i<sessions.length(); i++){
            JSONObject session = sessions.getJSONObject(i);
            String date = session.getString("event_date");
            sessionList.add(date);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, sessionList);
        sp_sessions.setAdapter(adapter);
    }

    private void swapButtons(String action){
        switch(action){
            case "registered":
                cancelEvent.setVisibility(View.VISIBLE);
                regEvent.setVisibility(View.GONE);
                break;
            case "unregistered":
                cancelEvent.setVisibility(View.GONE);
                regEvent.setVisibility(View.VISIBLE);
                break;
            default:
                Log.i("Test","Error: Should not reach here: A3xA2");
        }
    }

    private void createUnregisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to unregister from this event?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    Log.i("Test","Register");
                    int sessionID = jObj.getJSONArray("sessions").getJSONObject(
                            sp_sessions.getSelectedItemPosition()).getInt("SID");
                    queryEventService("unregister",eventID,UID,sessionID);
                } catch (JSONException e) {
                    Log.i("Test", "JSON error" + e.toString());
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void createWaitingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Waiting List");
        builder.setMessage("This session is currently full. However, we could put you on waiting list and " +
                "notify you when a slot is available." +
                "\nWould you like to be notified?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                try {
                    Log.i("Test","Register");
                    int sessionID = jObj.getJSONArray("sessions").getJSONObject(
                            sp_sessions.getSelectedItemPosition()).getInt("SID");
                    queryEventService("waiting",eventID,UID,sessionID);
                } catch (JSONException e) {
                    Log.i("Test", "JSON error" + e.toString());
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
