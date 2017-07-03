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

public class Setting extends Fragment {
    private static final String TAG = Setting.class.getSimpleName();

    private Button update;
    private EditText inputFullName;
    private EditText inputContact;
    private EditText inputAddress;
    private EditText inputPostalCode;
    private EditText inputPassword;
    private CheckBox englishSpoken,englishWritten,mandarinSpoken,mandarinWritten,malaySpoken,malayWritten,tamilSpoken,tamilWritten,othersSpoken,othersWritten;
    ArrayList<String> list,list2;

    private ProgressDialog pDialog;

    private SQLiteHandler db;
    public HashMap<String,String> user;
    private SessionManager session;
    View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.individualsetting, container, false);
        inputFullName = (EditText) myView.findViewById(R.id.accountName);
        inputContact = (EditText) myView.findViewById(R.id.accountContactNumber);
        inputAddress = (EditText) myView.findViewById(R.id.accountAddress);
        inputPostalCode = (EditText) myView.findViewById(R.id.accountPostalCode);
        inputPassword = (EditText) myView.findViewById(R.id.accountPassword);

        update = (Button) myView.findViewById(R.id.btnUpdate);
        englishSpoken = (CheckBox) myView.findViewById(R.id.checkBoxEnglish);
        englishWritten = (CheckBox) myView.findViewById(R.id.checkBoxEnglishWritten);
        mandarinSpoken = (CheckBox) myView.findViewById(R.id.checkBoxMandarin);
        mandarinWritten = (CheckBox) myView.findViewById(R.id.checkBoxMandarinWritten);
        malaySpoken = (CheckBox) myView.findViewById(R.id.checkBoxMalay);
        malayWritten = (CheckBox) myView.findViewById(R.id.checkBoxMalayWritten);
        tamilSpoken = (CheckBox) myView.findViewById(R.id.checkBoxTamil);
        tamilWritten = (CheckBox) myView.findViewById(R.id.checkBoxTamilWritten);
        othersSpoken = (CheckBox) myView.findViewById(R.id.checkBoxOthers);
        othersWritten = (CheckBox) myView.findViewById(R.id.checkBoxOthersWritten);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getActivity().getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());

        user = db.getUserDetails();

        String name = user.get("name");
        String contact = user.get("contactnumber");
        String address = user.get("address");
        String postalcode = user.get("postalcode");
        String langspoken = user.get("languagespoken");
        String langwritten = user.get("languagewritten");


        inputFullName.setText(name);
        inputContact.setText(contact);
        inputAddress.setText(address);
        inputPostalCode.setText(postalcode);

        //Lang Spoken

        if (langspoken.contains("English"))
        {
            englishSpoken.setChecked(true);
        }

        if (langspoken.contains("Mandarin"))
        {
            mandarinSpoken.setChecked(true);
        }

        if (langspoken.contains("Malay"))
        {
            malaySpoken.setChecked(true);
        }

        if (langspoken.contains("Tamil"))
        {
            tamilSpoken.setChecked(true);
        }

        if (langspoken.contains("Others"))
        {
            othersSpoken.setChecked(true);
        }

        //Lang Written

        if (langwritten.contains("English"))
        {
            englishWritten.setChecked(true);
        }

        if (langwritten.contains("Mandarin"))
        {
            mandarinWritten.setChecked(true);
        }

        if (langwritten.contains("Malay"))
        {
            malayWritten.setChecked(true);
        }

        if (langwritten.contains("Tamil"))
        {
            tamilWritten.setChecked(true);
        }

        if (langwritten.contains("Others"))
        {
            othersWritten.setChecked(true);
        }


        // Register Button Click event
        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String newname = inputFullName.getText().toString().trim();
                String newpassword = inputPassword.getText().toString().trim();
                String newcontact = inputContact.getText().toString().trim();
                String newaddress = inputAddress.getText().toString().trim();
                String newpostalcode = inputPostalCode.getText().toString().trim();
                String uid = user.get("uid");


//                for (String str : list) {
//                    txt.setText(txt.getText().toString() + " , " + str);
//                }
//                for (String str : list2) {
//                    txt2.setText(txt2.getText().toString() + " , " + str);
//                }
//                String languagespoken = txt.getText().toString();
//                String languagewritten = txt2.getText().toString();
                String spoken = TextUtils.join(",",list); //JOEL IS A NOOB
                String written = TextUtils.join(",",list2); // OMG JOEL IS REALLY A NOOB
                updateUser(uid, newname, newpassword, newaddress, newpostalcode, newcontact, spoken, written);

            }
        });
        return myView;
    }

    public void onCheckboxClicked(View v) {

        boolean checked = ((CheckBox) v).isChecked();

        switch(v.getId()) {
            case R.id.checkBoxEnglish:
                list.add(englishSpoken.getTag().toString());


                break;
            case R.id.checkBoxEnglishWritten:
                list2.add(englishWritten.getTag().toString());

                break;

            case R.id.checkBoxMandarin:
                list.add(mandarinSpoken.getTag().toString());

                break;
            case R.id.checkBoxMandarinWritten:
                list2.add(mandarinWritten.getTag().toString());

                break;
            case R.id.checkBoxMalay:
                list.add(malaySpoken.getTag().toString());

                break;
            case R.id.checkBoxMalayWritten:
                list2.add(malayWritten.getTag().toString());

                break;
            case R.id.checkBoxTamil:
                list.add(tamilSpoken.getTag().toString());

                break;
            case R.id.checkBoxTamilWritten:
                list2.add(tamilWritten.getTag().toString());

                break;
            case R.id.checkBoxOthers:
                list.add(othersSpoken.getTag().toString());

                break;
            case R.id.checkBoxOthersWritten:
                list2.add(othersWritten.getTag().toString());

                break;
        }
    }

    private void updateUser(final String uid, final String name,
                              final String password, final String address, final String postalcode, final String contactnumber, final String languagespoken, final String languagewritten) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Updating ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE, new Response.Listener<String>() {

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
                params.put("uid",uid);
                params.put("name", name);
                params.put("password", password);
                params.put("address", address);
                params.put("postalcode", postalcode);
                params.put("contactnumber", contactnumber);
                params.put("languagespoken", languagespoken);
                params.put("languagewritten", languagewritten);

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
