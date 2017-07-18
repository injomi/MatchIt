package com.example.matchit.helper;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matchit.EventDetails;
import com.example.matchit.HomeScreen;
import com.example.matchit.R;
import com.example.matchit.model.Event;
import com.example.matchit.model.Session;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by Ajimal on 6/15/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView event_desc;
        TextView event_name;
        ProgressBar event_pb;
        TextView event_sd;
        int eventID;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            event_desc = (TextView) itemView.findViewById(R.id.event_desc);
            event_name = (TextView) itemView.findViewById(R.id.event_name);
            event_pb = (ProgressBar) itemView.findViewById(R.id.event_pb);
            event_sd = (TextView) itemView.findViewById(R.id.event_sd);
            final FragmentManager fragmentManager = ((HomeScreen) itemView.getContext()).getFragmentManager();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Test", event_name.getText().toString() + "(" + eventID + ")" + " clicked");
                    Bundle bundle = new Bundle();
                    bundle.putInt(EventDetails.EVENT_ID_KEY, eventID);
                    EventDetails eventDetails = new EventDetails();
                    eventDetails.setArguments(bundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame
                                    , eventDetails)
                            .commit();
                }
            });
        }
    }

    List<Event> events;
    List<Event> eventsCopy;
    Activity activity;

    public RVAdapter(List<Event> events, Activity a) {
        this.events = events;
        eventsCopy = new ArrayList(events);
        activity = a;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_event, viewGroup, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        String name, description, startDate;
        Event event = events.get(i);
        if (event != null) {
            name = event.getName();
            description = event.getDescription();
            startDate = event.getStartDate();
            int max = 0;
            int count = 0;
            for (Session s : event.getSessions().values()) {
                max += s.getVolunteerMax();
                count += s.getVolunteerNo();
            }
            eventViewHolder.event_pb.setMax(max);
            eventViewHolder.event_pb.setProgress(count);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                eventViewHolder.event_pb.setProgress(count,true);
//            else
//                eventViewHolder.event_pb.setProgress(count);
            eventViewHolder.event_desc.setText(description);
            eventViewHolder.event_name.setText(name);
            eventViewHolder.event_sd.setText(startDate);
            eventViewHolder.eventID = event.getEventID();
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void filter(String text) {
        events.clear();
        if (text.isEmpty()) {
            events.addAll(eventsCopy);
        } else {
            text = text.toLowerCase();
            for (Event item : eventsCopy) {
                if (item.getName().toLowerCase().contains(text) || item.getCategory().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text) || item.getHostName().toLowerCase().contains(text)
                        || item.getLocation().toLowerCase().contains(text) || item.getStartDate().toLowerCase().contains(text)
                        || item.getEndDate().toLowerCase().contains(text)) {
                    events.add(item);
                }
            }
        }
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RVAdapter.this.notifyDataSetChanged();
                    if(events.isEmpty()) {
                        final Toast toast = Toast.makeText(activity, "No Event Found!", Toast.LENGTH_SHORT);
                        toast.show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast.cancel();
                            }
                        }, 1000);
                    }
                }
            });
        } catch (NullPointerException e) {
            Log.i("Test", "ignorable error");
        }
    }

}
