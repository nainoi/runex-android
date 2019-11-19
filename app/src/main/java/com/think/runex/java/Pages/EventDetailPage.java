package com.think.runex.java.Pages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Constants.Payment;
import com.think.runex.java.Constants.xAction;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.Fragment.xFragmentHandler;
import com.think.runex.java.Customize.Views.Toolbar.xToolbar;
import com.think.runex.java.Customize.Views.Toolbar.xToolbarProps;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Models.EventDetailObject;
import com.think.runex.java.Models.EventObject;
import com.think.runex.java.Models.ActivityInfoBean;
import com.think.runex.java.Models.MultiObject;
import com.think.runex.java.Pages.Record.EventRecordHistoryPage;
import com.think.runex.java.Pages.Record.RecordAdapter;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.ChildFragmentUtils;
import com.think.runex.java.Utils.FragmentUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.DeleteEventHistoryService;
import com.think.runex.java.Utils.Network.Services.GetEventDetailService;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.java.ViewHolders.VHEvent;


import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class EventDetailPage extends xFragment implements View.OnClickListener
    , SwipeRefreshLayout.OnRefreshListener {

    /**
     * Main variables
     */
    private final String ct = "EventDetailPage->";

    // instance variables
    private EventObject.DataBean mEvent;
    private RecordAdapter adapter;

    // explicit variables
    private String mEventProfile = null;
    private String mEventName = null;
    private String mEventId = null;
    private boolean ON_NETWORKING = false;

    // views
    private TextView lbEventName;
    private ImageView eventImage;
    private TextView lbTotalDistance;
    private AppCompatImageButton btnAddActivity;
    private AppCompatImageButton btnHistory;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    // views
    private RecyclerView recordList;
    //--> toolbar
    private xToolbar toolbar;

    /** Implement methods */
    @Override
    public void onRefresh() {
        if( ON_NETWORKING ) return;

        // update flag
        ON_NETWORKING = true;

        // get event detail
        apiGetEventDetail(mEventId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_event_detail_version2, container, false);
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
        //--> Event view holder
        eventViewHolder(v);
        //--> Recycler view props
        recyclerViewProps();
        //--> toolbar
        toolbar = new xToolbar(v.findViewById(R.id.frame_toolbar)) {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.toolbar_navigation_button: activity.onBackPressed(); break;
                    case R.id.toolbar_options_button: toRecordPage(); break;

                }
            }
        };

        // view event listener
        viewEventListener();

        try {
            // binding event name
            lbEventName.setText(mEventName);
            Picasso.get().load(mEventProfile).into(eventImage);

            // toolbar props
            xToolbarProps props = new xToolbarProps();

            // update props
//            props.titleImageUrl = mEventProfile;
            props.titleLabel = mEventName;
            //--> views
            toolbar.setImageOptionIcon(R.drawable.ic_add);
            toolbar.setNavigationButtonColorFilter(R.color.orange);
            toolbar.setOptionButtonColorFilter(R.color.orange);
            toolbar.toolbarTitleIcon.gone();

            // binding
            toolbar.bind(props);

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }

        return v;
    }

    /**
     * Feature methods
     */
    private AlertDialog dialogConfirmation(String title, String desc, DialogInterface.OnClickListener listener) {
        // prepare usage variables
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        // update props
        builder.setTitle(title);
        builder.setMessage(desc);
        builder.setPositiveButton("ยืนยัน", listener);
        builder.setNegativeButton("ยกเลิก", listener);

        // show
        return builder.create();

    }
    private void removeItem(int position){
        adapter.getList().remove( position );
        adapter.notifyItemRemoved( position );
    }
    // recycler view props
    private void recyclerViewProps(){
        // prepare usage variables
        adapter = new RecordAdapter(true,new onItemClick() {
            @Override
            public void onItemClicked(int position) {

                dialogConfirmation("ยืนยันลบกิจกรรม", "คุณต้องการลบกิจกรรมนี้ใช่หรือไม่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if( DialogInterface.BUTTON_POSITIVE == i ){
                            // display progress dialog
                            refreshLayout.setRefreshing( true );

                            // update flag
                            ON_NETWORKING = true;

                            // fire: delete event history
                            apiDeleteEventHistory(mEventId, adapter.getItem(position).getId(), position);

                            // dismiss
                        } else dialogInterface.dismiss();

                    }
                }).show();
            }
        });

        //--> update props
        recyclerView.setLayoutManager( new LinearLayoutManager( activity ));
        recyclerView.setAdapter(adapter);
    }

    // event view holder
    private void eventViewHolder(View v){
        // prepare usage variables
        View vh = v.findViewById( R.id.vh_event );
        EventObject.DataBean evt = mEvent;
        EventObject.DataBean.EventBean evtVal = evt.getEvent();
        TextView _lbEventName = vh.findViewById(R.id.lb_event_name);
        TextView _lbEventType = vh.findViewById(R.id.lb_event_type);
        ImageView imgCover = vh.findViewById(R.id.view_cover);

        // binding
        _lbEventName.setText((evtVal.getName() +"").trim() );
        _lbEventType.setText( (evtVal.getCategory().getName() +"").toUpperCase() );

        //--> image
        Picasso.get().load(APIs.DOMAIN.VAL + evtVal.getCover() ).into( imgCover );
    }
    // view matching
    private void viewMatching(View v) {
        eventImage = v.findViewById(R.id.event_image);
        lbEventName = v.findViewById(R.id.detail_lb_event_name);
        lbTotalDistance = v.findViewById(R.id.lb_total_running_distance);
        btnAddActivity = v.findViewById(R.id.btn_add_activity);
        btnHistory = v.findViewById(R.id.btn_history);

        recyclerView = v.findViewById(R.id.recycler_view);
        refreshLayout = v.findViewById(R.id.refresh_layout);

    }

    // view event listener
    private void viewEventListener() {
        refreshLayout.setOnRefreshListener( this );
        btnAddActivity.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
    }

    /**
     * API methods
     */
    private void apiDeleteEventHistory(String eventId, String historyId, int position){
        // prepare usage variables
        final String mtn = ct +"apiDeleteEventHistory() ";

        // fire
        new DeleteEventHistoryService(activity, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn +"response-code: "+ response.responseCode);
                L.i(mtn +"response-json: "+ response.jsonString);

                if( response.responseCode == HttpURLConnection.HTTP_OK ){
                    // toast
                    Toast.makeText(activity, "ลบกิจกรรมสำเร็จ", Toast.LENGTH_SHORT).show();

                    // item removed notify
                    removeItem( position );

                }

                // hide progress bar
                refreshLayout.setRefreshing( false );

                // update flag
                ON_NETWORKING = false;

                // refresh
                onRefresh();

            }

            @Override
            public void onFailure(xResponse response) {
                L.i(mtn +"response-code: "+ response.responseCode);
                L.i(mtn +"response-json: "+ response.jsonString);

                // hide progress bar
                refreshLayout.setRefreshing( false );

                // update flag
                ON_NETWORKING = false;

            }
        }).doIt(eventId, historyId);
    }
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
                    lbTotalDistance.setText(""+ Globals.DCM_2.format(db.getTotal_distance()));

                    //Keep activity record list for EventRecordHistoryPage
                    adapter.submitList(db.getActivity_info());

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());

                }

                // hide progress dialog
                refreshLayout.setRefreshing( false );

                // clear flag
                ON_NETWORKING = false;

            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn + "err-response: " + response.jsonString);

                // hide progress dialog
                refreshLayout.setRefreshing( false );

                // clear flag
                ON_NETWORKING = false;

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
            FragmentUtils.newInstance(activity).addFragment(activity.containerId, page, true);
//            ChildFragmentUtils.newInstance(this).addChildFragment
//                    (R.id.display_fragment_frame,
//                            page);

        } else if (v.getId() == R.id.btn_history) {
            toRecordPage();
        }
    }


    private void toRecordPage() {
        // prepare usage variables
        View v = new View(getActivity());
        v.setId( R.id.btn_add_activity );

        // by pass
        onClick( v );
//
//        if (adapter.getItemCount() == 0) {
//            Toast.makeText(getContext(), R.string.activity_record_empty, Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        // prepare usage variables
//        EventRecordHistoryPage page = new EventRecordHistoryPage();
//
//        // update props
//        page.setRecordList(adapter.getList());
//
//        // go to record page
//        ChildFragmentUtils.newInstance(this).addChildFragment(R.id.display_fragment_frame, page);
    }
}
