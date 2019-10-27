package com.think.runex.ui


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.facebook.internal.CallbackManagerImpl
import com.jozzee.android.core.fragment.replaceFragment

import com.jozzee.android.core.simpleName
import com.jozzee.android.core.ui.content
import com.jozzee.android.core.utility.Logger
import com.think.runex.R
import com.think.runex.common.fadeIn
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.feature.social.SocialLoginListener
import com.think.runex.feature.social.SocialLoginManger
import com.think.runex.feature.social.SocialLoginManger.Companion.RC_GOOGLE_LOGIN
import com.think.runex.feature.social.SocialPlatform
import com.think.runex.feature.user.Profile
import com.think.runex.feature.user.UserProvider
import com.think.runex.java.Activities.BridgeFile
import com.think.runex.java.App.Configs
import com.think.runex.java.Constants.APIs
import com.think.runex.java.Constants.Constants
import com.think.runex.java.Constants.Globals
import com.think.runex.java.Utils.Network.NetworkProps
import com.think.runex.java.Utils.Network.NetworkUtils
import com.think.runex.java.Utils.Network.Request.rqLogin
import com.think.runex.java.Utils.Network.onNetworkCallback
import com.think.runex.utility.InjectorUtils
import kotlinx.android.synthetic.main.screen_login.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginScreen : ScreenFragment(), SocialLoginListener, onNetworkCallback {
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProviders.of(this, InjectorUtils.provideAuthViewModelFactory(context!!)).get(AuthViewModel::class.java)
    }
    private val socialLoginManger: SocialLoginManger by lazy { SocialLoginManger() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        props.addHeader("Authorization", "Bearer " + Globals.TOKEN)
        props.setJsonAsObject(rqLogin("fakespmh.21@gmail.com", "p@ss1234", Configs.PLATFORM))
        props.setUrl(APIs.LOGIN.VAL)

        // call api
        nw.postJSON(props, this)
    }

    private fun performLogin() = viewLifecycleOwner.lifecycleScope.launch {
        authViewModel.login(edt_email.content(), edt_password.content())?.also { profile ->
            replaceFragment(MainScreen(), fadeIn(), clearStack = true, addToBackStack = false)

        }
    }

    private fun isDataValid(): Boolean {
        return true
    }

    //  Interface Methods
    override fun onSuccess(jsonString: String?) {
        Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(jsonString: Exception?) {
        Toast.makeText(activity, "fail", Toast.LENGTH_SHORT).show()

    }

    override fun onLoginWithSocialCompleted(platform: Int, userProvider: UserProvider) {
        // prepare usage variables
        val tag: String = Constants.TAG.VAL

        Logger.info(tag, "Social login completed with: ${SocialPlatform.platformText(platform)}")
        Logger.info(tag, "User: $userProvider")

//        replaceFragment(MainScreen(), fadeIn(), clearStack = true, addToBackStack = false)

        // exit from this process
        activity!!.finish();

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
