package com.think.runex.java.Pages.Record;

import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.R;
import com.think.runex.java.Models.ActivityInfoBean;

public class RecordViewHolder extends RecyclerView.ViewHolder {

    private RelativeLayout rootView;
    private TextView lbDistance;
    private TextView lbDate;
    private TextView lbCaption;

    public RecordViewHolder(@NonNull View itemView) {
        super(itemView);

        rootView = itemView.findViewById(R.id.list_item);
        lbDistance = itemView.findViewById(R.id.lb_distance);
        lbDate = itemView.findViewById(R.id.lb_date);
        lbCaption = itemView.findViewById(R.id.lb_caption);
    }

    public static RecordViewHolder create(ViewGroup parent) {
        return new RecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_record, parent, false));
    }

    public void bind(ActivityInfoBean data) {
        String distance = data.getDistance() + " " + lbDistance.getContext().getString(R.string.km);
        lbDistance.setText(distance);
        lbDate.setText(data.getActivityDate());
        lbCaption.setText(data.getCaption());
    }

}
