package com.think.runex.java.Pages.RegisteredEvent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.think.runex.R;
import com.think.runex.feature.auth.data.TokenManager;
import com.think.runex.java.App.App;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Constants.Priority;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.xTalk;
import com.think.runex.java.Utils.ChildFragmentUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class MyEventPage extends xFragment implements onNetworkCallback, View.OnClickListener {

    /**
     * Main variables
     */
    private final String ct = "MyEventPage->";

    // instance variables
    private xFragment pageRegisteredEvent;
    private App mApp;
    private ChildFragmentUtils mChildFragment;

    // explicit variables
    private final int SUMMARY_PAGE_CONTAINER_ID = R.id.summary_page_container;
    private boolean ON_LOADING = false;
    //--> Fragment
    private final String PAGE_REGISTERED_EVENT = "PAGE_REGISTERED_EVENT";
    // views
    private View btnSubmit;

    /**
     * Implement methods
     */
    @Override
    public xFragment onResult(xTalk talk) {

        if (isAdded()) {
            // xtalk refresh
            xTalk x = new xTalk();
            x.requestCode = Globals.RC_REFRESH;

            // to registered event
            pageRegisteredEvent.onResult(talk);

        }

        return this;
    }

    @Override
    public void onSuccess(xResponse rsp) {
        // prepaer usage variables
        final String mtn = ct + "onSuccess() ";

        L.i(mtn + "jsonString: " + rsp.jsonString);
    }

    @Override
    public void onFailure(xResponse rsp) {
        // prepaer usage variables
        final String mtn = ct + "onSuccess() ";

        L.e(mtn + "jsonString: " + rsp.jsonString);

    }

    @Override
    public void onClick(View view) {
        // prepare usage variables
        final String mtn = ct + "onClick() ";

        if (ON_LOADING) {
            // log
            L.i(mtn + "on loading[" + ON_LOADING + "] prevent on click.");

            // exit from this process
            return;
        }

        if (TokenManager.Companion.isAlive()) {

        } else mApp.serveLoginPage(getActivity(), Globals.RC_LOGIN_WITH_EMAIL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_my_event, container, false);

        // init
        mChildFragment = ChildFragmentUtils.newInstance(this);

        // matching views
        matchingView(v);

        // view event listener
        viewEventListener();

        // display summary page
        summaryPage();

        return v;
    }

    /**
     * Feature methods
     */
    private void summaryPage() {
        mChildFragment.addChildFragment(SUMMARY_PAGE_CONTAINER_ID, pageRegisteredEvent);

    }

    /**
     * View event listener
     */
    private void viewEventListener() {
        // prepare usage variables
        final String mtn = ct + "viewEventListener() ";

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                L.i(mtn + "clear app entity");

                // logout
                App.instance(getActivity()).clear();

                // perform login
                App.instance(getActivity()).serveLoginPage(MyEventPage.this, Globals.RC_NEED_LOGIN);

            }
        });
    }

    /** API methods */
    /**
     * Matching views
     */
    private void matchingView(View v) {
        btnSubmit = v.findViewById(R.id.submit_result_frame);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        getChildFragmentManager().putFragment(outState, PAGE_REGISTERED_EVENT, pageRegisteredEvent);

        super.onSaveInstanceState(outState);
    }

    /**
     * Life cycle
     */


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = App.instance(getActivity());

        if (savedInstanceState == null) {
            pageRegisteredEvent = new RegisteredEventsPage().setPriority(Priority.PARENT);

        } else {
            pageRegisteredEvent = (xFragment) getChildFragmentManager().getFragment(savedInstanceState, PAGE_REGISTERED_EVENT);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // prepare usage variables
        final String mtn = ct + "onActivityResult() ";
        final String resultCodeName = Globals.mapActivityResult(resultCode);

        L.i(mtn + "result: " + resultCodeName);

        if (requestCode == Globals.RC_NEED_LOGIN) {
            summaryPage();

        }
    }
}
