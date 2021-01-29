package com.think.runex.java.Pages.Record;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.R;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Models.WorkoutInfo;
import com.think.runex.java.Pages.OnItemClickListener;

import java.util.Calendar;

public class RecordViewHolder extends RecyclerView.ViewHolder {
    private String ct = "RecordViewHolder->";
    private RelativeLayout rootView;
    private TextView lbDistance;
    private TextView lbDate;
    private TextView lbCaption;
    private View btnDel;
    private Calendar calendar = Calendar.getInstance();

    public RecordViewHolder(@NonNull View itemView) {
        super(itemView);

        rootView = itemView.findViewById(R.id.list_item);
        lbDistance = itemView.findViewById(R.id.lb_description);
        lbDate = itemView.findViewById(R.id.lb_date);
        lbCaption = itemView.findViewById(R.id.lb_caption);
        btnDel = itemView.findViewById(R.id.btn_del);
    }

    public static RecordViewHolder create(ViewGroup parent) {
        return new RecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_record_java, parent, false));
    }

    public void bind(WorkoutInfo data,
                     OnItemClickListener onItemClick,
                     boolean canDelete,
                     WorkoutsAdapter.OnDeleteRecordListener onDeleteRecord) {
        String distance = Globals.DCM_2.format(data.getDistance()) + " " + lbDistance.getContext().getString(R.string.km);
        String mtn = ct + "bind() ";

        lbDistance.setText(distance);
        lbDate.setText(data.getWorkoutDate());
        lbCaption.setText(data.getCaption());

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick != null) {
                    onItemClick.onItemClicked(getAdapterPosition());
                }
            }
        });

        if (canDelete) {
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onDeleteRecord != null) {
                        // callback
                        onDeleteRecord.onDeleteRecord(getAdapterPosition());
                    }
                }
            });
        } else {
            btnDel.setVisibility(View.GONE);
        }

    }

}
