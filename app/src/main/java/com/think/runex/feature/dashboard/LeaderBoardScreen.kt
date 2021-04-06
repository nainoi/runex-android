package com.think.runex.feature.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.util.extension.setStatusBarColor
import com.think.runex.util.extension.setupToolbar
import com.think.runex.config.FACE_USER_AGENT_FOR_WEB_VIEW
import com.think.runex.config.KEY_EVENT_CODE
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.util.NightMode
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.screen_leader_board.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class LeaderBoardScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(eventCode: String) = LeaderBoardScreen().apply {
            arguments = Bundle().apply {
                putString(KEY_EVENT_CODE, eventCode)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_leader_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
        performLoadLeaderBoard()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar_layout, R.string.leader_board, R.drawable.ic_navigation_back)
    }

    private fun subscribeUi() {

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun performLoadLeaderBoard() = launch {
        progress_bar?.visible()

        //web_view?.requestFocus(View.FOCUS_DOWN)
        web_view?.settings?.apply {
            //setAppCacheEnabled(false)
            cacheMode = WebSettings.LOAD_NO_CACHE
            javaScriptEnabled = true
            //Set face user agent for google sign in.
            userAgentString = FACE_USER_AGENT_FOR_WEB_VIEW
        }

        web_view?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress_bar?.gone()
            }
        }

        val extraHeaders = HashMap<String, String>()
        extraHeaders["token"] = TokenManager.accessToken
        extraHeaders["id"] = "0"
        extraHeaders["code"] = arguments?.getString(KEY_EVENT_CODE) ?: ""
        //val url = "${ApiConfig.LEADER_BOARD_URL}/${userInfo.providerId ?: ""}/${userInfo.provider ?: ""}"
        web_view?.loadUrl(ApiConfig.LEADER_BOARD_URL, extraHeaders) //https://leaderboard.runex.co"
    }
}