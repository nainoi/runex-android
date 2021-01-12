package com.think.runex.feature.workout

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.showToast
import com.think.runex.config.KEY_LOCATION
import com.think.runex.config.KEY_STATUS
import com.think.runex.config.NOTIFICATION_WORKOUT_CHANEL_ID
import com.think.runex.feature.location.LocationTracking
import com.think.runex.util.launchMainThread

open class WorkoutService : Service() {

    companion object {
        private const val PACKAGE_NAME = "com.think.runex.feature.workout"
        const val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"
    }

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private var changingConfiguration = false

    private val messenger = Messenger(IncomingHandler())

    private lateinit var notificationManager: NotificationManager

    private lateinit var locationTracking: LocationTracking

    private var lastLocation: Location? = null

    private var status: Int = WorkoutStatus.READY

    /**
     * Callback for changes in location.
     */
    private val locationCallback: LocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                super.onLocationResult(result)
                result?.lastLocation?.let { location ->
                    Logger.info(simpleName(), "On location update : latitude: ${location.latitude}, longitude: ${location.longitude}")
                    lastLocation = location
                    if (status == WorkoutStatus.READY || status == WorkoutStatus.PAUSE) {
                        senUpdateLocationBroadcast()
                    }
                }
            }
        }
    }

    override fun onCreate() {
        Logger.info(simpleName(), "onCreate")

        //Set up notification manager
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the channel for the notification
            val channel = NotificationChannel(NOTIFICATION_WORKOUT_CHANEL_ID, "Workout", NotificationManager.IMPORTANCE_DEFAULT)
            // Set the Notification Channel for the Notification Manager.
            notificationManager.createNotificationChannel(channel)
        }

        //Setup location tracking
        locationTracking = LocationTracking(this, locationCallback)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.info(simpleName(), "onStartCommand")
        return START_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changingConfiguration = true
    }

    override fun onBind(intent: Intent?): IBinder {
        // Called when a component (such as activity) comes to the foreground and binds with this service.
        Logger.info(simpleName(), "onBind")
        Logger.info(simpleName(), "Start request location update")
        locationTracking.requestLocationUpdates()
        changingConfiguration = false
        return messenger.binder
    }

    override fun onRebind(intent: Intent?) {
        // Called when a component (such as activity) returns to the foreground and binds once again with this service
        Logger.info(simpleName(), "onRebind")
        changingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        // Called when the last component (such as activity) unbinds from this service.
        // If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        Logger.error(simpleName(), "onUnbind")
        if (changingConfiguration.not() && isRunningInForeground().not()) {
            Logger.error(simpleName(), "Stop request location update")
            locationTracking.removeLocationUpdates()
        }
        return true // Ensures onRebind() is called when a client re-binds.
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.error(simpleName(), "onDestroy")
    }

    private fun startWorkingOut() {

    }


    private fun senUpdateLocationBroadcast() {
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(KEY_STATUS, status)
        intent.putExtra(KEY_LOCATION, lastLocation)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    /**
     * Returns true if this is a foreground service.
     */
    private fun isRunningInForeground(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    private inner class LocalBinder : Binder() {
        fun getService(): WorkoutService {
            return this@WorkoutService
        }
    }


    @SuppressLint("HandlerLeak")
    private inner class IncomingHandler : Handler() {
        override fun handleMessage(message: Message) {
            when (message.what) {
                //TODO("Do something when received the incoming message")
            }
        }
    }

}