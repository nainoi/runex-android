package com.think.runex.java.Pages.RegisteredEvent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.R;
import com.think.runex.java.Models.MultiObject;
import com.think.runex.java.ViewHolders.VHEvent;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<VHEvent> {
    /** Main variables */
    private final String ct = "EventAdapter->";

    // instance variables
    private List<MultiObject> events;

    public EventAdapter( List<MultiObject> events) {
        super();

        this.events = events;
    }

    @NonNull
    @Override
    public VHEvent onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from( parent.getContext() ).inflate(R.layout.list_item_event, null);

        return new VHEvent( v );
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull VHEvent holder, int position) {

    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
