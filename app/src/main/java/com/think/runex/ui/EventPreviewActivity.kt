package com.think.runex.ui

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.util.KEY_EVENT_ID
import kotlinx.android.synthetic.main.activity_event_preview.*
import kotlinx.android.synthetic.main.toolbar.*

class EventPreviewActivity : AppCompatActivity() {

    private var eventId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialValue()
        setStatusBarColor(Color.TRANSPARENT, true)
        setContentView(R.layout.activity_event_preview)
        setupToolbar(toolbar, R.string.detail, R.mipmap.ic_back)
        setupComponents()
        subscribeUi()

        //Load event preview
        progress_bar?.visible()
        web_view.loadUrl("${ApiConfig.EVENT_PREVIEW_URL}/$eventId")
    }

    private fun initialValue() {
        //Get event id from intent
        eventId = intent?.getStringExtra(KEY_EVENT_ID) ?: ""
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupComponents() {

        //Setup view view
        web_view?.settings?.apply {
            setAppCacheEnabled(false)
            cacheMode = WebSettings.LOAD_NO_CACHE
            javaScriptEnabled = true
        }
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress_bar?.gone()
            }
        }
    }

    private fun subscribeUi() {
        register_button.setOnClickListener {

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Logger.info(simpleName(), "On options item selected id: ${item.itemId}, title: ${item.title}")
        if (item.itemId == android.R.id.home /*&& fragmentBackStackCount() > 0*/) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}