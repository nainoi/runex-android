package com.think.runex.java.Pages;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.think.runex.R;
import com.think.runex.java.Customize.xFragment;

public class PendingPaymentPage extends xFragment {
    /** Main variables */
    private final String ct = "RegisteredEventsPage->";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_pending_payment, container, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tabChangedListener.onNotify( position );

            }
        }, 1000);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
