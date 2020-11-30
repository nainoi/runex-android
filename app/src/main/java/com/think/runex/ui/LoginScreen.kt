package com.think.runex.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.facebook.internal.CallbackManagerImpl
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.think.runex.R
import com.think.runex.common.getViewModel
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.feature.auth.AuthViewModelFactory
import com.think.runex.feature.auth.TokenManager
import com.think.runex.feature.social.SocialLoginListener
import com.think.runex.feature.social.SocialLoginManger
import com.think.runex.feature.social.SocialLoginManger.Companion.RC_GOOGLE_LOGIN
import com.think.runex.feature.social.SocialPlatform
import com.think.runex.feature.user.UserProvider
import com.think.runex.java.App.Configs
import com.think.runex.java.Constants.APIs
import com.think.runex.java.Constants.Constants
import com.think.runex.java.Constants.Globals
import com.think.runex.java.Utils.Network.NetworkProps
import com.think.runex.java.Utils.Network.NetworkUtils
import com.think.runex.java.Utils.Network.Request.rqLogin
import com.think.runex.java.Utils.Network.Response.xResponse
import com.think.runex.java.Utils.Network.onNetworkCallback
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.ANDROID
import com.think.runex.util.AppPreference
import kotlinx.android.synthetic.main.screen_login.*
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginScreen : BaseScreen(), SocialLoginListener, onNetworkCallback {

    private lateinit var authViewModel: AuthViewModel
    private val socialLoginManger: SocialLoginManger by lazy { SocialLoginManger() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        authViewModel = getViewModel(AuthViewModelFactory(requireContext()))

        //Initial social login manage.
        socialLoginManger.setSocialLoginListener(this)
        socialLoginManger.registerLoginCallback()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupComponents()
        subscribeUi()
    }

    private fun setupComponents() {
        //TODO("mock up user, email")
        edt_email.setText("jozzeezaadee@gmail.com")
        edt_password.setText("12345678")
    }

    private fun subscribeUi() {
        btn_login_with_facebook.setOnClickListener {
            socialLoginManger.loginWithFacebook(this)
        }

        btn_login_with_google.setOnClickListener {
            socialLoginManger.loginWithGoogle(this)
        }

        btn_login.setOnClickListener {
            if (isDataValid()) {
                // request login
                apiLogin()
            }
        }
    }


    /** API methods  */
    private fun apiLogin() {
        val nw = NetworkUtils.newInstance(activity)
        val props = NetworkProps()

        // update props
        props.addHeader("Authorization", TokenManager.accessToken)
        props.setJsonAsObject(rqLogin("fakespmh.21@gmail.com", "p@ss1234", ANDROID))
        props.setUrl(APIs.LOGIN.VAL)

        // call api
        nw.postJSON(props, this)
    }

    private fun performLogin() = viewLifecycleOwner.lifecycleScope.launch {
//        authViewModel.login(edt_email.content(), edt_password.content())?.also { profile ->
//            replaceFragment(MainScreen(), fadeIn(), clearFragment = true, addToBackStack = false)
//
//        }
    }

    private fun isDataValid(): Boolean {
        return true
    }

    private fun resultCallback() {
        requireActivity().setResult(Activity.RESULT_OK)
    }

    //  Interface Methods
    override fun onSuccess(jsonString: xResponse?) {
//        Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
//
//        // activity result
//        activity!!.setResult( Activity.RESULT_OK );


    }

    override fun onFailure(jsonString: xResponse?) {
//        Toast.makeText(activity, "fail", Toast.LENGTH_SHORT).show()
//
//        // activity result
//        activity!!.setResult( Activity.RESULT_CANCELED );

    }

    override fun onLoginWithSocialCompleted(platform: Int, userProvider: UserProvider) {
        // prepare usage variables
        val tag: String = Constants.TAG.VAL

        Logger.info(tag, "Social login completed with: ${SocialPlatform.platformText(platform)}")
        Logger.info(tag, "User: $userProvider")

//        replaceFragment(MainScreen(), fadeIn(), clearStack = true, addToBackStack = false)

        // activity result
        requireActivity().setResult(Activity.RESULT_OK);

        // exit from this process
        requireActivity().finish()

        // result
//        resultCallback()

        // Bridge file activity
//        val intent = Intent(context, BridgeFile::class.java)
//        startActivity( intent );

    }

    override fun onLoginWithSocialCancel() {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLoginWithSocialError(exception: Exception) {
        Logger.error(simpleName(), "Social login error: ${exception.message}")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_GOOGLE_LOGIN ||
                requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
            socialLoginManger.handleLogInResult(requestCode, resultCode, data)

        } else {
            super.onActivityResult(requestCode, resultCode, data)

        }
    }

    override fun onDestroy() {
        socialLoginManger.unRegisterLoginCallback()
        super.onDestroy()
    }

}
