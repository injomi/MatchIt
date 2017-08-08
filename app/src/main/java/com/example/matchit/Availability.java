package com.example.matchit;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matchit.app.AppConfig;
import com.example.matchit.app.AppController;
import com.example.matchit.helper.AvailAdapter;
import com.leavjenn.smoothdaterangepicker.date.SmoothDateRangePickerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Availability extends Fragment implements  View.OnClickListener {
    View myView;
    Button addAvail;
    ListView lv_avail;
    String UID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.myavailability, container, false);

        //return super.onCreateView(inflater, container, savedInstanceState);
        addAvail = (Button)myView.findViewById(R.id.cfmavail);
        addAvail.setOnClickListener(this);
        lv_avail = (ListView)myView.findViewById(R.id.listViewAvail);
        UID = ((HomeScreen)this.getActivity()).user.get("uid");
        queryAvailService("getAvailability", UID, 0, "", "");
        return myView;
    }

    @Override
    public void onClick(View v) { //opens the date picker
        SmoothDateRangePickerFragment smoothDateRangePickerFragment = SmoothDateRangePickerFragment.newInstance(
                new SmoothDateRangePickerFragment.OnDateRangeSetListener() {
                    @Override
                    public void onDateRangeSet(SmoothDateRangePickerFragment view,
                                               int yearStart, int monthStart,
                                               int dayStart, int yearEnd,
                                               int monthEnd, int dayEnd) {
                        String start = yearStart + "-" + String.format("%02d",monthStart+1) +
                                        "-" + String.format("%02d",dayStart);
                        String end = yearEnd + "-" + String.format("%02d",monthEnd+1) +
                                "-" + String.format("%02d",dayEnd);
                        Log.i("Test","start->"+start+", end->"+end);
                        AvailAdapter adapter = (AvailAdapter)lv_avail.getAdapter();
                        try {
                            if(adapter.isDuplicate(start,end)){
                                Toast.makeText(getActivity(), "Date range already set available", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            Log.i("Test","JSON error:" + e.toString());
                        }
                        queryAvailService("addAvailability",UID,0,start,end);
                    }
                });
        smoothDateRangePickerFragment.show(getFragmentManager(), "smoothDateRangePicker");
    }

    //send Post request to web service
    public void queryAvailService(final String queryType, final String uid, final int AVID, final String startDate, final String endDate){
        final Context context = this.getActivity();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_AVAILABILITY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Test", "Service Response: " + response.toString());
                try {
                    final JSONObject jRes = new JSONObject(response);
                    boolean error = jRes.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        //success
                        if(queryType.equals("getAvailability")) {
                            //...
                            lv_avail.setAdapter(new AvailAdapter(context,jRes.getJSONArray("availability"),Availability.this));
                        }
                        else if(queryType.equals("addAvailability")) {
                            queryAvailService("getAvailability", UID, 0, "", "");
                        }
                        else if(queryType.equals("removeAvailability")){
                            queryAvailService("getAvailability", UID, 0, "", "");
                        }
                    } else { //error msges TODO:fill meaningful error msges
                        if(queryType.equals("getAvailability")) {
                            //...
                        }
                        else if(queryType.equals("addAvailability")) {
                            //...
                        }
                        else if(queryType.equals("removeAvailability")){
                            //...
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
                Map<String, String> params = new HashMap<String, String>();
                params.put("uniqueID", uid);
                params.put("queryType", queryType);
                if(queryType.equals("addAvailability")) {
                    params.put("startDate", startDate);
                    params.put("endDate", endDate);
                }
                else if(queryType.equals("removeAvailability")) {
                    params.put("AVID", String.valueOf(AVID));
                }
                // Posting parameters to login url
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "Test");
    }
}
