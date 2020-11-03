package com.think.runex.java.Pages;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.think.runex.R;
import com.think.runex.feature.auth.TokenManager;
import com.think.runex.feature.event.model.registered.RegisteredEvent;
import com.think.runex.java.App.App;
import com.think.runex.java.App.AppEntity;
import com.think.runex.java.Customize.Fragment.xFragment;
import com.think.runex.java.Customize.Views.Toolbar.xToolbar;
import com.think.runex.java.Customize.Views.Toolbar.xToolbarProps;
import com.think.runex.java.Utils.L;

import java.util.HashMap;
import java.util.Map;

public class LeaderBoardPage extends xFragment {
    /**
     * Main variables
     */
    private final String ct = "LeaderBoardPage->";

    //--> toolbar
    private xToolbar toolbar;
    private WebView webView;

    //--> event
    private String eventID;

    /**
     * Setter
     */
    public xFragment setEventID(String id) {
        eventID = id;

        return this;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final String mtn = ct + "onCreate() ";

        // prepare usage variables
        final View v = inflater.inflate(R.layout.page_leader_bard, container, false);

        // view matching
        viewMatching(v);

        try {
            toolbar = new xToolbar(v.findViewById(R.id.frame_toolbar)) {
                @Override
                public void onClick(View view) {
                    if (view.getId() == R.id.toolbar_navigation_button) {
                        activity.onBackPressed();
                    }
                }
            };
            toolbar.toolbarOptionButton.gone();
            toolbar.toolbarTitleIcon.gone();

            // toolbar props
            xToolbarProps props = new xToolbarProps();
            props.titleLabel = "Leaderboard";

            // binding
            toolbar.bind(props);
        } catch (Exception e) {
            L.e(mtn + "Err: " + e.getMessage());
        }

        return v;
    }

    // view matching
    @SuppressLint("SetJavaScriptEnabled")
    private void viewMatching(View v) {
        webView = v.findViewById(R.id.web_view);
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setSaveFormData(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webView.setWebViewClient(new WebViewClient() {
//            @Nullable
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                return super.shouldInterceptRequest(view, request);
//            }
//        });

        AppEntity appEntity = App.instance(getActivity()).getAppEntity();
        HashMap<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("token", TokenManager.Companion.getAccessToken().replace("Bearer ", ""));
        extraHeaders.put("id", eventID);

        webView.loadUrl("https://runex-leaderboard.thinkdev.app", extraHeaders);
    }
}
