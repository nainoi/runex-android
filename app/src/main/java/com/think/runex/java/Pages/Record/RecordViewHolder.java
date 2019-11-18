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
import com.think.runex.java.Pages.onItemClick;
import com.think.runex.java.Utils.L;

import java.util.Date;

public class RecordViewHolder extends RecyclerView.ViewHolder {
    private String ct = "RecordViewHolder->";
    private RelativeLayout rootView;
    private TextView lbDistance;
    private TextView lbDate;
    private TextView lbCaption;
    private View btnDel;

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

    public void bind(ActivityInfoBean data, boolean needEditor, onItemClick onItemClick) {
        String distance = Globals.DCM_2.format(data.getDistance()) + " " + lbDistance.getContext().getString(R.string.km);
        String histDate = "-";
        String mtn = ct +"bind() ";

        try{
            Date d = Globals.SDF_TOKEN.parse( data.getActivity_date() );
            histDate = Globals.SDF_DISPLAY_FULL_DATE_TIME.format( d );

        }catch (Exception e ){
            L.e(mtn +"Err: "+ e.getMessage());
        }

        lbDistance.setText(distance );
        lbDate.setText( histDate );
        lbCaption.setText(data.getCaption());

        if( needEditor ) {
            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClick != null) {
                        // callback
                        onItemClick.onItemClicked(getAdapterPosition());
                    }
                }
            });

        } else btnDel.setVisibility(View.GONE);

    }

}
