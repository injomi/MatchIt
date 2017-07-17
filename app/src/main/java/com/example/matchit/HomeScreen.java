package com.example.matchit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matchit.activity.LoginActivity;
import com.example.matchit.app.AppConfig;
import com.example.matchit.app.AppController;
import com.example.matchit.app.barcode.BarcodeCaptureActivity;
import com.example.matchit.helper.SQLiteHandler;
import com.example.matchit.helper.SessionManager;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView txtName;
    private TextView txtEmail;


    private SQLiteHandler db;
    public HashMap<String,String> user;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FirebaseMessaging.getInstance().subscribeToTopic("test");

        //This is the message button at the bottom right corner
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());


        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite

        user = db.getUserDetails();


        String name = user.get("name");
        String email = user.get("email");


        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        if (getIntent().getExtras() != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame
                            , new EventDetails())
                    .commit();
        }
        else{
            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame
                            , new Home())
                    .commit();
        }
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(HomeScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_my_events) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new Events()) //My Events
                    .commit();
        } else if (id == R.id.nav_events) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new Home()) //Events
                    .commit();
        } else if (id == R.id.nav_availability) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new Availability()) //My Availability
                    .commit();
        } else if (id == R.id.nav_setting) {
            if(user.get("org_name").isEmpty()){ //individual
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , new Setting()) //Account Settings
                        .commit();
            }
            else{
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame
                                , new OrgSetting()) //Account Settings
                        .commit();
            }
        } else if (id == R.id.nav_logout) {
            logoutUser(); //Logout
        } else if (id == R.id.nav_attendance) {
            Intent intent = new Intent(getApplicationContext(), BarcodeCaptureActivity.class);
            startActivityForResult(intent, 1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //Toast.makeText(this, barcode.displayValue, Toast.LENGTH_LONG).show();
                    Log.i("Test",barcode.displayValue);
                    updateAttendance(user.get("uid"),barcode.displayValue);

                } else Log.i("Test",getResources().getString(R.string.no_barcode_captured));
            } else Log.e("Test", String.format(getString(R.string.barcode_error_format),
                    CommonStatusCodes.getStatusCodeString(resultCode)));
        } else super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateAttendance(final String uid, final String SID) {
        // Tag used to cancel the request
        String tag_string_req = "QR";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.UPDATE_ATTENDANCE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { //response
                Log.d("Test", "QR Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite

                        // Inserting row in users table
                        db.updateUser(jObj);

                        Toast.makeText(HomeScreen.this, "Your attendance have been updated", Toast.LENGTH_LONG).show();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(HomeScreen.this.getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Test", "Update Error: " + error.getMessage());
                Toast.makeText(HomeScreen.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() { //sending
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("SID",SID);
                params.put("uid",uid);
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
