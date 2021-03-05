package com.think.runex.java.Pages.RegisteredEvent;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.R;
import com.think.runex.feature.event.data.EventRegistered;
import com.think.runex.java.Pages.OnItemClickListener;
import com.think.runex.java.ViewHolders.VHEvent;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * Main variables
     */
    private final String ct = "EventAdapter->";

    // instance variables
    private OnItemClickListener mListener;
    private List<EventRegistered> events;

    public EventAdapter(List<EventRegistered> events, OnItemClickListener listener) {
        super();

        this.events = events;
        this.mListener = listener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VHEvent(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event_java, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            VHEvent vh = ((VHEvent) holder);
            vh.bind(events.get(position), mListener);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
