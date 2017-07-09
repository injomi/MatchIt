package com.example.matchit.helper;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchit.R;
import com.example.matchit.model.eventItemModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ajimal on 7/2/2017.
 */

public class EventAdapter extends BaseAdapter {

    private Context context;
    private JSONArray data;
    private static LayoutInflater inflater=null;

    public EventAdapter(Context a, JSONArray data){
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
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder holder = new ViewHolder();
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

        } catch (JSONException e) {
            Log.i("Test","Error -> " + e.toString());
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO link to events details page
                Toast.makeText(context,"You clicked " + position,Toast.LENGTH_SHORT).show();
            }
        });
        return rowView;
    }

}
