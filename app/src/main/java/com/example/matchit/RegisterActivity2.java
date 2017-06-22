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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
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
    ArrayList<String> list,list2;
    TextView txt,txt2;


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
        list = new ArrayList<String>();
        list2 = new ArrayList<String>();



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
                Intent i = RegisterActivity2.this.getIntent();
                String name = i.getStringExtra("fullname");
                String nric = i.getStringExtra("nric");
                String address = i.getStringExtra("address");
                String postalcode = i.getStringExtra("postalcode");
                String gender = i.getStringExtra("gender");
                String contactnumber = i.getStringExtra("contactnumber");
                String email = i.getStringExtra("email");
                String password = i.getStringExtra("password");

                Log.i("Test", "Name -> " + name + ", nric -> " +nric + ", address -> " + address+ ", postalcode -> " + postalcode + ", gender -> " + gender + ", contactnumber -> " + contactnumber + ", email -> " + email + ", pw -> " + password);

                String nationality = inputNationality.getText().toString().trim();
                String race = inputRace.getSelectedItem().toString();
                String specialization = inputSpecializedArea.getText().toString().trim();
                String occupation = inputOccupation.getText().toString().trim();
                String org_name = "";
                String uen = "";
                String liason_contact = "";

                for (String str : list) {
                    txt.setText(txt.getText().toString() + " , " + str);
                }
                for (String str : list2) {
                    txt2.setText(txt2.getText().toString() + " , " + str);
                }
                String languagespoken = txt.getText().toString();
                String languagewritten = txt2.getText().toString();

                registerUser(name, email, password, nric, org_name, uen, liason_contact, address, postalcode, gender, contactnumber, nationality, race, specialization, occupation, languagespoken, languagewritten);

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

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
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
                        db.addUser(jObj);

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
            protected Map<String, String> getParams() { //sending
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
}