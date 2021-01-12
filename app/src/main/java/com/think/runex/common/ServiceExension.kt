package com.think.runex.common

import android.app.ActivityManager
import android.content.Context

fun Context.serviceIsRunningInForeground(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Int.MAX_VALUE)) {
        if (service.foreground && serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}


fun Context.serviceIsRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Int.MAX_VALUE)) {
        if (serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}