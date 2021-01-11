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
import com.think.runex.feature.event.model.EventRegistered;
import com.think.runex.java.Models.EventIdAndPartnerObject;
import com.think.runex.java.Models.RegisteredEventsObject;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Services.GetMyActiveRegisteredEventService;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.onNetworkCallback;

import java.net.HttpURLConnection;
import java.util.List;

import static com.think.runex.java.Constants.Globals.GSON;

public class ActiveRegisteredEventCheckerPage extends DialogFragment implements View.OnClickListener {

    private RecyclerView eventList;
    private EventCheckerAdapter adapter;
    private AppCompatButton btnConfirm;


    /**
     * Main variables
     */
    private final String ct = "ActiveRegisteredEventCheckerPage->";

    public ActiveRegisteredEventCheckerPage() {
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

        v.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = ActiveRegisteredEventCheckerPage.this;

                // event callback
                if (getConfirmEventsListener() != null) {
                    getConfirmEventsListener().onConfirmEvents(null);
                    dismissAllowingStateLoss();
                }

                // exit frmo this page
                fragment.getFragmentManager().beginTransaction()
                        .remove(fragment)
                        .commit();
            }
        });

        apiGetMyActiveRegEvent();

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
            List<EventIdAndPartnerObject> selectedEvent = adapter.getSelectedEvents();
            if (selectedEvent.size() > 0) {
                if (getConfirmEventsListener() != null) {
                    getConfirmEventsListener().onConfirmEvents(selectedEvent);
//                    dismissAllowingStateLoss();
                }
            } else {
                Toast.makeText(getContext(), "กรุณาเลือกรายการอีเวนท์มี่จะส่งระยะ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * API methods
     */
    //TODO("Update api")
    private void apiGetMyActiveRegEvent() {
        new GetMyActiveRegisteredEventService(getActivity(), new onNetworkCallback() {
            @Override
            public void onSuccess(xResponse response) {
                L.i("json-string: " + response.jsonString);
                RegisteredEventsObject object = GSON.fromJson(response.jsonString, RegisteredEventsObject.class);

                if (object.getCode() == HttpURLConnection.HTTP_OK) {
                    for (int i = 0; i < object.getData().size(); i++) {
                        EventRegistered data = object.getData().get(i);
                        data.setChecked(true);
                        object.getData().set(i, data);
                    }
                    adapter.submitList(object.getData());
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
