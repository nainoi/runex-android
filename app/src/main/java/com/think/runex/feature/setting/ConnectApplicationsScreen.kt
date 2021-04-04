package com.think.runex.feature.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jozzee.android.core.fragment.onBackPressed
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.util.extension.setStatusBarColor
import com.think.runex.util.extension.showAlertDialog
import com.think.runex.config.FACE_USER_AGENT_FOR_WEB_VIEW
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.util.NightMode
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.screen_connect_applications.*
import kotlinx.android.synthetic.main.screen_connect_applications.progress_bar
import kotlinx.android.synthetic.main.screen_connect_applications.web_view
import kotlinx.android.synthetic.main.screen_login.*
import kotlinx.android.synthetic.main.toolbar.*

class ConnectApplicationsScreen : BaseScreen() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_connect_applications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        performLoadApplicationsToConnect()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
    }

    /**
     * Get user info and generate konex url to connect application
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun performLoadApplicationsToConnect() = launch {
        progress_bar?.visible()
        val userInfo = getUserViewModel().getUSerInfoInstance()
        if (userInfo == null) {
            progress_bar?.gone()
            showAlertDialog(getString(R.string.error), getString(R.string.data_not_found), isCancelEnable = false) {
                onBackPressed()
            }
            return@launch
        }

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

        val url = "${ApiConfig.KONEX_URL}/${userInfo.providerId ?: ""}/${userInfo.provider ?: ""}"
        web_view?.loadUrl(url)
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        progress_bar?.gone()
    }
}