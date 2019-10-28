package com.think.runex.java.Pages.RegisteredEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.think.runex.R;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.xFragment;
import com.think.runex.java.Models.EventObject;
import com.think.runex.java.Models.MultiObject;
import com.think.runex.java.Utils.DateTime.DateTimeUtils;
import com.think.runex.java.Utils.DateTime.DisplayDateTimeObject;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.util.ArrayList;
import java.util.List;

import static com.think.runex.java.Constants.Globals.GSON;

public class RegisteredEventsPage extends xFragment implements
        onNetworkCallback,
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    /**
     * Main variables
     */
    private final String ct = "RegisteredEventsPage->";

    // instance variables
    private EventAdapter eventAdapter;
    private List<MultiObject> events = new ArrayList<MultiObject>() {{
//        add( new MultiObject());
//        add( new MultiObject().setLayoutTypeId(1));
//        add( new MultiObject());
//        add( new MultiObject().setLayoutTypeId(1));
//        add( new MultiObject());
//        add( new MultiObject().setLayoutTypeId(1));
//        add( new MultiObject());
//        add( new MultiObject().setLayoutTypeId(1));
//        add( new MultiObject());
//        add( new MultiObject().setLayoutTypeId(1));
//        add( new MultiObject());
//        add( new MultiObject().setLayoutTypeId(1));
//        add( new MultiObject());
//        add( new MultiObject().setLayoutTypeId(1));
//        add( new MultiObject());
//        add( new MultiObject().setLayoutTypeId(1));
//        add( new MultiObject());
//        add( new MultiObject().setLayoutTypeId(1));
//        add( new MultiObject());
//        add( new MultiObject().setLayoutTypeId(1));

    }};

    // explicit variables
    private boolean ON_LOADING = false;
    private boolean ON_REFRESH = false;
    // views
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    /**
     * Implement methods
     */
    @Override
    public void onClick(View view) {
        // prepare usage variables
        final String mtn = ct +"onClick() ";

        if( ON_REFRESH ) {
            // log
            L.i(mtn +"on refresh...");

            // exit from this process
            return;
        }



    }
    @Override
    public void onSuccess(String jsonString) {
        // prepare usage variables
        final String mtn = ct + "onSuccess() ";

        try {

            // logs
            L.i(mtn + "JSONResult: " + jsonString);

            // prepare usage variables
            final EventObject rsp = GSON.fromJson(jsonString, EventObject.class);
            final List<MultiObject> mlList = new ArrayList<>();

            L.i(mtn + "");
            L.i(mtn + "");
            L.i(mtn + "* * * Event Amount(" + rsp.getData().size() + ") * * *");
            for (int a = 0; a < rsp.getData().size(); a++) {
                // prepare usage variables
                EventObject.DataBean evt = rsp.getData().get(a);
                EventObject.DataBean.EventBean evtVal = evt.getEvent();
                MultiObject ml = new MultiObject();
                DisplayDateTimeObject start = DateTimeUtils.instance().stringToDate(evtVal.getStart_reg());
                DisplayDateTimeObject end = DateTimeUtils.instance().stringToDate(evtVal.getEnd_reg());

                // update props
                //--> evt val
                evtVal.setCustomRegDuration(start.Day + " " + start.shortMonth
                        + " - " + end.Day + " " + end.shortMonth + " " + end.year);
                //--> multi object
                ml.setAttachedObject(evt);
                ml.setLayoutTypeId(0);

                // keep object
                mlList.add(ml);
                // divider
                mlList.add(new MultiObject().setLayoutTypeId(1));

                // logs
                L.i(mtn + "Name[" + a + "]: " + evt.getEvent().getName());

            }
            L.i(mtn + "");


            // on refresh
            if( ON_REFRESH ) {
                // clear all item
                events.clear();

            }

            // add all items
            events.addAll( mlList );

            // notify data has changed
            if( ON_REFRESH ){
                // clear flag
                this.ON_REFRESH = false;

                // notify data has change
                eventAdapter.notifyDataSetChanged();


                // notify insert item
            } else eventAdapter.notifyItemRangeInserted(0, rsp.getData().size());

            // hide progress dialog
            refreshLayout.setRefreshing( false );

        } catch (Exception e) {
            L.e(mtn + "Err: " + e);

        }
    }

    @Override
    public void onFailure(Exception jsonString) {
        Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRefresh() {
        // update flag
        ON_REFRESH = true;

        // call api
        apiGetEvents();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_registered_events, container, false);

        // init
        eventAdapter = new EventAdapter(events);

        // matching view
        recyclerView = v.findViewById(R.id.recycler_view);
        refreshLayout = v.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);

        // recycler view props
        recyclerViewProps();

        // call api
        apiGetEvents();

        return v;
    }

    /**
     * API methods
     */
    private void apiGetEvents() {
        // prepare usage variables
        final String mtn = ct +"apiGetEvents() ";
        final NetworkUtils nw = NetworkUtils.newInstance(getActivity());
        final NetworkProps props = new NetworkProps();

        // log
        L.i(mtn +"api get events...");

        // update props
        props.addHeader("Authorization", "Bearer " + Globals.TOKEN);
        props.setUrl(APIs.GET_REGISTERED_EVENT.VAL);

        // call api
        nw.get(props, this);
    }

    /**
     * Recycler view props
     */
    private void recyclerViewProps() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(eventAdapter);
    }
}
