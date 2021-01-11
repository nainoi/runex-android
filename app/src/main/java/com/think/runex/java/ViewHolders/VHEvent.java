package com.think.runex.java.ViewHolders;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.feature.event.model.EventRegistered;
import com.think.runex.feature.payment.PaymentStatus;
import com.think.runex.java.Pages.OnItemClickListener;

public class VHEvent extends RecyclerView.ViewHolder {
    // views
    private TextView lbEventName;
    private TextView lbEventType;
    private TextView lbEventBill;
    private TextView lbDistance;
    private TextView lbStartReg;
    private ImageView imgCover;

    public VHEvent(@NonNull View v) {
        super(v);

        lbEventName = v.findViewById(R.id.lb_event_name);
        lbEventType = v.findViewById(R.id.lb_event_type);
        lbEventBill = v.findViewById(R.id.lb_event_bill);
        lbDistance = v.findViewById(R.id.lb_distance);
        lbStartReg = v.findViewById(R.id.lb_begin_registration);
        imgCover = v.findViewById(R.id.view_cover);
    }

    public static VHEvent create(ViewGroup parent) {
        return new VHEvent(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event_java, parent, false));
    }

    public void bind(EventRegistered data, OnItemClickListener listener) {
        // prepare usage variables
        //EventObject.DataBean evt = (EventObject.DataBean)ml.getAttachedObject();
        //EventObject.DataBean.EventBean evtVal = evt.getEvent();
        //Payment payment =  evtVal.getCustomPaymentColor();

        // binding
        lbEventName.setText(data.getName());
        lbEventType.setText(data.getCategory());
        lbStartReg.setText(data.registerEventPeriod(itemView.getContext()));
        //--> image
        Picasso.get().load(data.getCoverImage()).into(imgCover);

        //--> billing
        lbEventBill.setText(PaymentStatus.INSTANCE.getPaymentStatusText(itemView.getContext(), data.getStatus()));
        //lbEventBill.setTextColor(ContextCompat.getColor(itemView.getContext(), PaymentStatus.INSTANCE.getPaymentStatusColor(data.getPaymentStatus())));

        //--> event
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // exit from this process
                //if (!data.getPaymentStatus().equals(PaymentStatus.SUCCESS)) return;

                // exit from this process
                if (listener == null) return;

                // event listener
                listener.onItemClicked(getAdapterPosition());

            }
        });
    }
}
