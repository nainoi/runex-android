package com.think.runex

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jozzee.android.core.connection.NetworkMonitor
import com.jozzee.android.core.connection.NetworkStatus
import com.jozzee.android.core.fragment.fragmentBackStackCount
import com.jozzee.android.core.fragment.fragmentCount
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.showToast
import com.think.runex.base.BaseActivity
import com.think.runex.config.KEY_SCREEN
import com.think.runex.config.RC_OPEN_GPS
import com.think.runex.config.RC_UPDATE_APP
import com.think.runex.feature.SplashScreen
import com.think.runex.feature.auth.*
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.feature.auth.login.LoginNativeActivity
import com.think.runex.feature.auth.login.LoginScreen
import com.think.runex.feature.event.registration.RegistrationScreen
import com.think.runex.feature.main.MainScreen
import com.think.runex.feature.workout.record.WorkoutScreen
import com.think.runex.util.extension.fadeIn
import com.think.runex.util.extension.findFragment
import com.think.runex.util.extension.getViewModel
import com.think.runex.util.extension.launch
import kotlinx.coroutines.delay

class MainActivity : BaseActivity(), InstallStateUpdatedListener {

    private lateinit var authViewModel: AuthViewModel

    private val appUpdateManager by lazy {
        AppUpdateManagerFactory.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initial logger show log when is debug mode.
        //TODO("Force login to 'true' for development, Use default when build release.")
        Logger.isLogging = true//BuildConfig.DEBUG

        //Listener network connection.
        NetworkMonitor(this).observe(this, Observer(::onNetworkChanged))

        //Create view model
        authViewModel = getViewModel(AuthViewModel.Factory(this))

        //Setup main fragment container and screen
        setContentView(R.layout.activity_main)
        mainFragmentContainerId = R.id.main_fragment_container

        //ClientApis.initial(this)

        if (savedInstanceState == null) {
            launch {
                gotoSplashScreen()
                authViewModel.updateAppConfig()
                authViewModel.initialToken()
                delay(800)
                setupScreen()
                checkForUpdateApp()
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

    override fun onResume() {
        super.onResume()
        checkUpdateAppStatus()
    }

    private fun setupScreen() {
        when (TokenManager.isAlive()) {
            true -> gotoMainScreen(intent?.getStringExtra(KEY_SCREEN))
            false -> gotoLoginScreen()
        }
    }

    private fun gotoLoginScreen() {
        replaceFragment(LoginNativeActivity(), addToBackStack = false, clearFragment = false)
    }

    private fun gotoSplashScreen() {
        replaceFragment(SplashScreen(), fadeIn(), addToBackStack = false, clearFragment = true)
    }

    private fun gotoMainScreen(initialScreen: String? = null) {
        //Add argument request screen when open app from notification
        replaceFragment(MainScreen.newInstance(initialScreen), fadeIn(), addToBackStack = false, clearFragment = true)
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
        when (val topFragment = getTopFragment()) {
            is RegistrationScreen -> {
                topFragment.onClickBackPressed()
                return
            }
            is LoginScreen -> if(topFragment.handleBackPressed()){
                return
            }
        }
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
            RC_OPEN_GPS -> {
                /**
                 * Find [MainScreen] and send onActivityResult.
                 * [WorkoutScreen] it a only screen that request [RC_OPEN_GPS] for now,
                 * which that is child fragment of [MainScreen]
                 */
                findFragment<MainScreen>()?.onActivityResult(requestCode, resultCode, data)
            }
            RC_UPDATE_APP -> when (resultCode) {
                Activity.RESULT_CANCELED -> showToast(R.string.cancel_update_app)
                ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> showToast(R.string.update_app_failed)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun checkForUpdateApp() {
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    onStartUpdateFlow(appUpdateInfo, AppUpdateType.FLEXIBLE)
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    onStartUpdateFlow(appUpdateInfo, AppUpdateType.IMMEDIATE)
                }
            }

        }.addOnFailureListener {
            FirebaseCrashlytics.getInstance().recordException(it)
        }
    }

    private fun checkUpdateAppStatus() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->

            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                popupSnackbarForCompleteUpdate()
            }

            try {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    //If an in-app update is already running, resume the update.
                    onStartUpdateFlow(appUpdateInfo, AppUpdateType.IMMEDIATE)
                }
            } catch (e: IntentSender.SendIntentException) {
                e.printStackTrace()
            }
        }
    }

    private fun onStartUpdateFlow(appUpdateInfo: AppUpdateInfo, @AppUpdateType updateType: Int) {
        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, updateType, this, RC_UPDATE_APP)
        appUpdateManager.registerListener(this)
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            popupSnackbarForCompleteUpdate()
        } else if (state.installStatus() == InstallStatus.INSTALLED) {
            showToast(R.string.update_app_success)
            appUpdateManager.unregisterListener(this@MainActivity)
        }
    }

    // Displays the snackbar notification and call to action.
    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(findViewById(R.id.main_fragment_container),
                R.string.update_app_just_downloaded,
                Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(R.string.restart) {
                appUpdateManager.completeUpdate()
                appUpdateManager.unregisterListener(this@MainActivity)
            }
            setActionTextColor(ContextCompat.getColor(this@MainActivity, R.color.colorPrimary))
            show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(this)
    }

}
