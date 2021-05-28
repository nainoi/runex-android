package com.think.runex.feature.auth.login

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.view.*
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.config.FACE_USER_AGENT_FOR_WEB_VIEW
import com.think.runex.config.RC_RESTART_APP
import com.think.runex.datasource.api.ApiConfig
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.feature.main.MainScreen
import com.think.runex.feature.setting.EnvironmentDialog
import com.think.runex.util.AppPreference
import com.think.runex.util.extension.fadeIn
import com.think.runex.util.extension.getViewModel
import com.think.runex.util.extension.launch
import com.think.runex.util.extension.setStatusBarColor
import kotlinx.android.synthetic.main.screen_login.*
import kotlinx.coroutines.delay
import kotlin.system.exitProcess


class LoginScreen : BaseScreen(), EnvironmentDialog.OnEnvironmentSelectedListener {

    private lateinit var viewModel: AuthViewModel

    //private var webViewPopup: WebView? = null

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
            javaScriptEnabled = true
            setSupportMultipleWindows(true)
            //setAppCacheEnabled(false)
            cacheMode = WebSettings.LOAD_NO_CACHE
            //Set face user agent for google sign in.
            userAgentString = FACE_USER_AGENT_FOR_WEB_VIEW
        }

        CookieManager.getInstance().setAcceptThirdPartyCookies(web_view, true)
        //Cookie manager for the webview
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)

        web_view?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                progress_bar?.gone()

                if (url?.contains("facebook") == true
                        || url?.contains("google") == true
                        || url?.contains("apple") == true) {
                    set_environment_button?.gone()
                } else if (BuildConfig.DEBUG && url?.contains("login?device=android") == true) {
                    set_environment_button?.visible()
                }


                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {

                    val uri = Uri.parse(url)

                    if (uri?.scheme == BuildConfig.APP_SCHEME) {
                        val parameters = uri.getQueryParameters("code")
                        if (parameters?.isNotEmpty() == true) {
                            performLogin(parameters[0])
                        }
                    }
                }
            }

            //@RequiresApi(Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

                val uri = request?.url

                if (uri?.scheme == BuildConfig.APP_SCHEME) {
                    val parameters = uri.getQueryParameters("code")
                    if (parameters?.isNotEmpty() == true) {
                        performLogin(parameters[0])
                    }
                } else if (uri?.scheme == "auth.runex.co") {
                    web_view?.inVisible()
                }

                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N && uri?.host == "m.facebook.com") {
                    web_view?.loadUrl(uri.toString())
                    return false
                }

                return when (uri?.toString()?.startsWith("http") == true || uri?.toString()?.startsWith("https") == true) {
                    true -> super.shouldOverrideUrlLoading(view, request)
                    false -> true
                }
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
            }
        }
        web_view.loadUrl("${ApiConfig.LOGIN_URL}?device=android")
    }

    private fun subscribeUi() {

        set_environment_button?.setOnClickListener {
            showDialog(EnvironmentDialog())
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

    fun handleBackPressed(): Boolean {
        if (web_view?.canGoBack() == true) {
            web_view?.goBack()
            return true
        }
        return false
    }

    override fun errorHandler(code: Int, message: String, tag: String?) {
        super.errorHandler(code, message, tag)
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
