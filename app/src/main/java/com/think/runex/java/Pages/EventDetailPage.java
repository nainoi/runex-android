package com.think.runex.java.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Activity.xActivity;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.EventDetailObject;
import com.think.runex.java.Models.RunningHistoryObject;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetEventDetailService;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class EventDetailPage extends xActivity {
    /** Main variables */
    private final String ct = "EventDetailPage->";

    // instance variables

    // explicit variables
    private String mEventProfile = null;
    private String mEventName = null;
    private String mEventId = null;
    private boolean ON_NETWORKING = false;

    // views
    private TextView lbEventName;
    private ImageView eventImage;
    private TextView lbTotalDistance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_event_detail);

        final String mtn = ct +"onCreate() ";

        ActivityUtils.newInstance(this).fullScreen();

        Bundle b = getIntent().getExtras();
        mEventProfile = b.getString("EVENT_PROFILE");
        mEventName = b.getString("EVENT_NAME");
        mEventId = b.getString("EVENT_ID");

        // view matching
        viewMatching(  );

        try {
            // binding event name
            lbEventName.setText(mEventName);
            Picasso.get().load( mEventProfile ).into( eventImage );

        } catch ( Exception e ){
            L.e(mtn +"Err: "+ e.getMessage());
        }
    }

    /** Feature methods */
    private void binding(){
//        lbTotalDistance.setText();
    }

    // view matching
    private void viewMatching(){
        eventImage = findViewById(R.id.event_image);
        lbEventName = findViewById(R.id.lb_event_name);
        lbTotalDistance = findViewById(R.id.lb_total_running_distance);

    }

    /** API methods */
    private void apiGetEventDetail(String eventId){
        // prepare usage variables
        final String mtn = ct +"apiGetEventDetail() ";

        // fire
        new GetEventDetailService(this, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn +"response: "+ response.jsonString);

                try {
                    // convert to running history object
                    EventDetailObject rhis = Globals.GSON.fromJson(response.jsonString, EventDetailObject.class);
                    EventDetailObject.DataBean db = rhis.getData();

                    // update total distance
                    lbTotalDistance.setText(Globals.DCM.format(db.getTotal_distance()) +" km");

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
