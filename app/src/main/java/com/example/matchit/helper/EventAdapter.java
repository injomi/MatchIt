package com.example.matchit.helper;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchit.EventDetails;
import com.example.matchit.Feedback;
import com.example.matchit.HomeScreen;
import com.example.matchit.R;
import com.example.matchit.model.eventItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Adapter for my events list view
 */

public class EventAdapter extends BaseAdapter {

    private Context context;
    private JSONArray data;
    private static LayoutInflater inflater=null;
    public String type;

    public EventAdapter(Context a, JSONArray data, String type){
        this.type = type;
        context = a;
        this.data = data;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return data.length();
    }

    @Override
    public Object getItem(int postion){
        return postion;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public static class ViewHolder{
        public TextView name;
        public TextView date;
        public TextView time;
        public TextView status;
        public int eventID;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder holder = new ViewHolder();
        View rowView;
        rowView = inflater.inflate(R.layout.myevents_list,null);
        holder.date = (TextView)rowView.findViewById(R.id.myEvent_date);
        holder.name = (TextView)rowView.findViewById(R.id.myEvent_name);
        holder.status = (TextView)rowView.findViewById(R.id.myEvent_status);
        holder.time = (TextView)rowView.findViewById(R.id.myEvent_time);
        //TODO set textviews of JObj
        try {
            JSONObject obj = data.getJSONObject(position);
            holder.date.setText(obj.getString("event_date"));
            holder.name.setText(obj.getString("event_name"));
            holder.status.setText(obj.getString("status"));
            holder.time.setText(obj.getString("event_startTime"));
            holder.eventID = obj.getInt("EID");

        } catch (JSONException e) {
            Log.i("Test","Error -> " + e.toString());
        }
        final FragmentManager fragmentManager = ((HomeScreen)context).getFragmentManager();
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO link to events details page
                Log.i("Test","EID: " + "(" + holder.eventID + ")" + " clicked");
                Bundle bundle = new Bundle();
                bundle.putInt(EventDetails.EVENT_ID_KEY, holder.eventID);
                if(type.equals("past")){
                    //..
                    Feedback feedback = new Feedback();
                    feedback.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , feedback)
                            .commit();
                }
                else if(type.equals("upcoming")){
                    EventDetails eventDetails = new EventDetails();
                    eventDetails.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , eventDetails)
                            .commit();
                }

            }
        });
        return rowView;
    }

}
