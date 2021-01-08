package com.think.runex.feature.workout

import android.app.Service
import android.content.Intent
import android.os.IBinder

class WorkoutService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}