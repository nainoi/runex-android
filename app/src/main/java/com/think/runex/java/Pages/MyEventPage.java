package com.think.runex.java.Pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.think.runex.R;
import com.think.runex.java.App.App;
import com.think.runex.java.App.Configs;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Pages.Summary.SummaryPage;
import com.think.runex.java.Utils.ChildFragmentUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqLogin;
import com.think.runex.java.Utils.Network.Response.xResponse;
import com.think.runex.java.Utils.Network.onNetworkCallback;
import com.think.runex.ui.LoginScreen;

public class MyEventPage extends Fragment implements onNetworkCallback, View.OnClickListener {

    /** Main variables */
    private final String ct = "MyEventPage->";

    // instance variables
    private ChildFragmentUtils mChildFragmentUtils;
    private App mApp;

    // explicit variables
    private final int SUMMARY_PAGE_CONTAINER_ID = R.id.summary_page_container;
    private boolean ON_LOADING = false;

    // views
    private View btnSubmit;


    /** Implement methods */
    @Override
    public void onSuccess(xResponse rsp) {
        // prepaer usage variables
        final String mtn = ct +"onSuccess() ";

        L.i(mtn +"jsonString: "+ rsp.jsonString);
    }

    @Override
    public void onFailure(xResponse rsp) {
        // prepaer usage variables
        final String mtn = ct +"onSuccess() ";

        L.e(mtn +"jsonString: "+ rsp.jsonString);

    }

    @Override
    public void onClick(View view) {
        // prepare usage variables
        final String mtn = ct +"onClick() ";

        if( ON_LOADING ){
            // log
            L.i(mtn +"on loading["+ ON_LOADING +"] prevent on click.");

            // exit from this process
            return;
        }

        if (mApp.getAppEntity().isLoggedIn){

        } else mApp.serveLoginPage(getActivity(), Globals.RC_LOGIN_WITH_EMAIL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.page_my_event, container, false);

        // init
        mChildFragmentUtils = new ChildFragmentUtils(this);

        // matching views
        matchingView( v );

        // view event listener
        viewEventListener();

        // display summary page
        summaryPage();

        return v;
    }

    /** Feature methods */
    private void summaryPage(){
        mChildFragmentUtils.replaceChildFragment(SUMMARY_PAGE_CONTAINER_ID, new SummaryPage());

    }

    /** View event listener */
    private void viewEventListener(){
        // prepare usage variables
        final String mtn = ct +"viewEventListener() ";

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display login page
//                mChildFragmentUtils.addChildFragment(SUMMARY_PAGE_CONTAINER_ID, new LoginScreen());

                L.i(mtn +"clear app entity");

                // logout
                App.instance(getActivity()).clear();

                // perform login
                App.instance(getActivity()).serveLoginPage(MyEventPage.this, Globals.RC_NEED_LOGIN);

            }
        });
    }

    /** API methods */
    /** Matching views */
    private void matchingView( View v ){
        btnSubmit = v.findViewById(R.id.submit_result_frame);
    }

    /** Life cycle */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApp = App.instance( getActivity() );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // prepare usage variables
        final String mtn = ct +"onActivityResult() ";
        final String resultCodeName = Globals.mapActivityResult( resultCode );

        L.i(mtn +"result: "+ resultCodeName);

        if( requestCode == Globals.RC_NEED_LOGIN ){
            summaryPage();

        }



    }
}
