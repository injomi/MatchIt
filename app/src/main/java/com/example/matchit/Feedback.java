package com.example.matchit;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matchit.app.AppConfig;
import com.example.matchit.app.AppController;
import com.example.matchit.helper.RVAdapter;
import com.example.matchit.model.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Feedback extends Fragment implements  View.OnClickListener{

    View myView;
    DatabaseReference db;
    ImageView sad;
    ImageView neutral;
    ImageView happy;
    Button submit;
    int rating = 0;
    int EID;
    EditText comment;
    String UID;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.feedback_form, container, false);

        sad = (ImageView)myView.findViewById(R.id.imageButton3);
        sad.setOnClickListener(this);
        neutral = (ImageView)myView.findViewById(R.id.imageButton2);
        neutral.setOnClickListener(this);
        happy = (ImageView)myView.findViewById(R.id.imageButton4);
        happy.setOnClickListener(this);
        submit = (Button)myView.findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(this);
        comment = (EditText) myView.findViewById(R.id.textView9);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            EID = bundle.getInt(EventDetails.EVENT_ID_KEY, 0);
            UID = ((HomeScreen)this.getActivity()).user.get("uid");
            Log.i("Test","fragment argument of eventID -> " + EID + ", UID -> " + UID + "//");
        }

        //return super.onCreateView(inflater, container, savedInstanceState);
        return myView;
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageButton3: //sad
                sad.setAlpha(1f);
                happy.setAlpha(.4f);
                neutral.setAlpha(.4f);
                rating = 1;
                break;
            case R.id.imageButton2: //neutral
                neutral.setAlpha(1f);
                happy.setAlpha(.4f);
                sad.setAlpha(.4f);
                rating = 2;
                break;
            case R.id.imageButton4: //happy
                happy.setAlpha(1f);
                sad.setAlpha(.4f);
                neutral.setAlpha(.4f);
                rating = 3;
                break;
            case R.id.buttonSubmit:
                if(rating == 0){
                    Toast.makeText(getActivity(), "Please select your feedback", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateFeedback();

                break;
        }
    }

    private void updateFeedback() {
        // Tag used to cancel the request
        String tag_string_req = "feedback";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.UPDATE_FEEDBACK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) { //response
                Log.d("Test", "Feedback Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getActivity(), "Thank you for your feedback!", Toast.LENGTH_LONG).show();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity(),
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
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() { //sending
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("EID",String.valueOf(EID));
                params.put("uid",UID);
                params.put("feedback",comment.getText().toString());
                params.put("rating",String.valueOf(rating));
                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
