package com.think.runex.java.Pages.ReviewEvent;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.think.runex.R;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Models.RegEventsObject;
import com.think.runex.java.Models.TokenObject;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Request.MyRegEventService;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.net.HttpURLConnection;

public class ReviewEventPage extends DialogFragment implements View.OnClickListener {

    private RecyclerView eventList;
    private EventCheckerAdapter adapter;
    private AppCompatButton btnConfirm;


    /**
     * Main variables
     */
    private final String ct = "ReviewEventPage->";

    public ReviewEventPage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_review_event, container, false);

        // views matching
        viewsMatching(v);
        viewEventListener();

        adapter = new EventCheckerAdapter();
        eventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList.setAdapter(adapter);


        apiGetMyRegEvent();

        return v;
    }

    /**
     * Views matching
     */
    private void viewsMatching(View v) {
        eventList = v.findViewById(R.id.event_list);
        btnConfirm = v.findViewById(R.id.btn_confirm);
    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        btnConfirm.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                btnConfirmClick();
                break;
        }
    }

    private void btnConfirmClick() {
        if (adapter != null) {
            String[] selectedEvent = adapter.getSelectedEvents();
            if (selectedEvent.length > 0) {
                if (getConfirmEventsListener() != null) {
                    getConfirmEventsListener().onConfirmEvents(selectedEvent);
                    dismissAllowingStateLoss();
                }
            } else {
                Toast.makeText(getContext(), "กรุณาเลือกรายการอีเวนท์มี่จะส่งระยะ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * API methods
     */
    private void apiGetMyRegEvent() {
        new MyRegEventService(getActivity(), new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i("json-string: " + response.jsonString);
                RegEventsObject object = Globals.GSON.fromJson(response.jsonString, RegEventsObject.class);
                if (object.getCode() == HttpURLConnection.HTTP_OK) {
                    for (int i = 0; i < object.getList().size(); i++) {
                        RegEventsObject.EventChecker eventChecker = object.getList().get(i);
                        eventChecker.setChecked(true);
                        object.getList().set(i, eventChecker);
                    }
                    adapter.submitList(object.getList());
                }
            }

            @Override
            public void onFailure(xResponse response) {
                L.e("error-string: " + response.jsonString);
            }
        }).doIt();
    }

    private OnConfirmEventsListener getConfirmEventsListener() {
        if (getParentFragment() != null && getParentFragment() instanceof OnConfirmEventsListener) {
            return (OnConfirmEventsListener) getParentFragment();
        } else if (getActivity() != null && getActivity() instanceof OnConfirmEventsListener) {
            return (OnConfirmEventsListener) getActivity();
        }
        return null;
    }

}
