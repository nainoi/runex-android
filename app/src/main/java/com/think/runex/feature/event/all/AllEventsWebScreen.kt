package com.think.runex.feature.event.all

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.util.extension.setStatusBarColor
import kotlinx.android.synthetic.main.screen_web_view.*


class AllEventsWebScreen : BaseScreen() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()

//        AppConfig().eventsMobileUrl?.let {
//            performLoadUrl(it)
//        }

        performLoadUrl("https://runex-my-next.thinkdev.app/mobile/events?code=384f05e87c133740071a809a29b65905b55a8859")
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = true)
        web_view.settings.javaScriptEnabled = true

        val webSettings: WebSettings = web_view.settings
        webSettings.javaScriptEnabled = true
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.domStorageEnabled = true
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun performLoadUrl(url: String) {
        progress_bar?.visible()
        web_view.settings.javaScriptEnabled = true
        web_view.settings.setSupportZoom(false)
        web_view?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress_bar?.gone()
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                progress_bar?.gone()
                Log.e("Load events", error.toString())
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                Log.d("Load events", url!!)
                url?.let { view?.loadUrl(it) }
                return true
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
                handler?.proceed()
            }
        }
        web_view?.loadUrl(url)
    }
}