package com.think.runex.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jozzee.android.core.util.Logger
import com.think.runex.R
import com.think.runex.util.extension.toJson
import com.think.runex.config.*
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.auth.*
import com.think.runex.feature.auth.data.TokenManager
import com.think.runex.MainActivity
import com.think.runex.util.AppPreference
import com.think.runex.util.extension.launch
import kotlinx.coroutines.Dispatchers.IO

class FireBaseMessagingService : FirebaseMessagingService() {

    companion object {


//        fun getFirebaseTokenInstance(context: Context, onComplete: (firebaseToken: String?) -> Unit) {
//            FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task ->
//                when (task.isSuccessful) {
//                    true -> {
//                        val firebaseToken = task.result?.token ?: ""
//                        AppPreference.setFirebaseToken(context, firebaseToken)
//                        onComplete.invoke(firebaseToken)
//                    }
//                    false -> onComplete.invoke(null)
//                }
//            }
//        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Logger.debug("FireBasService", "onMessageReceived from: ${remoteMessage.from}")

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
    }

    override fun onNewToken(firebaseToken: String) {
        super.onNewToken(firebaseToken)
        Log.i("FireBasService", "New firebase token: $firebaseToken")

        //Send firebase token to server.
        launch(IO) {
            val repository = AuthRepository(ApiService().provideService(this@FireBaseMessagingService, AuthApi::class.java),
                    AppPreference.createPreference(this@FireBaseMessagingService))

            //Set new firebase token to shared preferences.
            repository.setFirebaseToken(firebaseToken)
            repository.setUpdatedFirebaseToken(false)

            if (TokenManager.isAlive()) {
                val result = repository.sendFirebaseTokenToServer(firebaseToken)
                if (result.isSuccessful()) {
                    repository.setUpdatedFirebaseToken(true)
                }
            }
        }
    }

    private fun sendNotification(title: String, body: String) {
        //TODO("Set arguments to activity via bundle such")
        //val bundle = Bundle().apply {
        //    //Wait codding
        //}


        //Set up notification manager
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the channel for the notification
            val channel = NotificationChannel(NOTIFICATION_DEFAULT_CHANEL_ID, getString(R.string.default_), NotificationManager.IMPORTANCE_DEFAULT)
            // Set the Notification Channel for the Notification Manager.
            notificationManager.createNotificationChannel(channel)
        }

        val activityIntent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, NOTIFICATION_DEFAULT_CHANEL_ID)
                .setSmallIcon(R.mipmap.ic_logo_runex)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_DEFAULT_ID, builder.build())
    }
}