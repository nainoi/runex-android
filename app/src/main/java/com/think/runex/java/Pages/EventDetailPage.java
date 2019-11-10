package com.think.runex.java.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.think.runex.R;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.RunningHistoryObject;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetEventDetailService;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class EventDetailPage extends xFragment {
    /** Main variables */
    private final String ct = "EventDetailPage->";

    // instance variables

    // explicit variables
    private String mEventId = null;
    private boolean ON_NETWORKING = false;

    // views
    private TextView lbTotalDistance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_event_detail, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // view matching
        viewMatching( view );
    }

    /** Feature methods */
    private void binding(){
//        lbTotalDistance.setText();
    }

    /** Setter */
    public xFragment setEventId(String eventId){
        this.mEventId = eventId;
        return this;
    }

    // view matching
    private void viewMatching(View v){
        lbTotalDistance = v.findViewById(R.id.lb_total_running_distance);

    }

    /** API methods */
    private void apiGetEventDetail(String eventId){
        // prepare usage variables
        final String mtn = ct +"apiGetEventDetail() ";

        // fire
        new GetEventDetailService(activity, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn +"response: "+ response.jsonString);

                try {
                    // convert to running history object
                    RunningHistoryObject rhis = Globals.GSON.fromJson(response.jsonString, RunningHistoryObject.class);
                    RunningHistoryObject.DataBean db = rhis.getData().get(0);

                    // update total distance
                    lbTotalDistance.setText(Globals.DCM.format(db.getTotal_distance()));

                } catch ( Exception e ){
                    L.e(mtn +"Err: "+ e.getMessage());

                }

            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn +"err-response: "+ response.jsonString);

            }
        }).doIt( eventId );
    }

    /** Life cycle */
    @Override
    public void onResume() {
        super.onResume();
        // prepare usage variables
        final String mtn = ct +"onResume() ";

        if( ON_NETWORKING ) return;
        if( mEventId == null ){
            L.e(mtn +"event id["+ mEventId +"] is not ready.");

            // exit from this process
            return;
        }

        // get event detail
        apiGetEventDetail(mEventId);

    }
}
