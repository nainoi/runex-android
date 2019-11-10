package com.think.runex.java.Pages.ReviewEvent;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.java.Models.RegEventsObject;

import java.util.ArrayList;
import java.util.List;

public class EventCheckerAdapter extends RecyclerView.Adapter<EventCheckerViewHolder> implements
        EventCheckerViewHolder.OnCheckedChangeListener {

    private List<RegEventsObject.EventChecker> list;

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

    void submitList(List<RegEventsObject.EventChecker> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public void onCheckedChanged(int position, boolean isChecked) {
        if (list != null) {
            RegEventsObject.EventChecker eventChecker = list.get(position);
            if (eventChecker != null) {
                eventChecker.setChecked(isChecked);
                list.set(position, eventChecker);
                notifyItemChanged(position);
            }
        }
    }

    public String[] getSelectedEvents() {
        if(list == null || list.size() == 0) return new  String[0];

        ArrayList<String> selectedList = new ArrayList<String>();
        for (RegEventsObject.EventChecker event : list) {
            if (event.isChecked()) {
                selectedList.add(event.getId());
            }
        }

        String[] selectedEvents = new String[selectedList.size()];
        for (int i = 0; i < selectedList.size(); i++) {
            selectedEvents[i] = selectedList.get(i);
        }

        return selectedEvents;
    }
}
