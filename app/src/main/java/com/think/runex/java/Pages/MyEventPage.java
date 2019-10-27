package com.think.runex.java.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.think.runex.R;
import com.think.runex.java.Constants.APIs;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Pages.Summary.SummaryPage;
import com.think.runex.java.Utils.ChildFragmentUtils;
import com.think.runex.java.Utils.L;
import com.think.runex.java.Utils.Network.NetworkProps;
import com.think.runex.java.Utils.Network.NetworkUtils;
import com.think.runex.java.Utils.Network.Request.rqLogin;
import com.think.runex.java.Utils.Network.onNetworkCallback;

public class MyEventPage extends Fragment implements onNetworkCallback {

    /** Main variables */
    private final String ct = "MyEventPage->";

    // instance variables
    private ChildFragmentUtils mChildFragmentUtils;

    // explicit variables
    private final int SUMMARY_PAGE_CONTAINER_ID = R.id.summary_page_container;

    // views
    private View btnSubmit;

    /** Implement methods */
    @Override
    public void onSuccess(String jsonString) {
        // prepaer usage variables
        final String mtn = ct +"onSuccess() ";

        L.i(mtn +"jsonString: "+ jsonString);
    }

    @Override
    public void onFailure(Exception jsonString) {
        // prepaer usage variables
        final String mtn = ct +"onSuccess() ";

        L.e(mtn +"jsonString: "+ jsonString);

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
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // logout
//                Toast.makeText(getActivity(), "Logout", Toast.LENGTH_SHORT).show();
                apiLogin();

            }
        });
    }

    /** API methods */
    private void apiLogin(){
        NetworkUtils nw = NetworkUtils.newInstance(getActivity());
        NetworkProps props = new NetworkProps();

        // update props
        props.addHeader("Authorization", "Bearer "+ Globals.TOKEN);
        props.setJsonAsObject(new rqLogin("fakespmh.21@gmail.com", "p@ss1234", "web"));
        props.setUrl(APIs.LOGIN.VAL);

        nw.postJSON( props, this );
    }

    /** Matching views */
    private void matchingView( View v ){
        btnSubmit = v.findViewById(R.id.submit_result_frame);
    }
}
