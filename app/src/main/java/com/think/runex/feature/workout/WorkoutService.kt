package com.think.runex.feature.workout

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.think.runex.R
import com.think.runex.config.*
import com.think.runex.feature.location.LocationTracking
import com.think.runex.feature.workout.data.*
import com.think.runex.MainActivity
import com.think.runex.feature.workout.db.LocationDao
import com.think.runex.feature.workout.db.WorkoutDataBase
import com.think.runex.feature.workout.record.WorkoutScreen
import com.think.runex.util.extension.launch
import kotlinx.coroutines.Dispatchers.IO
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

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

    private var scheduler: ScheduledExecutorService? = null

    private val workoutMessenger = Messenger(IncomingHandler())

    private var notificationManager: NotificationManager? = null

    private lateinit var locationTracking: LocationTracking
    private var newLocation: Location? = null
    private var locationDao: LocationDao? = null

    private var record: WorkingOutRecord? = null
    private var status: Int = WorkoutStatus.UNKNOWN
    private var lastUpdateTimeMillis: Long = 0
    private var lastUpdateLocation: Location? = null

    /**
     * Callback for changes in location.
     */
    private val locationCallback: LocationCallback by lazy {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.lastLocation.let { location ->
                    //Logger.info("WorkoutService", "Location update : latitude: ${location.latitude}, longitude: ${location.longitude}, accuracy: ${location.accuracy} at time: ${location.time}")
                    newLocation = location
                    if (status == WorkoutStatus.UNKNOWN) {
                        status = WorkoutStatus.READY
                    }
                    if (status == WorkoutStatus.READY || status == WorkoutStatus.PAUSE) {
                        broadcastWorkingOutUpdate(newLocation)
                    }
                }
            }
        }
    }

    override fun onCreate() {
        Logger.info(simpleName(), "onCreate")
        //Setup location tracking
        locationTracking = LocationTracking(this, locationCallback)

        //broadcast initial status
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(KEY_STATUS, status)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.info(simpleName(), "onStartCommand, startId: $startId")
        //Check click action from notification
        when (intent?.getIntExtra(KEY_ACTION, 0)) {
            WorkoutAction.PAUSE -> onPauseWorkingOut()
            WorkoutAction.RESUME -> onResumeWorkingOut()
        }
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
        return workoutMessenger.binder
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
        if (locationDao != null) {
            launch(IO) {
                locationDao?.deleteAllLocation()
                locationDao = null
                WorkoutDataBase.destroy()
            }
        }
    }

    private fun onStartWorkingOut(type: String) {
        if (isRunningInForeground()) return
        locationDao = WorkoutDataBase.getDatabase(this).locationDao()
        clearTempLocation()
        setupNotificationManager()
        record = WorkingOutRecord(type, System.currentTimeMillis())
        status = WorkoutStatus.WORKING_OUT
        startService(Intent(applicationContext, WorkoutService::class.java))
        startForeground(NOTIFICATION_WORKOUT_ID, getNotification())
        startScheduledThread()
    }

    private fun onPauseWorkingOut() {
        //Log.d(simpleName(), "Action Pause")
        status = WorkoutStatus.PAUSE
        stopScheduledThread()
        updateWorkingOutRecord()
        broadcastWorkingOutUpdate(lastUpdateLocation, record?.getDisplayData())
    }

    private fun onResumeWorkingOut() {
        //Log.d(simpleName(), "Action Resume")
        status = WorkoutStatus.WORKING_OUT
        startScheduledThread()
        broadcastWorkingOutUpdate(lastUpdateLocation, record?.getDisplayData())
    }

    private fun onStopWorkingOut() {
        //Log.d(simpleName(), "Action Stop")
        stopSelf()
        stopForeground(true)
        notificationManager = null
        status = WorkoutStatus.STOP
        stopScheduledThread()
        record?.stop = System.currentTimeMillis()
        updateWorkingOutRecord()

        //Send broadcast to update ui in [WorkoutScreen]
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(KEY_STATUS, status)
        intent.putExtra(KEY_DATA, record)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    private fun onClearWorkingOut() {
        record = null
        lastUpdateTimeMillis = 0
        lastUpdateLocation = null
        status = WorkoutStatus.UNKNOWN
        if (locationDao != null) {
            launch(IO) {
                locationDao?.deleteAllLocation()
                locationDao = null
                WorkoutDataBase.destroy()
            }
        }
    }

    private fun startScheduledThread() {
        if (scheduler != null) {
            stopScheduledThread()
        }
        //Set last update time and location
        lastUpdateTimeMillis = System.currentTimeMillis()
        lastUpdateLocation = newLocation

        scheduler = Executors.newScheduledThreadPool(1)
        scheduler?.scheduleAtFixedRate({
            updateWorkingOutRecord()
            //Send broadcast to update ui and update notification.
            broadcastWorkingOutUpdate(lastUpdateLocation, record?.getDisplayData())

        }, 0, 1, TimeUnit.SECONDS)
    }

    private fun stopScheduledThread() {
        scheduler?.shutdownNow()
        scheduler = null
    }

    private fun updateWorkingOutRecord() {
        record?.apply {
            duration += System.currentTimeMillis() - lastUpdateTimeMillis
            newLocation?.also {
                distances += lastUpdateLocation?.distanceTo(it) ?: 0f
            }
        }

        //Set last update time, location and add location to realm
        lastUpdateTimeMillis = System.currentTimeMillis()
        if (newLocation?.latitude != lastUpdateLocation?.latitude || newLocation?.longitude != lastUpdateLocation?.longitude) {
            addTempLocation(newLocation)
        }
        lastUpdateLocation = newLocation

    }

    private fun broadcastWorkingOutUpdate(location: Location?, displayData: WorkingOutDisplayData? = null) {
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(KEY_STATUS, status)

        if (location != null) {
            intent.putExtra(KEY_LOCATION, WorkingOutLocation(location))
        }

        if (displayData != null) {
            intent.putExtra(KEY_DATA, displayData)
            notificationManager?.notify(
                NOTIFICATION_WORKOUT_ID,
                getNotification(displayData.getNotificationContent(resources))
            )
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    private fun setupNotificationManager() {
        //Set up notification manager
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the channel for the notification
            val channel = NotificationChannel(
                NOTIFICATION_WORKOUT_CHANEL_ID,
                getString(R.string.workout),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            // Set the Notification Channel for the Notification Manager.
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun getNotification(contentText: String = "00:00:00, 0.00 km"): Notification {

        val activityIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            putExtra(KEY_SCREEN, WorkoutScreen::class.java.simpleName)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_WORKOUT_CHANEL_ID)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.mipmap.ic_logo_runex)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentTitle(record?.type ?: "")
            .setContentText(contentText)
            .setContentIntent(pendingIntent)

        if (status == WorkoutStatus.WORKING_OUT || status == WorkoutStatus.PAUSE) {
            val intent = Intent(this, WorkoutService::class.java)
            var icon: Int = R.drawable.ic_control_pause
            var title: String = ""
            when (status) {
                WorkoutStatus.WORKING_OUT -> {
                    intent.putExtra(KEY_ACTION, WorkoutAction.PAUSE)
                    icon = R.drawable.ic_control_pause
                    title = getString(R.string.pause_recording)
                }
                WorkoutStatus.PAUSE -> {
                    intent.putExtra(KEY_ACTION, WorkoutAction.RESUME)
                    icon = R.drawable.ic_control_play
                    title = getString(R.string.resume_recording)
                }
            }
            val servicePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.addAction(icon, title, servicePendingIntent)
        }
        return builder.build()
    }

    //Update last location to realm data base
    private fun addTempLocation(location: Location?) {
        if (location == null) return
        launch(IO) {
            locationDao?.insertLocation(WorkingOutLocation(location))
        }
    }

    private fun clearTempLocation() {
        launch(IO) {
            locationDao?.deleteAllLocation()
        }
    }

    /**
     * Returns true if this is a foreground service.
     */
    private fun isRunningInForeground(): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    Logger.debug(simpleName(), "isRunningInForeground: true")
                    return true
                }
            }
        }
        Logger.debug(simpleName(), "isRunningInForeground: false")
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


    /**
     * Handler message from [WorkoutScreen]
     */
    @SuppressLint("HandlerLeak")
    private inner class IncomingHandler : Handler() {
        override fun handleMessage(message: Message) {
            when (message.what) {
                WorkoutAction.START -> {
                    val type = message.data?.getString(KEY_TYPE) ?: WorkoutType.RUNNING
                    onStartWorkingOut(type)
                }
                WorkoutAction.PAUSE -> onPauseWorkingOut()
                WorkoutAction.RESUME -> onResumeWorkingOut()
                WorkoutAction.STOP -> onStopWorkingOut()
                WorkoutAction.CLEAR -> onClearWorkingOut()
            }
        }
    }
}