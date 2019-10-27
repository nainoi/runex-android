package com.think.runex.java.Pages.RegisteredEvent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jozzee.android.core.utility.Logger;
import com.think.runex.R;
import com.think.runex.java.Models.MultiObject;
import com.think.runex.java.Utils.L;
import com.think.runex.java.ViewHolders.VHEmpty;
import com.think.runex.java.ViewHolders.VHEvent;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if( viewType == 0 ){
            return new VHEvent(LayoutInflater.from( parent.getContext() ).inflate(R.layout.list_item_event, parent, false));

        } else return new VHEmpty(LayoutInflater.from( parent.getContext() ).inflate(R.layout.list_item_event_divider, parent, false));

    }

    @Override
    public int getItemViewType(int position) {
        return events.get( position ).getLayoutTypeId();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
