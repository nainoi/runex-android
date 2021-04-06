package com.think.runex.feature.auth.login

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.core.content.IntentCompat
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.view.*
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.util.extension.fadeIn
import com.think.runex.util.extension.getViewModel
import com.think.runex.util.extension.setStatusBarColor
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.base.BaseScreen
import com.think.runex.config.FACE_USER_AGENT_FOR_WEB_VIEW
import com.think.runex.config.RC_RESTART_APP
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.main.MainScreen
import com.think.runex.feature.setting.SelectEnvironmentDialog
import com.think.runex.util.AppPreference
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.screen_login.*
import kotlinx.coroutines.delay
import kotlin.system.exitProcess

class LoginScreen : BaseScreen(), SelectEnvironmentDialog.OnEnvironmentSelectedListener {

    private lateinit var viewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(AuthViewModel.Factory(requireContext()))

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
        setStatusBarColor(getColor(R.color.backgroundPrimaryLight), isLightStatusBar = true)

        set_environment_button?.setVisible(BuildConfig.DEBUG)

        web_view?.settings?.apply {
            //setAppCacheEnabled(false)
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

        set_environment_button?.setOnClickListener {
            showDialog(SelectEnvironmentDialog())
        }

        viewModel.setOnHandleError(::errorHandler)
    }

    private fun performLogin(code: String) = launch {
        progress_bar?.visible()
        web_view?.inVisible()
        val isSuccess = viewModel.loginWithCode(code)
        progress_bar?.gone()
        if (isSuccess) {
            replaceFragment(MainScreen.newInstance(), fadeIn(), addToBackStack = false, clearFragment = false)
        }
    }

    override fun onEnvironmentSelected(environment: Int) {
        if (environment != AppPreference.getEnvironment(requireContext())) {
            AppPreference.setEnvironment(requireContext(), environment)
            launch {
                delay(100)
                requireActivity().finish()
                restartApplication()
            }
        }
    }

    private fun restartApplication() {
        requireContext().run {

            val intent = packageManager.getLaunchIntentForPackage(packageName) ?: return@run
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent = PendingIntent.getActivity(this, RC_RESTART_APP, intent, PendingIntent.FLAG_CANCEL_CURRENT)

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent)

            exitProcess(0)
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
