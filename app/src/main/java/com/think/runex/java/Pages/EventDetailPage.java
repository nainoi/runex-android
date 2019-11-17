package com.think.runex.java.Pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Constants.xAction;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.Fragment.xFragmentHandler;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Models.EventDetailObject;
import com.think.runex.java.Models.EventObject;
import com.think.runex.java.Models.ActivityInfoBean;
import com.think.runex.java.Pages.Record.EventRecordHistoryPage;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.ChildFragmentUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetEventDetailService;
import com.think.runex.java.Utils.Network.onNetworkCallback;


import java.util.ArrayList;

public class EventDetailPage extends xFragment implements View.OnClickListener {

    /**
     * Main variables
     */
    private final String ct = "EventDetailPage->";

    // instance variables
    private EventObject.DataBean mEvent;

    // explicit variables
    private String mEventProfile = null;
    private String mEventName = null;
    private String mEventId = null;
    private boolean ON_NETWORKING = false;
    private ArrayList<ActivityInfoBean> activityRecordList;

    // views
    private TextView lbEventName;
    private ImageView eventImage;
    private TextView lbTotalDistance;
    private AppCompatImageButton btnAddActivity;
    private AppCompatImageButton btnHistory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_event_detail, container, false);
        final String mtn = ct + "onCreate() ";

        // init
        ActivityUtils.newInstance(activity).fullScreen();

        // prepare usage variables
        EventObject.DataBean.EventBean evtVal = mEvent.getEvent();

        // update props
        mEventProfile = APIs.DOMAIN.VAL + evtVal.getCover();
        mEventName = evtVal.getName();
        mEventId = mEvent.getEvent_id();

        // view matching
        viewMatching(v);

        // view event listener
        viewEventListener();

        try {
            // binding event name
            lbEventName.setText(mEventName);
            Picasso.get().load(mEventProfile).into(eventImage);

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }

        return v;
    }

    /**
     * Feature methods
     */
    private void binding() {
//        lbTotalDistance.setText();
    }

    // view matching
    private void viewMatching(View v) {
        eventImage = v.findViewById(R.id.event_image);
        lbEventName = v.findViewById(R.id.lb_event_name);
        lbTotalDistance = v.findViewById(R.id.lb_total_running_distance);
        btnAddActivity = v.findViewById(R.id.btn_add_activity);
        btnHistory = v.findViewById(R.id.btn_history);
    }

    // view event listener
    private void viewEventListener() {
        btnAddActivity.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
    }

    /**
     * API methods
     */
    private void apiGetEventDetail(String eventId) {
        // prepare usage variables
        final String mtn = ct + "apiGetEventDetail() ";

        // fire
        new GetEventDetailService(activity, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn + "response: " + response.jsonString);

                try {
                    // convert to running history object
                    EventDetailObject rhis = Globals.GSON.fromJson(response.jsonString, EventDetailObject.class);
                    EventDetailObject.DataBean db = rhis.getData();

                    // update total distance
                    lbTotalDistance.setText(Globals.DCM.format(db.getTotal_distance()) + " km");

                    //Keep activity record list for EventRecordHistoryPage
                    activityRecordList = db.getActivity_info();

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());

                }

            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn + "err-response: " + response.jsonString);

            }
        }).doIt(eventId);
    }

    /**
     * Setter
     */
    public xFragment setEvent(EventObject.DataBean event) {
        mEvent = event;

        return this;
    }

    /**
     * Life cycle
     */
    @Override
    public void onResume() {
        super.onResume();
        // prepare usage variables
        final String mtn = ct + "onResume() ";

        if (ON_NETWORKING) return;
        if (mEventId == null) {
            L.e(mtn + "event id[" + mEventId + "] is not ready.");

            // exit from this process
            return;
        }

        // get event detail
        apiGetEventDetail(mEventId);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add_activity) {
            // prepare usage variables
            AddEventPage page = new AddEventPage();

            // update props
            page.setEventId(mEventId);
            page.setFragmentHandler(new xFragmentHandler() {
                @Override
                public xFragment onResult(xTalk talk) {

                    if (talk.resultCode == xAction.SUCCESS.ID) {
                        // get event detail
                        apiGetEventDetail(mEventId);

                    }

                    return null;
                }
            });

            // display add event activity
            ChildFragmentUtils.newInstance(this).addChildFragment
                    (R.id.display_fragment_frame,
                            page);

        } else if (v.getId() == R.id.btn_history) {
            recordPage();
        }
    }


    private void recordPage() {
        if (activityRecordList == null || activityRecordList.size() == 0) {
            Toast.makeText(getContext(), R.string.activity_record_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // prepare usage variables
        EventRecordHistoryPage page = new EventRecordHistoryPage();

        // update props
        page.setRecordList(activityRecordList);

        // go to record page
        ChildFragmentUtils.newInstance( this ).addChildFragment(R.id.display_fragment_frame, page);
    }
}
