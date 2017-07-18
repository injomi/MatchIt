package com.example.matchit;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by josephtyx on 4/6/17.
 */

public class OrgSetting extends Fragment {
    private static final String TAG = Setting.class.getSimpleName();

    private Button updateProfile;
    private Button updatePassword;
    private EditText inputContact;
    private EditText inputOrgName;
    private EditText inputOrgUEN;
    private EditText inputName;
    private EditText newPassword;
    private EditText retypePassword;
    private EditText oldPassword;

    //ArrayList<String> list,list2;


    private ProgressDialog pDialog;

    private SQLiteHandler db;
    public HashMap<String,String> user;
    private SessionManager session;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.organizationsettings, container, false);
        inputContact = (EditText) myView.findViewById(R.id.accountContactNumber);
        inputName = (EditText) myView.findViewById(R.id.accountName);
        inputOrgName = (EditText) myView.findViewById(R.id.orgName);
        inputOrgUEN = (EditText)myView.findViewById(R.id.orgUEN);
        newPassword = (EditText) myView.findViewById(R.id.accountPassword);
        oldPassword = (EditText) myView.findViewById(R.id.oldPassword);
        retypePassword = (EditText)myView.findViewById(R.id.accountCfmPassword) ;

        updateProfile = (Button) myView.findViewById(R.id.btnUpdateProfile);
        updatePassword = (Button) myView.findViewById(R.id.btnUpdate);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        user = db.getUserDetails();

        String orgname = user.get("org_name");
        String contact = user.get("liason_contact");
        String uen = user.get("uen");
        String name = user.get("name");


        inputContact.setText(contact);
        inputOrgUEN.setText(uen);
        inputOrgName.setText(orgname);
        inputName.setText(name);
        // Register Button Click event
        updateProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String newname = inputName.getText().toString().trim();
                String newnorg = inputOrgName.getText().toString().trim();
                String newcontact = inputContact.getText().toString().trim();
                String newuen = inputOrgUEN.getText().toString().trim();
                String uid = user.get("uid");

//                for (String str : list) {
//                    txt.setText(txt.getText().toString() + " , " + str);
//                }
//                for (String str : list2) {
//                    txt2.setText(txt2.getText().toString() + " , " + str);
//                }
//                String languagespoken = txt.getText().toString();
//                String languagewritten = txt2.getText().toString();
//                String spoken = TextUtils.join(",",list); //JOEL IS A NOOB
//                String written = TextUtils.join(",",list2); // OMG JOEL IS REALLY A NOOB
                updateUser("profile",uid, newname, newcontact,newuen,newnorg,"","");

            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String oldPW = oldPassword.getText().toString();
                String newPW = newPassword.getText().toString();
                String cfmPW = retypePassword.getText().toString();
                if(!cfmPW.equals(newPW)){
                    Toast.makeText(getActivity(), "Passwords mismatch", Toast.LENGTH_LONG).show();
                    return;
                }
                updateUser("password",user.get("uid"), "","", "", "",newPW,oldPW);
            }
        });
        return myView;
    }

    private void updateUser(final String queryType, final String uid, final String name,final String contactnumber, final String neworg, final String uen, final String newPass, final String oldPass) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Updating ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE2, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { //response
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        // Inserting row in users table
                        db.updateUser(jObj);

                        Toast.makeText(getActivity().getApplicationContext(), "Update Successful!", Toast.LENGTH_LONG).show();


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
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() { //sending
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                if(queryType.equals("profile")) {
                    params.put("queryType", queryType);
                    params.put("uid", uid);
                    params.put("name", name);
                    params.put("contactnumber", contactnumber);
                    params.put("uen", uen);
                    params.put("newOrg", neworg);
                }
                else if(queryType.equals("password")){
                    params.put("uid",uid);
                    params.put("oldPw",oldPass);
                    params.put("newPw",newPass);
                    params.put("queryType",queryType);
                }
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
