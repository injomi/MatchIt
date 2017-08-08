package com.example.matchit;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import java.util.HashMap;
import java.util.Map;


public class Setting extends Fragment {
    private static final String TAG = Setting.class.getSimpleName();

    private Button updateProfile;
    private Button updatePassword;
    private EditText inputFullName;
    private EditText inputContact;
    private EditText inputAddress;
    private EditText inputPostalCode;
    private EditText newPassword;
    private EditText retypePassword;
    private EditText oldPassword;

    private CheckBox englishSpoken, englishWritten, mandarinSpoken, mandarinWritten, malaySpoken, malayWritten, tamilSpoken, tamilWritten, othersSpoken, othersWritten;
    //ArrayList<String> list,list2;


    private ProgressDialog pDialog;

    private SQLiteHandler db;
    public HashMap<String, String> user;
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
        newPassword = (EditText) myView.findViewById(R.id.accountPassword);
        oldPassword = (EditText) myView.findViewById(R.id.oldPassword);
        retypePassword = (EditText) myView.findViewById(R.id.accountCfmPassword);

        updateProfile = (Button) myView.findViewById(R.id.btnUpdateProfile);
        updatePassword = (Button) myView.findViewById(R.id.btnUpdate);
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

        if (langspoken.contains("English")) {
            englishSpoken.setChecked(true);
        }

        if (langspoken.contains("Mandarin")) {
            mandarinSpoken.setChecked(true);
        }

        if (langspoken.contains("Malay")) {
            malaySpoken.setChecked(true);
        }

        if (langspoken.contains("Tamil")) {
            tamilSpoken.setChecked(true);
        }

        if (langspoken.contains("Others")) {
            othersSpoken.setChecked(true);
        }

        //Lang Written

        if (langwritten.contains("English")) {
            englishWritten.setChecked(true);
        }

        if (langwritten.contains("Mandarin")) {
            mandarinWritten.setChecked(true);
        }

        if (langwritten.contains("Malay")) {
            malayWritten.setChecked(true);
        }

        if (langwritten.contains("Tamil")) {
            tamilWritten.setChecked(true);
        }

        if (langwritten.contains("Others")) {
            othersWritten.setChecked(true);
        }


        // Register Button Click event
        updateProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String newname = inputFullName.getText().toString().trim();
                String newcontact = inputContact.getText().toString().trim();
                String newaddress = inputAddress.getText().toString().trim();
                String newpostalcode = inputPostalCode.getText().toString().trim();
                String uid = user.get("uid");
                String spoken = "";
                String written = "";
                CheckBox[] writtenChks = {englishWritten, mandarinWritten, malayWritten, tamilWritten, othersWritten};
                CheckBox[] spokenChks = {englishSpoken, mandarinSpoken, malaySpoken, tamilSpoken, othersSpoken};
                for (int i = 0; i < writtenChks.length; i++) {
                    if (writtenChks[i].isChecked())
                        written += writtenChks[i].getTag().toString() + ",";
                    if (spokenChks[i].isChecked())
                        spoken += spokenChks[i].getTag().toString() + ",";
                }
                Log.i("Test","Spoken->"+spoken+",written->"+written);
                if (!newname.isEmpty() && !newcontact.isEmpty() && !newaddress.isEmpty() && !newpostalcode.isEmpty() && !spoken.isEmpty() && !written.isEmpty()) {
                    updateUser("profile", uid, newname, newaddress, newpostalcode, newcontact,
                            spoken.substring(0, spoken.length() - 1), written.substring(0, written.length() - 1), "", "");
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
        updatePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String oldPW = oldPassword.getText().toString();
                String newPW = newPassword.getText().toString();
                String cfmPW = retypePassword.getText().toString();
                if (!cfmPW.equals(newPW)) {
                    Toast.makeText(getActivity(), "Passwords mismatch", Toast.LENGTH_LONG).show();
                    return;
                }
                updateUser("password", user.get("uid"), "", "", "", "", "", "", newPW, oldPW);
            }
        });

        return myView;
    }

    private void updateUser(final String queryType, final String uid, final String name,
                            final String address, final String postalcode, final String contactnumber, final String languagespoken, final String languagewritten, final String newPass, final String oldPass) {
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
                if (queryType.equals("profile")) {
                    params.put("queryType", queryType);
                    params.put("uid", uid);
                    params.put("name", name);
                    params.put("address", address);
                    params.put("postalcode", postalcode);
                    params.put("contactnumber", contactnumber);
                    params.put("languagespoken", languagespoken);
                    params.put("languagewritten", languagewritten);
                } else if (queryType.equals("password")) {
                    params.put("uid", uid);
                    params.put("oldPw", oldPass);
                    params.put("newPw", newPass);
                    params.put("queryType", queryType);
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
