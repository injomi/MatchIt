/**
 * Created by Joel on 04-Jun-17.
 */
package com.example.matchit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matchit.activity.LoginActivity;
import com.example.matchit.app.AppConfig;
import com.example.matchit.app.AppController;
import com.example.matchit.helper.SQLiteHandler;
import com.example.matchit.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity2 extends Activity {
    private static final String TAG = RegisterActivity2.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputNationality;
    private Spinner inputRace;
    private EditText inputSpecializedArea;
    private EditText inputOccupation;
    private CheckBox englishSpoken,englishWritten,mandarinSpoken,mandarinWritten,malaySpoken,malayWritten,tamilSpoken,tamilWritten,othersSpoken,othersWritten;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_2);
        //Declarations
        inputNationality = (EditText) findViewById(R.id.nationality);
        inputRace = (Spinner) findViewById(R.id.spinner);
        inputSpecializedArea = (EditText) findViewById(R.id.specialized);
        inputOccupation = (EditText) findViewById(R.id.occupation);
        englishSpoken = (CheckBox) findViewById(R.id.checkBoxEnglish);
        englishWritten = (CheckBox) findViewById(R.id.checkBoxEnglishWritten);
        mandarinSpoken = (CheckBox) findViewById(R.id.checkBoxMandarin);
        mandarinWritten = (CheckBox) findViewById(R.id.checkBoxMandarinWritten);
        malaySpoken = (CheckBox) findViewById(R.id.checkBoxMalay);
        malayWritten = (CheckBox) findViewById(R.id.checkBoxMalayWritten);
        tamilSpoken = (CheckBox) findViewById(R.id.checkBoxTamil);
        tamilWritten = (CheckBox) findViewById(R.id.checkBoxTamilWritten);
        othersSpoken = (CheckBox) findViewById(R.id.checkBoxOthers);
        othersWritten = (CheckBox) findViewById(R.id.checkBoxOthersWritten);

        //Buttons
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity2.this,
                    HomeScreen.class);
            startActivity(intent);
            finish();
        }


        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences prefs = getPreferences(MODE_PRIVATE);
                String name = prefs.getString("fullname", null);
                String nric = prefs.getString("nric", null);
                String address = prefs.getString("address", null);
                String postalcode = prefs.getString("postalcode", null);
                String gender = prefs.getString("gender", null);
                String contactnumber = prefs.getString("contactnumber", null);
                String email = prefs.getString("email", null);
                String password = prefs.getString("password", null);

                String nationality = inputNationality.getText().toString().trim();
                String race = inputRace.getSelectedItem().toString();
                String specialization = inputSpecializedArea.getText().toString().trim();
                String occupation = inputOccupation.getText().toString().trim();




                //registerUser(name, email, password, nric, org_name, uen, liason_contact);

            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String email,
                              final String password, final String nric, final String org_name, final String uen, final String liason_contact) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        String nric = user.getString("nric");
                        String org_name = user.getString("org_name");
                        String uen = user.getString("uen");
                        String liason_contact = user.getString("liason_contact");

                        // Inserting row in users table
                        db.addUser(name, email, uid, created_at, nric, org_name, uen, liason_contact);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity2.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("nric", nric);
                params.put("org_name", org_name);
                params.put("uen", uen);
                params.put("liason_contact", liason_contact);

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