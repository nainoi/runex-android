package com.think.runex.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.lifecycleScope
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.getViewModel
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.*
import com.think.runex.java.App.App
import com.think.runex.java.App.AppEntity
import com.think.runex.java.Models.TokenObject
import com.think.runex.java.Models.UserObject
import com.think.runex.ui.base.BaseActivity
import com.think.runex.util.APP_SCHEME
import com.think.runex.util.AppPreference
import com.think.runex.util.KEY_MESSAGE
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.activity_login2.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class LoginActivity : BaseActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = getViewModel(AuthViewModel::class.java, AuthViewModelFactory(this))

        setContentView(R.layout.activity_login2)
        setupComponents()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupComponents() {
        web_view?.settings?.apply {
            setAppCacheEnabled(false)
            cacheMode = WebSettings.LOAD_NO_CACHE
            javaScriptEnabled = true
            //Set face user agent for google sign in.
            userAgentString = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19"
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
        web_view.loadUrl("${ApiConfig.AUTH_URL}/login?device=android")
    }


    private fun performLogin(code: String) = launch {
        progress_bar?.visible()
        web_view?.inVisible()
        val result = authViewModel.loginWithCode(this@LoginActivity, code)
        progress_bar?.gone()

        //Set result to MainActivity and finish
        when (result?.isSuccessful() == true) {
            true -> setResult(RESULT_OK)
            false -> setResult(RESULT_CANCELED, Intent().apply {
                putExtra(KEY_MESSAGE, result?.errorMessage() ?: "")
            })
        }
        finish()
    }
}

