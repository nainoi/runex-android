package com.think.runex.java.Pages.RegisteredEvent;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.R;
import com.think.runex.feature.event.model.registered.RegisteredEvent;
import com.think.runex.feature.event.model.registered.RegisteredEventInfo;
import com.think.runex.java.Pages.onItemClick;
import com.think.runex.java.ViewHolders.VHEvent;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * Main variables
     */
    private final String ct = "EventAdapter->";

    // instance variables
    private onItemClick mListener;
    private List<RegisteredEvent> events;

    public EventAdapter(List<RegisteredEvent> events, onItemClick listener) {
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

            if (events.get(position) != null && events.get(position).getRegisterInfoList() != null && events.get(position).getRegisterInfoList().size() > 0) {
                RegisteredEventInfo data = events.get(position).getRegisterInfoList().get(0);
                vh.bind(data, mListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
