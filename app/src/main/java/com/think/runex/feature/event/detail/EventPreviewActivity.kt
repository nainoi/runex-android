package com.think.runex.feature.event.detail

import android.os.Bundle
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.util.extension.getViewModel
import com.think.runex.feature.event.data.EventItem
import com.think.runex.base.BaseActivity
import kotlinx.android.synthetic.main.activity_event_preview.*

class EventPreviewActivity : BaseActivity(), RegisterEventWithEBIBDialog.OnEBIBSpecifiedListener {

    private lateinit var viewModel: EventDetailsViewModel
    private var event: EventItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialValue()
        setContentView(R.layout.activity_event_preview)
        //setupComponents()
        //subscribeUi()

        //Load event preview
        register_button?.isEnabled = false
        progress_bar?.visible()
        //web_view.loadUrl("${ApiConfig.PREVIEW_EVENT_URL}/${event?.id}")
    }

    private fun initialValue() {
        viewModel = getViewModel(EventDetailsViewModel.Factory(this))

        //Get event id from intent
        //event = intent?.getParcelableExtra(KEY_EVENT)
    }

//    @SuppressLint("SetJavaScriptEnabled")
//    private fun setupComponents() {
//        setStatusBarColor(Color.TRANSPARENT, NightMode.isNightMode(this).not())
//        setupToolbar(toolbar, R.string.detail, R.mipmap.ic_navigation_back)
//
//        //Setup view view
//        web_view?.settings?.apply {
//            setAppCacheEnabled(false)
//            cacheMode = WebSettings.LOAD_NO_CACHE
//            javaScriptEnabled = true
//        }
//        web_view.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                progress_bar?.gone()
//                //After load event web view finish will check is registered event api.
//                checkRegisteredEvent()
//            }
//        }
//
//        //Set register button if event not free
//        if (event?.isFree == true) {
//            register_button.isEnabled = true
//        }
//    }
//
//    private fun subscribeUi() {
//        viewModel.setOnHandleError(::errorHandler)
//
//        register_button.setOnClickListener {
//            //TODO(Force enable to register when partner name is 'KAO' for now.")
//            if (event?.partner?.partnerName == "KAO") {
//                showDialog(RegisterEventWithEBIBDialog())
//            }
//        }
//    }
//
//    private fun checkRegisteredEvent() = launch {
//        val isRegistered = viewModel.isRegisteredEvent(event?.id ?: "")
//        register_button.isEnabled = isRegistered.not()
//    }
//
    override fun onEBIBSpecified(eBib: String) {
        TODO("Disable for now")
        //performRegisterEvent(eBib)
    }
//
//    private fun performRegisterEvent(eBib: String) = launch {
//        event?.also { event ->
//            showProgressDialog(getString(R.string.register_event))
//            val isSuccess = viewModel.registerEventWithKoa(event, eBib)
//            hideProgressDialog()
//            if (isSuccess) {
//                finish()
//            }
//        }
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        Logger.info(simpleName(), "On options item selected id: ${item.itemId}, title: ${item.title}")
//        if (item.itemId == android.R.id.home /*&& fragmentBackStackCount() > 0*/) {
//            onBackPressed()
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
}