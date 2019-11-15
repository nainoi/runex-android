package com.think.runex.java.Pages;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.think.runex.R;
import com.think.runex.java.Constants.Globals;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.xTalk;

public class SuccessfullySubmitRunningResultPage extends xFragment {
    /** Main variables */
    private final String ct = "SuccessfullySubmitRunningResultPage->";

    // views
    private View btnFinish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page_successfully_submit_running_result, container, false);

        // view binding
        btnFinish = v.findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // exit from this page
                getParentFragment().getChildFragmentManager()
                        .beginTransaction()
                        .remove( SuccessfullySubmitRunningResultPage.this )
                        .commit();

                // prepare usage variables
                final xTalk x = new xTalk();
                x.requestCode = Globals.RC_TO_PROFILE_PAGE;

                // send back result
                onResult(x);

            }
        });

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


        // callback
        activity.onFragmentCallback(xTalk);
    }
}

