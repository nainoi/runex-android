package com.think.runex.ui

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.getViewModel
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.event.EventViewModel
import com.think.runex.feature.event.EventViewModelFactory
import com.think.runex.feature.event.model.Event
import com.think.runex.util.KEY_EVENT
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.activity_event_preview.*
import kotlinx.android.synthetic.main.toolbar.*

class EventPreviewActivity : AppCompatActivity() {

    private lateinit var viewModel: EventViewModel
    private var event: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialValue()
        setContentView(R.layout.activity_event_preview)
        setupComponents()
        subscribeUi()

        //Load event preview
        progress_bar?.visible()
        web_view.loadUrl("${ApiConfig.EVENT_PREVIEW_URL}/${event?.id}")

        //Check is registered event
        checkRegisteredEvent()
    }

    private fun initialValue() {
        viewModel = getViewModel(EventViewModelFactory(this))

        //Get event id from intent
        event = intent?.getParcelableExtra(KEY_EVENT)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupComponents() {
        setStatusBarColor(Color.TRANSPARENT, true)
        setupToolbar(toolbar, R.string.detail, R.mipmap.ic_back)

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

        //Set register button if event not free
        if (event?.isFree == true) {
            register_button.isEnabled = true
        }

    }

    private fun subscribeUi() {
        register_button.setOnClickListener {

        }
    }

    private fun checkRegisteredEvent() = launch {
        val isRegistered = viewModel.isRegisteredEvent(event?.id ?: "")
        if (isRegistered) {
            register_button.isEnabled = false
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