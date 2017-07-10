package com.example.matchit.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.matchit.Availability;
import com.example.matchit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ajimal on 7/2/2017.
 */

public class AvailAdapter extends BaseAdapter {

    private Context context;
    private JSONArray data;
    private static LayoutInflater inflater=null;
    private Availability availFragment;

    public AvailAdapter(Context a, JSONArray data, Availability fragment){
        context = a;
        this.data = data;
        availFragment = fragment;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return data.length();
    }

    @Override
    public Object getItem(int position){
        return position;
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public static class ViewHolder{
        public TextView start;
        public TextView end;
        public ImageButton delete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        //Log.i("Test","i-> " + position);
        ViewHolder holder = new ViewHolder();
        View rowView;
        rowView = inflater.inflate(R.layout.availability_list,null);
        holder.start = (TextView)rowView.findViewById(R.id.avail_start);
        holder.end = (TextView)rowView.findViewById(R.id.avail_end);
        holder.delete = (ImageButton)rowView.findViewById(R.id.avail_del);
        //TODO set textviews of JObj
        try {
            JSONObject obj = data.getJSONObject(position);
            holder.start.setText(obj.getString("start"));
            holder.end.setText(obj.getString("end"));
            final int AVID = obj.getInt("AVID");
            holder.delete.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    //TODO delete
//                    Toast.makeText(context,"delete: " + AVID, Toast.LENGTH_SHORT).show();
                    availFragment.queryAvailService("removeAvailability", "",AVID,"","");
                }
            });

        } catch (JSONException e) {
            Log.i("Test","Error -> " + e.toString());
        }
        return rowView;
    }

}
