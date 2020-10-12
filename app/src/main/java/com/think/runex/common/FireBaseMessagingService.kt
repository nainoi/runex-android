package com.think.runex.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jozzee.android.core.util.Logger
import com.think.runex.R
import com.think.runex.config.NOTIFICATION_CHANEL_ID_DEFAULT
import com.think.runex.config.NOTIFICATION_ID_DEFAULT
import com.think.runex.config.NOTIFICATION_REQUEST_CODE

class FireBaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val KEY_BODY = "body"
        private const val KEY_TITLE = "title"

        const val KEY_FIREBASE_TOKEN = "firebase_token"

        fun getFirebaseTokenInstance(context: Context, onComplete: (firebaseToken: String?) -> Unit) {
            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
                when (task.isSuccessful) {
                    true -> {
                        val firebaseToken = task.result?.token ?: ""
                        AppPreference.setFirebaseToken(context, firebaseToken)
                        onComplete.invoke(firebaseToken)
                    }
                    false -> onComplete.invoke(null)
                }
            }
        }
    }

    private var refreshBroadcaster: LocalBroadcastManager? = null

    override fun onCreate() {
        super.onCreate()
        refreshBroadcaster = LocalBroadcastManager.getInstance(this)
        Logger.debug("FireBasService", "Initial refreshBroadcaster")
    }

    override fun onDestroy() {
        super.onDestroy()
        refreshBroadcaster = null
        Logger.debug("FireBasService", "Clear refreshBroadcaster")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Logger.debug("FireBasService", "onMessageReceived from: ${remoteMessage.from}")

        // Check if message contains a data payload.
        //if (remoteMessage.data.isNotEmpty()) {
        //    Logger.debug("FireBasService", "Message data payload: ${remoteMessage.data}")
        //}
        // Check if message contains a notification payload.
        //if (remoteMessage.notification != null) {
        //    Logger.debug("FireBasService", "Notification payload: ${remoteMessage.notification}")
        //}

        /**
         * There are two types of messages in FCM (Firebase Cloud Messaging)
         * 1 Display Messages: These messages trigger the onMessageReceived() callback only when your app is in foreground
         * 2 Data Messages: Theses messages trigger the onMessageReceived() callback even if your app is in foreground/background/killed
         * So, send message use "data"
         * Example JSON format
         * {
        "to": "/topics/dev_journal",
        "data": {
        "title":"Title",
        "body":"Message",
        "some data":"some value"
        }
        }
         */

        Log.d("FirebaseService", "onMessageReceived")
        Log.d("FirebaseService", "From: ${remoteMessage.from}")
        Log.d("FirebaseService", "Notification: ${remoteMessage.notification?.toJson()}")
        Log.d("FirebaseService", "Data: ${remoteMessage.data.toJson()}")

        val title = remoteMessage.notification?.title ?: remoteMessage.data[KEY_TITLE] ?: ""
        val body = remoteMessage.notification?.body ?: remoteMessage.data[KEY_BODY] ?: ""

        Logger.debug("FireBasService", "Title: $title")
        Logger.debug("FireBasService", "Body: $body")

        sendNotification(title, body)


        //Send broadcast to refresh
//        refreshBroadcaster?.sendBroadcast(Intent(KEY_REFRESH).apply {
//            putExtra(KEY_REFRESH, true)
//        })
    }

    override fun onNewToken(firebaseToken: String) {
        super.onNewToken(firebaseToken)
        Log.w("FireBasService", "New firebase token: $firebaseToken")
        AppPreference.setFirebaseToken(this, firebaseToken)
    }

    private fun sendNotification(title: String, body: String) {

        //TODO("Set arguments to activity via bundle such")
        //val bundle = Bundle().apply {
        //    //Wait codding
        //}

        val intent = Intent().apply {
//            when (BuildConfig.FLAVOR) {
//                CUSTOMER -> setClass(this@FireBaseMessagingService, CustomerActivity::class.java)
//                MERCHANT -> setClass(this@FireBaseMessagingService, MerchantActivity::class.java)
//                DRIVER -> setClass(this@FireBaseMessagingService, DriverActivity::class.java)
//            }
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(NOTIFICATION_CHANEL_ID_DEFAULT, "Default", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANEL_ID_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)


        manager.notify(NOTIFICATION_ID_DEFAULT, builder.build())
    }
}