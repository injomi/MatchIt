/**
 * Created by Joel on 04-Jun-17.
 */
package com.example.matchit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class RegisterActivityCop extends Activity {
    private static final String TAG = RegisterActivityCop.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputNric;
    private EditText inputOrgName;
    private EditText inputUen;
    private EditText inputContact;
    private EditText inputEmail;
    private EditText inputPassword;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_cop);
        //Edit Text
        inputFullName = (EditText) findViewById(R.id.name);
        inputNric = (EditText) findViewById(R.id.nric);
        inputOrgName = (EditText) findViewById(R.id.organizationname);
        inputUen = (EditText) findViewById(R.id.UEN);
        inputContact = (EditText) findViewById(R.id.contactnumber);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

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
            Intent intent = new Intent(RegisterActivityCop.this,
                    HomeScreen.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String nric = inputNric.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String org_name = inputOrgName.getText().toString().trim();
                String uen = inputUen.getText().toString().trim();
                String liason_contact = inputContact.getText().toString().trim();
                String address = "";
                String postalcode = "";
                String gender = "";
                String contactnumber = "";
                String nationality = "";
                String race = "";
                String specialization = "";
                String occupation = "";
                String languagespoken = "";
                String languagewritten = "";


                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !nric.isEmpty() && !org_name.isEmpty() && !uen.isEmpty() && !liason_contact.isEmpty()) {
                    String namePattern = "^[a-zA-Z_ ]*$";
                    if(!name.matches(namePattern))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Name can only contain letters", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }

                    if (!nric.matches("\\p{Upper}\\d{7}\\p{Upper}"))
                    {
                        Toast.makeText(getApplicationContext(),
                                "NRIC not in the right format", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (!email.matches(emailPattern))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Email not in the right format", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }

                    String orgName = "^[a-zA-Z_ ]*$";
                    if(!name.matches(orgName))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Organization name can only contain letters", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    registerUser(name, email, password, nric, org_name, uen, liason_contact, address, postalcode, gender, contactnumber, nationality, race, specialization, occupation, languagespoken, languagewritten);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
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
                              final String password, final String nric, final String org_name, final String uen, final String liason_contact, final String address, final String postalcode, final String gender, final String contactnumber, final String nationality, final String race, final String specialization, final String occupation, final String languagespoken, final String languagewritten) {
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
                        // Inserting row in users table
                        db.addUser(jObj);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivityCop.this,
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
                params.put("address", address);
                params.put("postalcode", postalcode);
                params.put("gender", gender);
                params.put("contactnumber", contactnumber);
                params.put("nationality", nationality);
                params.put("race", race);
                params.put("specialization", specialization);
                params.put("occupation", occupation);
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RegisterActivityCop.this, RegisterSelection.class);
        startActivity(intent);
        finish(); // Destroy activity A and not exist in Back stack
    }
}