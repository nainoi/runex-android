package com.think.runex.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.jozzee.android.core.connection.NetworkMonitor
import com.jozzee.android.core.connection.NetworkStatus
import com.jozzee.android.core.fragment.FragmentContainer
import com.jozzee.android.core.fragment.fragmentBackStackEntryCount
import com.jozzee.android.core.fragment.replaceFragment
import com.jozzee.android.core.getConnectivityManager
import com.jozzee.android.core.simpleName
import com.jozzee.android.core.utility.Logger
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.common.fadeIn
import com.think.runex.common.getCurrentNightMode
import com.think.runex.ui.MainScreen
import com.think.runex.ui.SplashScreen
import com.think.runex.utility.LocalManager

class MainActivity : AppCompatActivity() {

    private var backPressedEnabled = true

    //override fun attachBaseContext(newBase: Context?) {
    //    super.attachBaseContext(LocalManager.Wrapper(newBase).wrapLanguage(LocalManager.KEY_EN))
    //}

    override fun onCreate(savedInstanceState: Bundle?) {

        //Initial logger show log when is debug mode.
        Logger.isLogging = BuildConfig.DEBUG

        //Set up theme (use light mode or dark mode)
        delegate.localNightMode = getCurrentNightMode()

        //Initial language.
        LocalManager.getInstance().initialLanguage()

        //Listener network connection.
        NetworkMonitor(getConnectivityManager()).observe(this, Observer(::onNetworkChanged))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FragmentContainer.id = R.id.main_fragment_container

        replaceFragment(SplashScreen(), fadeIn(), clearStack = true, addToBackStack = false)
    }

    override fun onBackPressed() {
        if (backPressedEnabled) {
            Logger.warning(simpleName(), "onBackPressed")
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home && fragmentBackStackEntryCount() > 0) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun isBackPressedEnabled() = backPressedEnabled

    fun setBackPressedEnabled(backPressedEnabled: Boolean) {
        this.backPressedEnabled = backPressedEnabled
    }

    private fun onNetworkChanged(status: NetworkStatus) {
        Logger.warning(simpleName(), "Network Changed: $status")
        //TODO("Update ui or logic when network changed.")
    }
}
