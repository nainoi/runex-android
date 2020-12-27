package com.think.runex.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.jozzee.android.core.view.showDialog
import com.think.runex.R
import com.think.runex.common.getViewModel
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.event.EventViewModel
import com.think.runex.feature.event.EventViewModelFactory
import com.think.runex.feature.event.model.Event
import com.think.runex.ui.base.BaseActivity
import com.think.runex.config.KEY_EVENT
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.activity_event_preview.*
import kotlinx.android.synthetic.main.toolbar.*

class EventPreviewActivity : BaseActivity(), RegisterEventWithEBIBDialog.OnEBIBSpecifiedListener {

    private lateinit var viewModel: EventViewModel
    private var event: Event? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialValue()
        setContentView(R.layout.activity_event_preview)
        setupComponents()
        subscribeUi()

        //Load event preview
        register_button?.isEnabled = false
        progress_bar?.visible()
        web_view.loadUrl("${ApiConfig.PREVIEW_EVENT_URL}/${event?.id}")
    }

    private fun initialValue() {
        viewModel = getViewModel(EventViewModelFactory(this))

        //Get event id from intent
        event = intent?.getParcelableExtra(KEY_EVENT)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupComponents() {
        setStatusBarColor(Color.TRANSPARENT, NightMode.isNightMode(this).not())
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
                //After load event web view finish will check is registered event api.
                checkRegisteredEvent()
            }
        }

        //Set register button if event not free
        if (event?.isFree == true) {
            register_button.isEnabled = true
        }
    }

    private fun subscribeUi() {
        viewModel.setOnHandleError(::errorHandler)

        register_button.setOnClickListener {
            //TODO(Force enable to register when partner name is 'KAO' for now.")
            if (event?.partner?.partnerName == "KAO") {
                showDialog(RegisterEventWithEBIBDialog())
            }
        }
    }

    private fun checkRegisteredEvent() = launch {
        val isRegistered = viewModel.isRegisteredEvent(event?.id ?: "")
        register_button.isEnabled = isRegistered.not()
    }

    override fun onEBIBSpecified(eBib: String) {
        performRegisterEvent(eBib)
    }

    private fun performRegisterEvent(eBib: String) = launch {
        event?.also { event ->
            showProgressDialog(getString(R.string.register_event))
            val isSuccess = viewModel.registerEventWithKoa(event, eBib)
            hideProgressDialog()
            if (isSuccess) {
                finish()
            }
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