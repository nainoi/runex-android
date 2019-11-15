package com.think.runex.java.Pages.Record;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.java.Models.ActivityInfoBean;
import com.think.runex.java.Models.RegEventsObject;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder> {

    private List<ActivityInfoBean> list;

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RecordViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list == null) return 0;
        return list.size();
    }

    void submitList(List<ActivityInfoBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
