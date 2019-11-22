package com.think.runex.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.jozzee.android.core.fragment.replaceFragment
import com.think.runex.R
import com.think.runex.common.fadeIn
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.feature.auth.TokenManager
import com.think.runex.java.Activities.BridgeFile
import com.think.runex.java.Activities.LoginActivity
import com.think.runex.java.App.App
import com.think.runex.java.Constants.Globals
import com.think.runex.java.Pages.MainPage
import com.think.runex.java.Utils.L
import com.think.runex.utility.InjectorUtils
import kotlinx.android.synthetic.main.screen_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : ScreenFragment() {
    private val ct = "SplashScreen->";

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        btn_get_started.setOnClickListener {
            loginPage()
        }
    }

    /** Feature methods */
    fun loginPage() {
        val intent = Intent(context, LoginActivity::class.java);
        startActivityForResult(intent, Globals.RC_NEED_LOGIN);
    }

    fun bridgeFile() {
        val intent = Intent(context, BridgeFile::class.java);
        startActivity(intent);
    }

    /** Life cycle */
    override fun onStart() {
        super.onStart()

//        authViewModel.initialToken()

//        viewLifecycleOwner.lifecycleScope.launch {
//            delay(1000)

//        // prepare usage variables
//        val app = App.instance(activity);
//
//        // print token info
//        app.appEntity.token.printInfo();
//
//        // go to main page
//        activity!!.finish();

        // go to bridge file
//        bridgeFile()

//            // does token available
//            if (app.appEntity.token.expiredLong <= 0) {
//                // exit from this process
//                activity!!.finish()
//
//                // go to login page
//                loginPage()
//
////                replaceFragment(LoginScreen(),
////                        fadeIn(), clearStack = true, addToBackStack = false)
//
//            } else {
//                // exit from this process
//                activity!!.finish();
//
//                // go to main page
//                bridgeFile();
//
//            }


//            if (TokenManager.isAlive()) {
//                // pop out
//                activity!!.supportFragmentManager.popBackStack();
//
//                // go to main page
//                mainPage()
//
//
//            } else replaceFragment( LoginScreen(),
//                    fadeIn(), clearStack = true, addToBackStack = false)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // login successfully
        if( requestCode == Globals.RC_NEED_LOGIN && resultCode == Activity.RESULT_OK ) {
            // go to bridge file
            bridgeFile()

            // exit from this page
            activity!!.finish()
        }

    }
}
