package com.think.runex.java.Pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.think.runex.R;
import com.think.runex.java.Customize.Fragment.xFragment;

public class SuccessfullySubmitRunningResultPage extends xFragment {
    /** Main variables */
    private final String ct = "SuccessfullySubmitRunningResultPage->";

    // views
    private View btnFinish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page_successfully_submit_running_result, container, false);

        btnFinish = v.findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onFragmentCallback(xTalk);

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

