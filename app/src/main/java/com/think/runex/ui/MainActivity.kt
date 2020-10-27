package com.think.runex.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.jozzee.android.core.connection.NetworkMonitor
import com.jozzee.android.core.connection.NetworkStatus
import com.jozzee.android.core.fragment.fragmentBackStackCount
import com.jozzee.android.core.fragment.replaceFragment
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.showToast
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.common.fadeIn
import com.think.runex.common.getViewModel
import com.think.runex.feature.auth.*
import com.think.runex.java.Activities.BridgeFile
import com.think.runex.ui.base.BaseActivity
import com.think.runex.util.KEY_MESSAGE
import com.think.runex.util.RC_LOGIN

class MainActivity : BaseActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initial logger show log when is debug mode.
        Logger.isLogging = BuildConfig.DEBUG

        //Listener network connection.
        NetworkMonitor(this).observe(this, Observer(::onNetworkChanged))

        //Create view model
        authViewModel = getViewModel(AuthViewModel::class.java, AuthViewModelFactory(this))

        //Setup main fragment container and screen
        setContentView(R.layout.activity_main)
        mainFragmentContainerId = R.id.main_fragment_container

        if (savedInstanceState == null) {
            authViewModel.initialToken()
            setupScreen()
        }
    }

    private fun setupScreen() {
        when (TokenManager.isAlive()) {
            true -> goToHomePageWithJavaStyle()
            false -> replaceFragment(fragment = OnBoardingScreen(),
                    containerViewId = R.id.main_fragment_container,
                    animations = fadeIn(),
                    clearFragment = true,
                    addToBackStack = false)
        }
    }

    override fun onBackPressed() {
        Logger.warning(simpleName(), "onBackPressed")
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home && fragmentBackStackCount() > 0) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onNetworkChanged(status: NetworkStatus) {
        Logger.warning(simpleName(), "Network Changed: $status")
        //TODO("Update ui or logic when network changed.")
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_LOGIN -> when (resultCode == RESULT_OK) {
                true -> goToHomePageWithJavaStyle()
                false -> data?.getStringExtra(KEY_MESSAGE)?.also {
                    showToast(it)
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    //TODO("Use old to java style")
    private fun goToHomePageWithJavaStyle() {
        val intent = Intent(this@MainActivity, BridgeFile::class.java);
        startActivity(intent)
        finish()
    }
}
