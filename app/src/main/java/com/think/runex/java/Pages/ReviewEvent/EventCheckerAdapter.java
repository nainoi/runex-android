package com.think.runex.java.Pages.ReviewEvent;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.feature.event.model.registered.RegisteredEvent;
import com.think.runex.java.Models.EventIdAndPartnerObject;

import java.util.ArrayList;
import java.util.List;

public class EventCheckerAdapter extends RecyclerView.Adapter<EventCheckerViewHolder> implements
        EventCheckerViewHolder.OnCheckedChangeListener {

    private List<RegisteredEvent> list;

    @NonNull
    @Override
    public EventCheckerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return EventCheckerViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull EventCheckerViewHolder holder, int position) {
        holder.bind(list.get(position), this);
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    void submitList(List<RegisteredEvent> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public void onCheckedChanged(int position, boolean isChecked) {
        if (list != null) {
            RegisteredEvent event = list.get(position);
            if (event != null) {
                event.setChecked(isChecked);
                list.set(position, event);
                notifyItemChanged(position);
            }
        }
    }

    public List<EventIdAndPartnerObject> getSelectedEvents() {
        if (list == null || list.size() == 0) return new ArrayList<>();

        ArrayList<EventIdAndPartnerObject> selectedList = new ArrayList<>();
        for (RegisteredEvent event : list) {
            if (event.isChecked()) {
                EventIdAndPartnerObject eventIdAndPartner = new EventIdAndPartnerObject();
                eventIdAndPartner.setEvent_id(event.getEventId());
                if (event.getEventInfo() != null) {
                    eventIdAndPartner.setPartner(event.getEventInfo().getPartner());
                }
                selectedList.add(eventIdAndPartner);
            }
        }

        return selectedList;
    }
}
