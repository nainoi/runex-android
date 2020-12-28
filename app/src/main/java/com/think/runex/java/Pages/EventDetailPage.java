package com.think.runex.java.Pages;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.think.runex.datasource.api.ApiConfig;
import com.think.runex.feature.event.model.registered.RegisteredEvent;
import com.think.runex.feature.event.model.registered.RegisteredEventInfo;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Constants.xAction;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.Fragment.xFragmentHandler;
import com.think.runex.java.Customize.Views.Toolbar.xToolbar;
import com.think.runex.java.Customize.Views.Toolbar.xToolbarProps;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Models.EventDetailObject;
import com.think.runex.java.Models.WorkoutInfo;
import com.think.runex.java.Pages.Record.WorkoutsAdapter;
import com.think.runex.java.Utils.ActivityUtils;
import com.think.runex.java.Utils.FragmentUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.DeleteEventHistoryService;
import com.think.runex.java.Utils.Network.Services.GetEventDetailService;
import com.think.runex.java.Utils.Network.onNetworkCallback;


import java.net.HttpURLConnection;
import java.util.ArrayList;

public class EventDetailPage extends xFragment implements View.OnClickListener
        , SwipeRefreshLayout.OnRefreshListener {

    /**
     * Main variables
     */
    private final String ct = "EventDetailPage->";

    // instance variables
    private RegisteredEvent mEvent;
    private WorkoutsAdapter adapter;

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
    private RelativeLayout viewLeaderBoardButton;
    // views
    private RecyclerView recordList;
    //--> toolbar
    private xToolbar toolbar;

    /**
     * Implement methods
     */
    @Override
    public void onRefresh() {
        if (ON_NETWORKING) return;

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

        if (mEvent == null) return v;

        // update props
        mEventId = mEvent.getEventId();
        if (mEvent.getRegisterInfoList() != null && mEvent.getRegisterInfoList().size() > 0) {
            RegisteredEventInfo evtVal = mEvent.getRegisterInfoList().get(0);
            if (evtVal.getEvent() != null) {
                mEventProfile = evtVal.getEvent().coverImage();
                mEventName = evtVal.getEvent().getName();
            }
        }


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
                switch (view.getId()) {
                    case R.id.toolbar_navigation_button:
                        activity.onBackPressed();
                        break;
                    case R.id.toolbar_options_button:
                        toRecordPage();
                        break;

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
            //--> option button
            if (mEvent.getRegisterInfoList() != null && mEvent.getRegisterInfoList().size() > 0) {
                if (mEvent.getRegisterInfoList().get(0).getEvent() != null) {
                    if (!mEvent.getRegisterInfoList().get(0).getEvent().isInApp()) {
                        toolbar.setImageOptionIcon(R.drawable.ic_add);
                        toolbar.setOptionButtonColorFilter(R.color.colorAccent);

                        // hide toolbar option button
                    } else toolbar.toolbarOptionButton.gone();
                }
            }

            //--> navigate button
            toolbar.setNavigationButtonColorFilter(R.color.colorAccent);
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

    private void removeItem(int position) {
        adapter.getList().remove(position);
        adapter.notifyItemRemoved(position);
    }

    // recycler view props
    private void recyclerViewProps() {
        // prepare usage variables
        adapter = new WorkoutsAdapter(null, true, new WorkoutsAdapter.OnDeleteRecordListener() {
            @Override
            public void onDeleteRecord(int position) {
                dialogConfirmation("ยืนยันลบกิจกรรม", "คุณต้องการลบกิจกรรมนี้ใช่หรือไม่", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (DialogInterface.BUTTON_POSITIVE == i) {
                            // display progress dialog
                            refreshLayout.setRefreshing(true);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(adapter);
    }

    // event view holder
    private void eventViewHolder(View v) {
        // prepare usage variables
        View vh = v.findViewById(R.id.vh_event);
        if (mEvent.getRegisterInfoList() != null && mEvent.getRegisterInfoList().size() > 0) {
            RegisteredEventInfo evtVal = mEvent.getRegisterInfoList().get(0);
            if (evtVal.getEvent() != null) {
                TextView _lbEventName = vh.findViewById(R.id.lb_event_name);
                TextView _lbEventType = vh.findViewById(R.id.lb_event_type);
                ImageView imgCover = vh.findViewById(R.id.view_cover);

                // binding
                _lbEventName.setText(evtVal.getEvent().getName());
                _lbEventType.setText((evtVal.getEvent().getCategory()));

                //--> image
                Picasso.get().load(evtVal.getEvent().coverImage()).into(imgCover);
            }
        }
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

        viewLeaderBoardButton = v.findViewById(R.id.view_leader_board_button);

    }

    // view event listener
    private void viewEventListener() {
        refreshLayout.setOnRefreshListener(this);
        btnAddActivity.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        viewLeaderBoardButton.setOnClickListener(this);
    }

    /**
     * API methods
     */
    private void apiDeleteEventHistory(String eventId, String historyId, int position) {
        // prepare usage variables
        final String mtn = ct + "apiDeleteEventHistory() ";

        // fire
        new DeleteEventHistoryService(activity, new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i(mtn + "response-code: " + response.responseCode);
                L.i(mtn + "response-json: " + response.jsonString);

                if (response.responseCode == HttpURLConnection.HTTP_OK) {
                    // toast
                    Toast.makeText(activity, "ลบกิจกรรมสำเร็จ", Toast.LENGTH_SHORT).show();

                    // item removed notify
                    removeItem(position);

                }

                // hide progress bar
                refreshLayout.setRefreshing(false);

                // update flag
                ON_NETWORKING = false;

                // refresh
                onRefresh();

            }

            @Override
            public void onFailure(xResponse response) {
                L.i(mtn + "response-code: " + response.responseCode);
                L.i(mtn + "response-json: " + response.jsonString);

                // hide progress bar
                refreshLayout.setRefreshing(false);

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
                    lbTotalDistance.setText("" + Globals.DCM_2.format(db.getActivities().getTotal_distance()));

                    //Keep activity record list for EventRecordHistoryPage
                    if (db.getActivities().getActivity_info() != null) {
                        ArrayList<WorkoutInfo> workoutInfo = new ArrayList<>();
                        for (EventDetailObject.activity_info item : db.getActivities().getActivity_info()) {
                            WorkoutInfo info = new WorkoutInfo();
                            info.setId(item.getId());
                            info.setDistance(item.getDistance());
                            info.setCaption(item.getCaption());
                            info.setApp(item.getApp());
                            info.setDuration(item.getTime());
                            info.setWorkout_date(item.getActivity_date());
                            workoutInfo.add(info);
                        }
                        adapter.submitList(workoutInfo);
                    }

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());

                }

                // hide progress dialog
                refreshLayout.setRefreshing(false);

                // clear flag
                ON_NETWORKING = false;

            }

            @Override
            public void onFailure(xResponse response) {
                L.e(mtn + "err-response: " + response.jsonString);

                // hide progress dialog
                refreshLayout.setRefreshing(false);

                // clear flag
                ON_NETWORKING = false;

            }
        }).doIt(eventId);
    }

    /**
     * Setter
     */
    public xFragment setEvent(RegisteredEvent event) {
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
                        // on refresh
                        onRefresh();

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
        } else if (v.getId() == R.id.view_leader_board_button) {
            toLeaderBoardPage();
        }
    }


    private void toRecordPage() {
        // prepare usage variables
        View v = new View(getActivity());
        v.setId(R.id.btn_add_activity);

        // by pass
        onClick(v);
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

    private void toLeaderBoardPage() {
        // prepare usage variables
        final String mtn = ct + "toLeaderBoardPage() ";
        try {
            // prepare usage variables
            final LeaderBoardPage page = new LeaderBoardPage();
            page.setEventID(mEventId);
            // go to specified page
            FragmentUtils.newInstance(activity).addFragment(activity.containerId, page, true);
        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }
    }
}
