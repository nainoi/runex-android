package com.think.runex.feature.auth.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.visible
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.common.fadeIn
import com.think.runex.common.getViewModel
import com.think.runex.common.setStatusBarColor
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.feature.auth.AuthViewModelFactory
import com.think.runex.base.BaseScreen
import com.think.runex.config.FACE_USER_AGENT_FOR_WEB_VIEW
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.main.MainScreen
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_login.*

class LoginScreen : BaseScreen() {

    private lateinit var viewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(AuthViewModelFactory(requireContext()))

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = true)

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
                    if (uri.scheme == BuildConfig.APP_SCHEME) {
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
        val isSuccess = viewModel.loginWithCode(requireContext(), code)
        progress_bar?.gone()
        if (isSuccess) {
            replaceFragment(MainScreen.newInstance(), fadeIn(), addToBackStack = false, clearFragment = false)
        }
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        progress_bar?.gone()
    }

    override fun onDestroyView() {
        web_view?.clearCache(true)
        web_view?.clearFormData()
        web_view?.clearHistory()
        web_view?.clearSslPreferences()
        super.onDestroyView()
    }

    override fun onDestroy() {
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
        super.onDestroy()
    }

}
