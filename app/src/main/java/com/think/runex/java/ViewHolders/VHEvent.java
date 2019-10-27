package com.think.runex.java.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Models.EventObject;
import com.think.runex.java.Models.MultiObject;

public class VHEvent extends RecyclerView.ViewHolder {
    // views
    private TextView lbEventName;
    private TextView lbEventType;
    private TextView lbDistance;
    private TextView lbStartReg;
    private ImageView imgCover;

    public VHEvent(@NonNull View v) {
        super(v);

        lbEventName = v.findViewById(R.id.lb_event_name);
        lbEventType = v.findViewById(R.id.lb_event_type);
        lbDistance = v.findViewById(R.id.lb_distance);
        lbStartReg = v.findViewById(R.id.lb_begin_registration);
        imgCover = v.findViewById(R.id.view_cover);
    }

    public void bind(MultiObject ml){
        // prepare usage variables
        EventObject.DataBean evt = (EventObject.DataBean)ml.getAttachedObject();
        EventObject.DataBean.EventBean evtVal = evt.getEvent();

        // binding
        lbEventName.setText( evtVal.getName() );
        lbEventType.setText( evtVal.getCategory().getName() );
        lbStartReg.setText(evtVal.getStart_reg());
        Picasso.get().load(APIs.DOMAIN.VAL + evtVal.getCover() ).into( imgCover );
    }
}
