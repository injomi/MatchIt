package com.example.matchit.helper;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.matchit.R;
import com.example.matchit.model.Event;

import java.util.List;

/**
 * Created by Ajimal on 6/15/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.EventViewHolder> {
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView par_label;
        TextView event_name;

        EventViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            par_label = (TextView)itemView.findViewById(R.id.par_label);
            event_name = (TextView)itemView.findViewById(R.id.event_name);
        }
    }

    List<Event> events;

    public RVAdapter(List<Event> events){
        this.events = events;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_event, viewGroup, false);
        EventViewHolder evh = new EventViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        String name = "";
        String part = "Participant: ";
        Event event = events.get(i);
        if(event!=null){
            name = event.getName();
            if(event.getSessions().size()>0){
                part += String.valueOf(event.getSessions().get(0).getVolunteerNo());
            }
        }
        eventViewHolder.par_label.setText(part);
        eventViewHolder.event_name.setText(name);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}
