package com.think.runex.java.Pages;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.think.runex.feature.event.data.EventItem;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.AllEventsObject;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.Services.GetAllEventService;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.feature.event.detail.EventPreviewActivity;
import com.think.runex.component.recyclerview.LineSeparatorItemDecoration;
import com.think.runex.feature.event.all.AllEventsAdapter;

import java.net.HttpURLConnection;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

import static com.think.runex.java.Constants.Globals.GSON;
import static com.think.runex.config.ConstantsKt.KEY_EVENT;

public class AllEventsPage extends xFragment {
    /**
     * Main variables
     */
    private final String ct = "EventsPage->";

    // instance variables
    private AllEventsAdapter adapter;

    //Views
    private RecyclerView eventList;
    private SwipeRefreshLayout refreshLayout;
    private ImageView needAuthImage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_all_events, container, false);

        // views matching
        viewsMatching(v);

        //components setup
        recyclerViewProps();

        // views event listener
        viewEventListener();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter.getItemCount() == 0) {
            refreshLayout.setRefreshing(true);
            apiGetAllEvents();
        }
    }

    /**
     * Views matching
     */
    private void viewsMatching(View v) {
        eventList = v.findViewById(R.id.event_list);
        refreshLayout = v.findViewById(R.id.refresh_layout);
        needAuthImage = v.findViewById(R.id.need_auth_image);
    }

    private void recyclerViewProps() {
        adapter = new AllEventsAdapter();
        eventList.setLayoutManager(new LinearLayoutManager(requireContext()));
        Drawable lineSeparator = ContextCompat.getDrawable(requireContext(), R.drawable.line_separator_list_item);
        eventList.addItemDecoration(new LineSeparatorItemDecoration(lineSeparator, false, true, RecyclerView.VERTICAL));
        eventList.setAdapter(adapter);
    }

    private void viewEventListener() {
        adapter.setOnItemClickListenerJava(new Function2<Integer, EventItem, Unit>() {
            @Override
            public Unit invoke(Integer position, EventItem event) {
                Intent intent = new Intent(activity, EventPreviewActivity.class);
                intent.putExtra(KEY_EVENT, event);
                startActivity(intent);
                return null;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                apiGetAllEvents();
            }
        });
    }

    private void apiGetAllEvents() {
        new GetAllEventService(requireActivity(), new onNetworkCallback() {
            final String mtn = ct + "onSuccess() ";

            @Override
            public void onSuccess(xResponse response) {
                try {
                    // gone progress
                    refreshLayout.setRefreshing(false);

                    if (response.responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        // display need auth image
                        displayNeedAuth();

                        // exit from this process
                        return;
                    }
                    hideNeedAuth();

                    // prepare usage variables
                    final String jsonString = response.jsonString;

                    // logs
                    L.i(mtn + "JSONResult: " + jsonString);

                    AllEventsObject allEventsObject = GSON.fromJson(jsonString, AllEventsObject.class);

                    adapter.submitList(allEventsObject.getData());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(xResponse response) {
                // gone progress
                refreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                hideNeedAuth();
            }
        }).doIt();
    }

    private void hideNeedAuth() {
        needAuthImage.setVisibility(View.GONE);

    }

    private void displayNeedAuth() {
        // gone progress
        refreshLayout.setRefreshing(false);
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/beverestlife.appspot.com/o/folderEmptyState.png?alt=media&token=ec2146a6-e656-4a05-a1ef-25db7529047b")
                .into(needAuthImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        needAuthImage.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }
}
