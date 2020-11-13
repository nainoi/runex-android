package com.think.runex.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.webkit.*
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.getViewModel
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.feature.auth.AuthViewModelFactory
import com.think.runex.ui.base.BaseActivity
import com.think.runex.util.APP_SCHEME
import com.think.runex.util.FACE_USER_AGENT_FOR_WEB_VIEW
import com.think.runex.util.KEY_MESSAGE
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.activity_login2.*

class LoginActivity : BaseActivity() {

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialValue()
        setContentView(R.layout.activity_login2)
        setupComponents()
        subscribeUi()
    }

    private fun initialValue() {
        viewModel = getViewModel(AuthViewModelFactory(this))
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupComponents() {
        web_view?.settings?.apply {
            setAppCacheEnabled(false)
            cacheMode = WebSettings.LOAD_NO_CACHE
            javaScriptEnabled = true
            //Set face user agent for google sign in.
            userAgentString = FACE_USER_AGENT_FOR_WEB_VIEW
        }
        web_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress_bar?.gone()
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                request?.url?.also { uri ->
                    if (uri.scheme == APP_SCHEME) {
                        val parameters = request.url?.getQueryParameters("code")
                        if (parameters?.isNotEmpty() == true) {
                            performLogin(parameters[0])
                        }
                    } else if (uri.scheme == "auth.runex.co") {
                        web_view?.inVisible()
                    }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        web_view.loadUrl("${ApiConfig.LOGIN_URL}?device=android")
    }

    private fun subscribeUi() {
        viewModel.setOnHandleError(::errorHandler)
    }


    private fun performLogin(code: String) = launch {
        progress_bar?.visible()
        web_view?.inVisible()
        val result = viewModel.loginWithCode(this@LoginActivity, code)
        progress_bar?.gone()

        //Set result to MainActivity and finish
        when (result?.isSuccessful() == true) {
            true -> setResult(RESULT_OK)
            false -> setResult(RESULT_CANCELED, Intent().apply {
                putExtra(KEY_MESSAGE, result?.message ?: "")
            })
        }
        finish()
    }

    override fun onDestroy() {
        WebStorage.getInstance().deleteAllData()
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
        web_view?.clearCache(true)
        web_view?.clearFormData()
        web_view?.clearHistory()
        web_view?.clearSslPreferences()
        super.onDestroy()
    }
}

