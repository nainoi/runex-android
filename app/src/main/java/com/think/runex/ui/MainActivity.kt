package com.think.runex.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.jozzee.android.core.connection.NetworkMonitor
import com.jozzee.android.core.connection.NetworkStatus
import com.jozzee.android.core.fragment.fragmentBackStackCount
import com.jozzee.android.core.fragment.fragmentCount
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.showToast
import com.think.runex.R
import com.think.runex.common.fadeIn
import com.think.runex.common.findFragment
import com.think.runex.common.getViewModel
import com.think.runex.feature.auth.*
import com.think.runex.ui.base.BaseActivity
import com.think.runex.ui.workout.record.WorkoutScreen
import com.think.runex.config.KEY_MESSAGE
import com.think.runex.config.KEY_SCREEN
import com.think.runex.config.RC_LOGIN
import com.think.runex.config.RC_OPEN_GPS
import com.think.runex.util.launch
import kotlinx.coroutines.delay

class MainActivity : BaseActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initial logger show log when is debug mode.
        //TODO("Force login to 'true' for development, Use default when build release.")
        Logger.isLogging = true//BuildConfig.DEBUG

        //Listener network connection.
        NetworkMonitor(this).observe(this, Observer(::onNetworkChanged))

        //Create view model
        authViewModel = getViewModel(AuthViewModelFactory(this))

        //Setup main fragment container and screen
        setContentView(R.layout.activity_main)
        mainFragmentContainerId = R.id.main_fragment_container

        if (savedInstanceState == null) {
            launch {
                delay(100)
                authViewModel.updateApiConfig()
                authViewModel.initialToken()
                setupScreen()
            }
        }
    }

    /**
     * When click notification while app is running
     * will be call [onNewIntent] check have a request screen?
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Logger.warning(simpleName(), "On new intent")
        when (intent?.getStringExtra(KEY_SCREEN)) {
            WorkoutScreen::class.java.simpleName -> gotoWorkoutScreen()
        }
    }

    private fun setupScreen() {
        when (TokenManager.isAlive()) {
            true -> gotoMainScreen(intent?.getStringExtra(KEY_SCREEN))
            false -> gotoOnBoardingScreen()
        }
    }

    private fun gotoMainScreen(initialScreen: String? = null) {
        //Add argument request screen when open app from notification
        replaceFragment(MainScreen.newInstance(initialScreen), fadeIn(), addToBackStack = false, clearFragment = false)
    }

    private fun gotoOnBoardingScreen() {
        replaceFragment(OnBoardingScreen(), fadeIn(), addToBackStack = false, clearFragment = false)
    }

    private fun gotoWorkoutScreen() {
        for (i in fragmentCount() - 1 downTo 0) {
            supportFragmentManager.fragments[i]?.also { fragment ->
                if (fragment is SupportRequestManagerFragment) {
                    return@also
                }
                if (fragment is MainScreen) {
                    fragment.forceToWorkoutScreen()
                    return
                } else {
                    gotoMainScreen(WorkoutScreen::class.java.simpleName)
                    return
                }
            }
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
                true -> gotoMainScreen()
                false -> data?.getStringExtra(KEY_MESSAGE)?.also {
                    showToast(it)
                }
            }
            RC_OPEN_GPS -> {
                /**
                 * Find [MainScreen] and send onActivityResult.
                 * [WorkoutScreen] it a only screen that request [RC_OPEN_GPS] for now,
                 * which that is child fragment of [MainScreen]
                 */
                findFragment<MainScreen>()?.onActivityResult(requestCode, resultCode, data)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
