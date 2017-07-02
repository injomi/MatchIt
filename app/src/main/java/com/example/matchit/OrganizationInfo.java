package com.example.matchit;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matchit.app.AppConfig;
import com.example.matchit.app.AppController;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Joel on 13/6/17.
 */

public class OrganizationInfo extends Fragment {

    public static final String ORG_ID_KEY = "orginfoID";
    View myView;
    TextView tv_orgname;
    TextView tv_address;
    TextView tv_contact;
    TextView tv_who;
    TextView tv_howthey;
    TextView tv_howyou;
    JSONObject jObj;
    int orgID;
    String UID;
    DatabaseReference db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        myView = inflater.inflate(R.layout.organizationinfo, container, false);
        db = FirebaseDatabase.getInstance().getReference();
        tv_orgname = (TextView)myView.findViewById(R.id.textOrgName);
        tv_address = (TextView)myView.findViewById(R.id.textAddress);
        tv_contact = (TextView)myView.findViewById(R.id.textContactNumber);
        tv_who = (TextView)myView.findViewById(R.id.textWhoTheyHelp);
        tv_howthey = (TextView)myView.findViewById(R.id.textHowTheyHelp);
        tv_howyou = (TextView)myView.findViewById(R.id.textHowYouCanBeInvolved);

        //return super.onCreateView(inflater, container, savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            orgID = bundle.getInt(ORG_ID_KEY, 0);
            Log.i("Test","fragment argument of orgID -> " + orgID);
            UID = ((HomeScreen)this.getActivity()).user.get("uid");
            //Log.i("Test","fragment argument of eventID -> " + eventID + ", UID -> " + uid + "//");
            queryEventService("getDetails",orgID,UID,0);
        }
        return myView;
    }

    private void queryEventService(final String queryType, final int AID, final String uid, final int sessionID){
        final Context context = this.getActivity();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORGINFO, new Response.Listener<String>() {

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
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "Test");
    }

    private void fillInformationToUI() throws JSONException {
        final Context context = this.getActivity();
        this.tv_orgname.setText(jObj.getString("org_name"));
        this.tv_address.setText(jObj.getString("org_address"));
        this.tv_contact.setText(jObj.getString("org_contact"));
        this.tv_who.setText(jObj.getString("org_who"));
        this.tv_howthey.setText(jObj.getString("org_how_they"));
        this.tv_howyou.setText(jObj.getString("org_how_you"));
    }
}
