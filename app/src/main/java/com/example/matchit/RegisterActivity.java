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
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.matchit.activity.LoginActivity;
import com.example.matchit.helper.SQLiteHandler;
import com.example.matchit.helper.SessionManager;

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnNextPage;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputNric;
    private EditText inputAddress;
    private EditText inputPostalCode;
    private RadioButton inputMale;
    private RadioButton inputFemale;
    private EditText inputContact;
    private EditText inputEmail;
    private EditText inputPassword;
    String radioValue;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Edit Text
        inputFullName = (EditText) findViewById(R.id.name);
        inputNric = (EditText) findViewById(R.id.nric);
        inputAddress = (EditText) findViewById(R.id.address);
        inputPostalCode = (EditText) findViewById(R.id.postalcode);
        inputMale = (RadioButton) findViewById(R.id.radioMale);
        inputFemale = (RadioButton) findViewById(R.id.radioFemale);
        inputContact = (EditText) findViewById(R.id.contactnumber);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);

        //Buttons
        btnNextPage = (Button) findViewById(R.id.btnNextPage);
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
            Intent intent = new Intent(RegisterActivity.this,
                    HomeScreen.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        if(btnNextPage==null)
            Log.i("Test", "btnNextPage is null");
        else
            Log.i("Test", "btnNextPage is not null");
        btnNextPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //Get gender value
                if (inputMale.isChecked()){
                    radioValue = "Male";
                }
                else
                {
                    radioValue = "Female";
                }
                //Validate form
                if (!inputFullName.getText().toString().isEmpty() && !inputNric.getText().toString().isEmpty() && !inputAddress.getText().toString().isEmpty() && !inputPostalCode.getText().toString().isEmpty() && !inputContact.getText().toString().isEmpty() && !inputEmail.getText().toString().isEmpty() && !inputPassword.getText().toString().isEmpty()) {
                    //Validate nric
                    String nric = inputNric.getText().toString().trim();
                    if (!nric.matches("\\p{Upper}\\d{7}\\p{Upper}"))
                    {
                        Toast.makeText(getApplicationContext(),
                                "NRIC not in the right format", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    //Validate email
                    String email = inputEmail.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (!email.matches(emailPattern))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Email not in the right format", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                } else {
                    //Validate nric
                    String nric = inputNric.getText().toString().trim();
                    if (!nric.matches("\\p{Upper}\\d{7}\\p{Upper}"))
                    {
                        Toast.makeText(getApplicationContext(),
                                "NRIC not in the right format", Toast.LENGTH_LONG)
                                .show();
                    }
                    //Validate email
                    String email = inputEmail.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                    if (!email.matches(emailPattern))
                    {
                        Toast.makeText(getApplicationContext(),
                                "Email not in the right format", Toast.LENGTH_LONG)
                                .show();
                    }
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();

                    return;
                }
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity2.class);
                i.putExtra("fullname", inputFullName.getText().toString());
                i.putExtra("nric", inputNric.getText().toString());
                i.putExtra("address", inputAddress.getText().toString());
                i.putExtra("postalcode", inputPostalCode.getText().toString());
                i.putExtra("gender", radioValue.toString());
                i.putExtra("contactnumber", inputContact.getText().toString());
                i.putExtra("email", inputEmail.getText().toString());
                i.putExtra("password", inputPassword.getText().toString());

                startActivity(i);
                finish();
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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}