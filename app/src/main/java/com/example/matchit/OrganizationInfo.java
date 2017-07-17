package com.example.matchit;

import android.app.Fragment;
import android.app.ProgressDialog;
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
import com.example.matchit.helper.SQLiteHandler;
import com.example.matchit.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class OrganizationInfo extends Fragment {
    private static final String TAG = OrganizationInfo.class.getSimpleName();

    private TextView orgName;
    private TextView orgAddress;
    private TextView orgContact;
    private TextView orgWho;
    private TextView orgHow;
    private TextView orgYou;

    private ProgressDialog pDialog;

    private SQLiteHandler db;
    private SessionManager session;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.organizationinfo, container, false);

        orgName = (TextView) myView.findViewById(R.id.textOrgName);
        orgAddress = (TextView) myView.findViewById(R.id.textAddress);
        orgContact = (TextView) myView.findViewById(R.id.textContactNumber);
        orgWho = (TextView) myView.findViewById(R.id.textWhoTheyHelp);
        orgHow = (TextView) myView.findViewById(R.id.textHowTheyHelp);
        orgYou = (TextView) myView.findViewById(R.id.textHowYouCanBeInvolved);

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            getOrgInfo();
        }

        return myView;
    }

    private void getOrgInfo() {
        // Tag used to cancel the request
        String tag_string_req = "org_info";


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORGINFO, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { //response
                Log.d(TAG, "Org Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        String name = jObj.getString("org_name");
                        String address = jObj.getString("org_address");
                        String contact = jObj.getString("org_contact");
                        String who = jObj.getString("org_who");
                        String how = jObj.getString("org_how_they");
                        String howyou = jObj.getString("org_how_you");

                        orgName.setText(name);
                        orgAddress.setText(address);
                        orgContact.setText(contact);
                        orgWho.setText(who);
                        orgHow.setText(how);
                        orgYou.setText(howyou);

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Display Error! : " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
