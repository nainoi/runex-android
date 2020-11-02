package com.think.runex.java.Pages.ReviewEvent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.think.runex.R;
import com.think.runex.java.Models.ActiveRegisteredEventObject;

public class EventCheckerViewHolder extends RecyclerView.ViewHolder {

    private OnCheckedChangeListener checkedChangeListener;
    private RelativeLayout rootView;
    private AppCompatImageView icon;
    private MaterialTextView title;


    public EventCheckerViewHolder(@NonNull View itemView) {
        super(itemView);
        rootView = itemView.findViewById(R.id.list_item);
        icon = itemView.findViewById(R.id.ic_checked);
        title = itemView.findViewById(R.id.tv_title);
    }

    public static EventCheckerViewHolder create(@NonNull ViewGroup parent) {
        return new EventCheckerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event_send_distance, parent, false));
    }

    public void bind(ActiveRegisteredEventObject.DataBean data, OnCheckedChangeListener checkedChangeListener) {
        if (checkedChangeListener != null) {
            this.checkedChangeListener = checkedChangeListener;
        }

        if (data.isChecked()) {
            icon.setImageResource(R.mipmap.ic_checkbox_checked);
        } else {
            icon.setImageResource(R.mipmap.ic_checkbox);
        }

        title.setText(data.getName());

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedChangeListener != null) {
                    checkedChangeListener.onCheckedChanged(getAdapterPosition(), !data.isChecked());
                }
            }
        });
    }

    public OnCheckedChangeListener getCheckedChangeListener() {
        return checkedChangeListener;
    }

    public void setCheckedChangeListener(OnCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
    }

    public interface OnCheckedChangeListener {
        public void onCheckedChanged(int position, boolean isChecked);
    }
}
