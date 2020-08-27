package com.think.runex.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.jozzee.android.core.connection.NetworkMonitor
import com.jozzee.android.core.connection.NetworkStatus
import com.jozzee.android.core.fragment.FragmentContainer
import com.jozzee.android.core.fragment.fragmentBackStackCount
import com.jozzee.android.core.fragment.replaceFragment
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.common.fadeIn
import com.think.runex.common.getCurrentNightMode
import com.think.runex.java.Activities.BridgeFile
import com.think.runex.java.App.App
import com.think.runex.java.App.AppEntity
import com.think.runex.java.Utils.ActivityUtils
import com.think.runex.java.Utils.L
import com.think.runex.ui.SplashScreen
import com.think.runex.utility.LocalManager

class MainActivity : AppCompatActivity() {

    private val ct = "MainActivity->"
    private var backPressedEnabled = true

    //override fun attachBaseContext(newBase: Context?) {
    //    super.attachBaseContext(LocalManager.Wrapper(newBase).wrapLanguage(LocalManager.KEY_EN))
    //}

    override fun onCreate(savedInstanceState: Bundle?) {
        // prepare usage variables
        val mtn = ct +"onCreateView() ";

        //Initial logger show log when is debug mode.
        Logger.isLogging = BuildConfig.DEBUG

        //Set up theme (use light mode or dark mode)
        delegate.localNightMode = getCurrentNightMode()

        //Initial language.
        LocalManager.getInstance().initialLanguage()

        //Listener network connection.
        NetworkMonitor(this).observe(this, Observer(::onNetworkChanged))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FragmentContainer.id = R.id.main_fragment_container

        // fullscreen display
        ActivityUtils.newInstance(this).fullScreen();
        //-->
        val appEntity: AppEntity = App.instance(this).appEntity;
        L.i(  "{$mtn} App-entity: {$appEntity}")

        if(appEntity.token.isAlive && appEntity.isLoggedIn){
            // go to bridge file
            val intent = Intent(this, BridgeFile::class.java);

            // new page
            startActivity(intent);

            // exit from this page
            finish()

        } else replaceFragment(SplashScreen(), fadeIn(), clearFragment = true, addToBackStack = false)


    }

    override fun onBackPressed() {
        if (backPressedEnabled) {
            Logger.warning(simpleName(), "onBackPressed")
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home && fragmentBackStackCount() > 0) {
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
