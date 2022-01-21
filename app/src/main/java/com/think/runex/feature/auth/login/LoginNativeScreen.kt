package com.think.runex.feature.auth.login

import android.app.AlarmManager
import android.app.PendingIntent
import com.think.runex.feature.social.UserProvider
import com.think.runex.R
import android.content.Intent
import android.os.Bundle
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentActivity
import com.facebook.FacebookSdk
import com.facebook.FacebookSdk.getApplicationContext
import com.facebook.appevents.AppEventsLogger
import com.facebook.internal.CallbackManagerImpl.RequestCodeOffset
import com.google.android.material.textview.MaterialTextView
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.showDialog
import com.jozzee.android.core.view.visible
import com.think.runex.base.BaseScreen
import com.think.runex.config.RC_RESTART_APP
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.feature.main.MainScreen
import com.think.runex.feature.setting.EnvironmentDialog
import com.think.runex.feature.social.SocialLoginListener
import com.think.runex.feature.social.SocialLoginManger
import com.think.runex.feature.social.SocialPlatform
import com.think.runex.feature.social.SocialPlatform.Companion.platformText
import com.think.runex.util.AppPreference
import com.think.runex.util.extension.fadeIn
import com.think.runex.util.extension.getViewModel
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.screen_login.*
import kotlinx.android.synthetic.main.screen_login_native.*
import kotlinx.coroutines.delay
import java.lang.Exception
import java.util.ArrayList
import kotlin.system.exitProcess

class LoginNativeActivity : BaseScreen(), EnvironmentDialog.OnEnvironmentSelectedListener,
    SocialLoginListener, View.OnClickListener {
    /**
     * Main variables
     */
    private val ct = "LoginActivity->"

    // instance variables
    private var socialLoginManger: SocialLoginManger? = null
    private lateinit var viewModel: AuthViewModel

    //Layout
    private var loginLayout: RelativeLayout? = null

    // Button
    private var btnLoginFacebook: FrameLayout? = null
    private var btnLoginGoogle: FrameLayout? = null
    private var btnClose: AppCompatImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(AuthViewModel.Factory(requireContext()))
        socialLoginManger = SocialLoginManger()
        socialLoginManger!!.setSocialLoginListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_login_native, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    /**
     * Matching views
     */

    private fun setupComponents() {

    }

    private fun subscribeUi() {
        set_environment_button?.setOnClickListener {
            showDialog(EnvironmentDialog())
        }

//        viewModel.setOnHandleError(::errorHandler)
//        btn_close!!.setOnClickListener(this)
//        btn_login_with_facebook!!.setOnClickListener(this)
//        btn_login_with_google!!.setOnClickListener(this)
    }

    /**
     * View event listener
     */
    private fun viewEventListener() {
        btnClose!!.setOnClickListener(this)
        btnLoginFacebook!!.setOnClickListener(this)
        btnLoginGoogle!!.setOnClickListener(this)
    }

    /**
     * View on click
     */
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_login_with_facebook -> {
                // prepare usage variables
                val permissions: MutableList<String> = ArrayList()
                // update
                permissions.add("email")
                socialLoginManger!!.loginWithFacebook(this, permissions)
            }
            R.id.btn_login_with_google -> socialLoginManger!!.loginWithGoogle(this)
        }
    }

    /**
     * Implement methods
     */
    override fun onLoginWithSocialCompleted(platform: Int, userProvider: UserProvider) {
        // prepare usage variables
        performLogin(userProvider)
    }

    override fun onLoginWithSocialCancel() {}
    override fun onLoginWithSocialError(exception: Exception) {}

    private fun performLogin(provider: UserProvider) = launch {
        progress_bar?.visible()
        web_view?.inVisible()
        val isSuccess = viewModel.loginWithOpenID(provider)
        progress_bar?.gone()
        if (isSuccess) {
            replaceFragment(
                MainScreen.newInstance(),
                fadeIn(),
                addToBackStack = false,
                clearFragment = false
            )
        }
    }

    private fun performResult(requestCode: Int, resultCode: Int, data: Intent?) = launch {
        socialLoginManger!!.handleLogInResult(requestCode, resultCode, data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SocialLoginManger.RC_GOOGLE_LOGIN ||
            requestCode == RequestCodeOffset.Login.toRequestCode()
        ) {
            performResult(requestCode, resultCode, data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
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

            val pendingIntent = PendingIntent.getActivity(
                this,
                RC_RESTART_APP,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, pendingIntent)

            exitProcess(0)
        }
    }

    fun handleBackPressed(): Boolean {
        return false
    }

    override fun errorHandler(code: Int, message: String, tag: String?) {
        super.errorHandler(code, message, tag)
        progress_bar?.gone()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
        socialLoginManger!!.unRegisterLoginCallback()
        super.onDestroy()
    }
}
