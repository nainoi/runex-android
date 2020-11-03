package com.think.runex.java.Pages.RegisteredEvent;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.think.runex.R;
import com.think.runex.feature.event.model.registered.RegisteredEvent;
import com.think.runex.java.App.App;
import com.think.runex.java.App.AppEntity;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.RegisteredEventsObject;
import com.think.runex.java.Models.UserObject;
import com.think.runex.java.Pages.EventDetailPage;
import com.think.runex.java.Pages.onItemClick;
import com.think.runex.java.Utils.ChildFragmentUtils;
import com.think.runex.java.Utils.FragmentUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetRegisteredEventService;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.ui.component.recyclerview.LineSeparatorItemDecoration;

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
    private List<RegisteredEvent> events = new ArrayList<>();

    // explicit variables
    private boolean ON_REFRESH = false;
    private String mUserEmail = null;

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
    public xFragment onResult(com.think.runex.java.Customize.xTalk talk) {
        if (isAdded()) {
            // prepare usage variables
            final String mtn = ct + "onResult() ";

            // prepare usage variables
            AppEntity appEntity = App.instance(activity).getAppEntity();
            UserObject.DataBean user = appEntity.user.getData();

            if (mUserEmail != null && !((mUserEmail + "").equalsIgnoreCase(user.getEmail()))) {

                // exit from this process
                if (ON_REFRESH) return null;

                // update flag
                ON_REFRESH = true;

                // when no event
                // display refresh dialog
                refreshLayout.setRefreshing(true);

                // refresh
                onRefresh();
            }

            // update user email
            // for condition
            mUserEmail = user.getEmail();

        }

        return super.onResult(talk);
    }

    @Override
    public void onClick(View view) {
        // prepare usage variables
        final String mtn = ct + "onClick() ";

        if (ON_REFRESH) {
            // log
            L.i(mtn + "on refresh...");

            // exit from this process
            return;
        }

        switch (view.getId()) {
            case R.id.btn_see_all_events:
                // gone no registered event frame
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

            // gone progress
            refreshLayout.setRefreshing(false);

            if (xrsp.responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
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
            final RegisteredEventsObject object = GSON.fromJson(jsonString, RegisteredEventsObject.class);


//            Type registeredEventType = new TypeToken<List<RegisteredEvent>>() {
//            }.getType();
//            List<RegisteredEvent> mlList = GSON.fromJson(jsonString, registeredEventType);

            events.clear();
            // add all items
            if (object.getData() != null) {
                events.addAll(object.getData());
            }

            // notify data has changed
            if (ON_REFRESH) {
                // clear flag
                this.ON_REFRESH = false;
            }

            // notify data has change
            eventAdapter.notifyDataSetChanged();

            // gone progress dialog
            refreshLayout.setRefreshing(false);

            // display frame no register event
            frameNoRegisterEvent.setVisibility(events.size() <= 0 ? View.VISIBLE : View.INVISIBLE);

        } catch (Exception e) {
            L.e(mtn + "Err: " + e);

        }
    }

    @Override
    public void onFailure(xResponse xResponse) {
        // prepare usage variables
        final String mtn = ct + "onFailure() ";

        try {
            Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();

            // clear flag
            ON_REFRESH = false;

            // gone progress
            refreshLayout.setRefreshing(false);

        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }

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
                // exit from this process
                if (ON_REFRESH) return;

                // prepare usage variables
                final String mtn = ct + "onItemClick() ";

                try {

                    // prepare usage variables
                    final EventDetailPage page = new EventDetailPage();

                    // update props
                    page.setEvent(events.get(position));

                    // go to specified page

                    FragmentUtils.newInstance(activity).addFragment(activity.containerId, page, true);
//                    mChildFragmentUtils.addChildFragment(R.id.frame_fragment, page);

                } catch (Exception e) {
                    L.e(mtn + "Err: " + e.getMessage());
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
        refreshLayout.setRefreshing(true);

        return v;
    }

    /**
     * Feature methods
     */
    private void toRUNEXWebSite() {
        // prepare usage variables
        Intent i = new Intent(Intent.ACTION_VIEW);

        // update props
        i.setData(Uri.parse(Globals.URL_RUNEX));

        // go to web browser
        startActivityForResult(i, 0);
    }

    private void hideNeedAuth() {
        frameNeedAuth.setVisibility(View.INVISIBLE);

    }

    private void displayNeedAuth() {
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

    /**
     * View event listener
     */
    private void viewEventListener() {
        btnSeeAllEvents.setOnClickListener(this);
    }

    /**
     * Recycler view props
     */
    private void recyclerViewProps() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Drawable lineSeparator = ContextCompat.getDrawable(requireContext(), R.drawable.line_separator_list_item);
        recyclerView.addItemDecoration(new LineSeparatorItemDecoration(lineSeparator, false, true, RecyclerView.VERTICAL));
        recyclerView.setAdapter(eventAdapter);
    }

    /**
     * Life cycle
     */
    @Override
    public void onResume() {
        super.onResume();

        // when no item to display
        if (events.size() <= 0) {
            // call api
            apiGetEvents();

        }

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
