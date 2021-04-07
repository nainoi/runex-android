package com.think.runex.feature.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.think.runex.config.*
import com.think.runex.util.extension.setStatusBarColor
import com.think.runex.util.extension.setupToolbar
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.util.NightMode
import com.think.runex.util.extension.launch
import com.think.runex.util.extension.toJson
import kotlinx.android.synthetic.main.screen_leader_board.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

class LeaderBoardScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(eventCode: String,
                        eventId: Int,
                        registerId: String,
                        parentRegisterId: String,
                        ticketId: String) = LeaderBoardScreen().apply {

            arguments = Bundle().apply {
                putString(KEY_EVENT_CODE, eventCode)
                putInt(KEY_EVENT_ID, eventId)
                putString(KEY_REGISTER_ID, registerId)
                putString(KEY_PARENT_REGISTER_ID, parentRegisterId)
                putString(KEY_TICKET, ticketId)
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
            //userAgentString = FACE_USER_AGENT_FOR_WEB_VIEW
        }

        web_view?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress_bar?.gone()
            }
        }

        arguments?.run {

            val extraHeaders = HashMap<String, String>()
            extraHeaders["token"] = TokenManager.accessToken(false)
            extraHeaders["id"] = getString(KEY_EVENT_CODE) ?: ""//getInt(KEY_EVENT_ID).toString()
            //extraHeaders["event_code"] = getString(KEY_EVENT_CODE) ?: ""
            extraHeaders["reg_id"] = getString(KEY_REGISTER_ID) ?: ""
            extraHeaders["parent_reg_id"] = getString(KEY_PARENT_REGISTER_ID) ?: ""
            extraHeaders["ticket_id"] = getString(KEY_TICKET) ?: ""

            Log.e("Jozzee", "extraHeaders: ${extraHeaders.toJson()}")

            web_view?.loadUrl(ApiConfig.LEADER_BOARD_URL, extraHeaders) //https://leaderboard.runex.co"
        }
    }
}