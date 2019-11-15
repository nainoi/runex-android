package com.think.runex.java.Pages.RegisteredEvent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.EventObject;
import com.think.runex.java.Models.MultiObject;
import com.think.runex.java.Pages.EventDetailPage;
import com.think.runex.java.Pages.onItemClick;
import com.think.runex.java.Utils.ChildFragmentUtils;
import com.think.runex.java.Utils.DateTime.DateTimeUtils;
import com.think.runex.java.Utils.DateTime.DisplayDateTimeObject;
import com.think.runex.java.Utils.FragmentUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetRegisteredEventService;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.net.HttpURLConnection;
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
    private ChildFragmentUtils mChildFragmentUtils;
    private EventAdapter eventAdapter;
    private List<MultiObject> events = new ArrayList<>();

    // explicit variables
    private boolean ON_LOADING = false;
    private boolean ON_REFRESH = false;
    // views
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private View frameNeedAuth;
    private ImageView imgNeedAuth;
    //--> frame no registered event
    private View btnSeeAllEvents;
    private View frameNoRegisterEvent;

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

        switch( view.getId() ){
            case R.id.btn_see_all_events:
                // hide no registered event frame
                frameNoRegisterEvent.setVisibility(View.INVISIBLE);

                // to runex.co website
                toRUNEXWebSite();

                // display progress dialog
//                refreshLayout.setRefreshing( true );
//
//                // refresh
//                onRefresh();

                break;
        }



    }
    @Override
    public void onSuccess(xResponse xrsp) {
        // prepare usage variables
        final String mtn = ct + "onSuccess() ";

        try {

            // hide progress
            refreshLayout.setRefreshing( false );

            if( xrsp.responseCode == HttpURLConnection.HTTP_UNAUTHORIZED ){
                // display need auth image
                displayNeedAuth();

                // exit from this process
                return;
            }

            // prepare usage variables
            final String jsonString = xrsp.jsonString;

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
                DisplayDateTimeObject start = DateTimeUtils.stringToDate(evtVal.getStart_reg());
                DisplayDateTimeObject end = DateTimeUtils.stringToDate(evtVal.getEnd_reg());

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

            // display frame no register event
            frameNoRegisterEvent.setVisibility(events.size() <= 0 ? View.VISIBLE : View.INVISIBLE);

        } catch (Exception e) {
            L.e(mtn + "Err: " + e);

        }
    }

    @Override
    public void onFailure(xResponse xResponse) {
        Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();

        // hide progress
        refreshLayout.setRefreshing( false );

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
        mChildFragmentUtils = ChildFragmentUtils.newInstance(this);
        eventAdapter = new EventAdapter(events, new onItemClick() {
            @Override
            public void onItemClicked(int position) {
                // prepare usage variables
                final String mtn = ct +"onItemClick() ";

                try {

                    // prepare usage variables
                    final EventDetailPage page = new EventDetailPage();

                    // update props
                    page.setEvent( (EventObject.DataBean)events.get(position).getAttachedObject());

                    // go to specified page
                    mChildFragmentUtils.addChildFragment(R.id.frame_fragment, page);

                } catch ( Exception e ){
                    L.e(mtn +"Err: "+ e.getMessage());
                }

            }
        });

        // matching view
        recyclerView = v.findViewById(R.id.recycler_view);
        refreshLayout = v.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        imgNeedAuth = v.findViewById(R.id.img_need_auth);
        frameNeedAuth = v.findViewById(R.id.frame_need_auth);
        //--> frame no registered event
        frameNoRegisterEvent = v.findViewById(R.id.frame_no_registered_event);
        btnSeeAllEvents = frameNoRegisterEvent.findViewById(R.id.btn_see_all_events);

        // view event listener
        viewEventListener();

        // recycler view props
        recyclerViewProps();

        // display progress dialog
        refreshLayout.setRefreshing( true );

        return v;
    }

    /** Feature methods */
    private void toRUNEXWebSite(){
        // prepare usage variables
        Intent i = new Intent(Intent.ACTION_VIEW);

        // update props
        i.setData(Uri.parse(Globals.URL_RUNEX));

        // go to web browser
        startActivityForResult(i, 0);
    }
    private void hideNeedAuth(){
        frameNeedAuth.setVisibility(View.INVISIBLE);

    }
    private void displayNeedAuth(){
        // display need auth image
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/beverestlife.appspot.com/o/folderEmptyState.png?alt=media&token=ec2146a6-e656-4a05-a1ef-25db7529047b")
                .into(imgNeedAuth, new Callback() {
                    @Override
                    public void onSuccess() {
                        frameNeedAuth.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });

    }

    /**
     * API methods
     */
    private void apiGetEvents() {
        // prepare usage variables
        new GetRegisteredEventService(getActivity(), this).doIt();
    }

    /** View event listener */
    private void viewEventListener(){
        btnSeeAllEvents.setOnClickListener( this );
    }

    /**
     * Recycler view props
     */
    private void recyclerViewProps() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(eventAdapter);
    }

    /** Life cycle */
    @Override
    public void onResume() {
        super.onResume();

        // when no event
        if( events.size() <= 0 ){
            // call api
            apiGetEvents();

            // just refresh
        } // else onRefresh();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        events = new ArrayList<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
