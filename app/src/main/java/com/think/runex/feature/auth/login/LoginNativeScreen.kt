package com.think.runex.feature.auth.login

import android.app.AlarmManager
import android.app.PendingIntent
import com.think.runex.R
import android.content.Intent
import android.os.Bundle
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import com.facebook.internal.CallbackManagerImpl.RequestCodeOffset
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.showDialog
import com.jozzee.android.core.view.visible
import com.linecorp.linesdk.LoginDelegate
import com.linecorp.linesdk.LoginListener
import com.linecorp.linesdk.Scope
import com.linecorp.linesdk.auth.LineAuthenticationParams
import com.linecorp.linesdk.auth.LineLoginResult
import com.think.runex.base.BaseScreen
import com.think.runex.config.RC_RESTART_APP
import com.think.runex.databinding.ScreenLoginNativeBinding
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.feature.main.MainScreen
import com.think.runex.feature.setting.EnvironmentDialog
import com.think.runex.feature.social.*
import com.think.runex.util.AppPreference
import com.think.runex.util.extension.fadeIn
import com.think.runex.util.extension.getViewModel
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.screen_login.*
import kotlinx.coroutines.delay
import java.lang.Exception
import java.util.ArrayList
import kotlin.system.exitProcess
import androidx.annotation.NonNull

import android.R.string.no
import android.annotation.SuppressLint
import androidx.annotation.Nullable
import com.linecorp.linesdk.LineApiResponseCode
import com.linecorp.linesdk.auth.LineLoginApi
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleConfiguration
import com.willowtreeapps.signinwithapplebutton.SignInWithAppleResult
import android.content.pm.PackageManager

import android.content.pm.PackageInfo
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import com.facebook.AccessToken
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginNativeActivity : BaseScreen(), EnvironmentDialog.OnEnvironmentSelectedListener, SocialLoginListener {
    /**
     * Main variables
     */
    private val ct = "LoginActivity->"

    private val binding: ScreenLoginNativeBinding by lazy { ScreenLoginNativeBinding.inflate(layoutInflater) }

    // instance variables
    private var socialLoginManger: SocialLoginManger? = null
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(AuthViewModel.Factory(requireContext()))
        socialLoginManger = SocialLoginManger()
        socialLoginManger?.setSocialLoginListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            setupComponents()
        }
        subscribeUi()
    }

    /**
     * Matching views
     */
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("UseRequireInsteadOfGet")
    private fun setupComponents() {
        try {
            val info: PackageInfo = this.activity?.packageManager?.getPackageInfo(
                "com.think.runex",
                PackageManager.GET_SIGNATURES
            )!!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                for (signature in info.signatures){
                    val md: MessageDigest = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            } else {
                TODO("VERSION.SDK_INT < P")
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    private fun subscribeUi() {
        set_environment_button?.setOnClickListener {
            showDialog(EnvironmentDialog())
        }

        binding.btnClose.setOnClickListener {

        }

        binding.btnLoginWithFacebook.setOnClickListener {
            // prepare usage variables
            val permissions: MutableList<String> = ArrayList()
            // update
            permissions.add("email")
            socialLoginManger?.loginWithFacebook(this, permissions)
        }

        binding.btnLoginWithGoogle.setOnClickListener {
            socialLoginManger?.loginWithGoogle(this)
        }

        binding.btnLoginWithApple.setOnClickListener {
            socialLoginManger?.loginWithGoogle(this)
        }

        val configuration = SignInWithAppleConfiguration(
            clientId = "com.your.client.id.here",
            redirectUri = "https://your-redirect-uri.com/callback",
            scope = "email"
        )

        binding.signInWithAppleButton.setUpSignInWithAppleOnClick(this.childFragmentManager, configuration) { result ->
            when (result) {
                is SignInWithAppleResult.Success -> {
                    // Handle success
                }
                is SignInWithAppleResult.Failure -> {
                    // Handle failure
                }
                is SignInWithAppleResult.Cancel -> {
                    // Handle user cancel
                }
            }
        }

        //socialLoginManger?.setLineLogin(binding.lineLoginBtn, this)
        val lineCallDelegate = LoginDelegate.Factory.create()
        val params = LineAuthenticationParams.Builder()
            .scopes(listOf(Scope.PROFILE)) // .nonce("<a randomly-generated string>") // nonce can be used to improve security
            .build()
        val ch = getString(R.string.line_channel_id)
        binding.lineLoginBtn.setFragment(this)
        binding.lineLoginBtn.setChannelId(ch)
        binding.lineLoginBtn.enableLineAppAuthentication(true)
        binding.lineLoginBtn.setAuthenticationParams(params)
        binding.lineLoginBtn.setLoginDelegate(lineCallDelegate)
        val listener = object : LoginListener {
            override fun onLoginSuccess(result: LineLoginResult) {
                if (result.isSuccess) {
                    try {
                        val user = UserProviderCreate().apply {
                            providerID = result.lineProfile?.userId ?: ""
                            providerName = SocialPlatform.platformText(SocialPlatform.LINE)
                            firstName = result.lineProfile?.displayName ?: ""
                            lastName = ""
                            email = ""
                            avatarUrl = result.lineProfile?.pictureUrl.toString()
                        }
                        //loginListener?.onLoginWithSocialCompleted(SocialPlatform.LINE, user)


                    } catch (e: Exception) {
                        e.printStackTrace()
                        //loginListener?.onLoginWithSocialError(e)
                    }
                }
            }

            override fun onLoginFailure(result: LineLoginResult?) {
                //loginListener?.onLoginWithSocialError(Exception(result?.errorData?.message))
            }
        }
        binding.lineLoginBtn.addLoginListener(listener)

//        binding.lineLoginBtn.setOnClickListener {
//            socialLoginManger?.loginWithLine(it, this)
//        }
    }


    /**
     * Implement methods
     */
    override fun onLoginWithSocialCompleted(
        @SocialPlatform platform: Int,
        userProvider: UserProviderCreate
    ) {
        // prepare usage variables
        performLogin(userProvider)
    }

    override fun onLoginWithSocialCancel() {}
    override fun onLoginWithSocialError(exception: Exception) {}

    private fun performLogin(provider: UserProviderCreate) = launch {
        progress_bar?.visible()
        val isSuccess = viewModel.createUser(provider)
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
        socialLoginManger?.handleLogInResult(requestCode, resultCode, data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SocialLoginManger.RC_GOOGLE_LOGIN ||
            requestCode == RequestCodeOffset.Login.toRequestCode()
        ) {
            performResult(requestCode, resultCode, data)
        } else {
            if (requestCode == 1){
                val result = LineLoginApi.getLoginResultFromIntent(data)

                when (result.responseCode) {
                    LineApiResponseCode.SUCCESS -> {
                        val user = UserProviderCreate().apply {
                            providerID = result.lineProfile?.userId ?: ""
                            providerName = SocialPlatform.platformText(SocialPlatform.LINE)
                            firstName = result.lineProfile?.displayName ?: ""
                            lastName = ""
                            email = ""
                            avatarUrl = result.lineProfile?.pictureUrl.toString()
                        }

                        performLogin(user)

                    }
                    LineApiResponseCode.CANCEL ->             // Login canceled by user
                        Log.e("ERROR", "LINE Login Canceled by user.")
                    else -> {
                        // Login canceled due to other error
                        Log.e("ERROR", "Login FAILED!")
                        Log.e("ERROR", result.errorData.toString())
                    }
                }
            }
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

    override fun onDestroy() {
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
        socialLoginManger!!.unRegisterLoginCallback()
        super.onDestroy()
    }
}
