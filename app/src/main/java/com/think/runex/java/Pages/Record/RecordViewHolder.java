package com.think.runex.java.Pages.Record;

import android.content.pm.ActivityInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.think.runex.R;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Models.ActivityInfoBean;

public class RecordViewHolder extends RecyclerView.ViewHolder {

    private RelativeLayout rootView;
    private TextView lbDistance;
    private TextView lbDate;
    private TextView lbCaption;
    private View btnDel;
    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
        }
    };

    public RecordViewHolder(@NonNull View itemView) {
        super(itemView);

        rootView = itemView.findViewById(R.id.list_item);
        lbDistance = itemView.findViewById(R.id.lb_description);
        lbDate = itemView.findViewById(R.id.lb_date);
        lbCaption = itemView.findViewById(R.id.lb_caption);
        btnDel = itemView.findViewById(R.id.btn_del);
    }

    public static RecordViewHolder create(ViewGroup parent) {
        return new RecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_record, parent, false));
    }

    public void bind(ActivityInfoBean data) {
        String distance = Globals.DCM_2.format(data.getDistance()) + " " + lbDistance.getContext().getString(R.string.km);

        lbDistance.setText(distance);
        lbDate.setText(data.getActivityDate());
        lbCaption.setText(data.getCaption());

        btnDel.setOnClickListener( listener );

    }

}
