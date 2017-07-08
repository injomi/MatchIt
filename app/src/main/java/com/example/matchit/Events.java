package com.example.matchit;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matchit.app.AppConfig;
import com.example.matchit.app.AppController;
import com.example.matchit.helper.EventAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by josephtyx on 4/6/17.
 */

public class Events extends Fragment {

    View myView;
    DatabaseReference db;
    String UID;
    ListView lv_past;
    ListView lv_current;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.events, container, false);
        db = FirebaseDatabase.getInstance().getReference();
        UID = ((HomeScreen)this.getActivity()).user.get("uid");
        queryDB(UID);
        lv_past = (ListView)myView.findViewById(R.id.listViewPastEvent);
        lv_current = (ListView)myView.findViewById(R.id.listViewCurrentEvent);
        return myView;
    }

    private void queryDB(final String uid){
        final Context context = this.getActivity();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_USEREVENTS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("Test", "Service Response: " + response.toString());
                try {
                    final JSONObject jRes = new JSONObject(response);
                    boolean error = jRes.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        JSONArray past = jRes.getJSONArray("past");
                        JSONArray current = jRes.getJSONArray("current");
                        lv_past.setAdapter(new EventAdapter(context,past));
                        lv_current.setAdapter(new EventAdapter(context,current));

                    } else { //error msges TODO:fill meaningful error msges

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
                params.put("uniqueID", uid);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "Test");
    }
}
