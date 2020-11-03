package com.think.runex.common

import android.app.Activity
import android.os.Build
import android.view.ViewStub
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment

fun Activity.setStatusBarColor(@ColorInt color: Int, isLightStatusBar: Boolean) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

    //TODO("Clear flag full screen in java code")
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

    window.statusBarColor = color
    var newUiVisibility: Int = ViewStub.SYSTEM_UI_FLAG_LAYOUT_STABLE

    newUiVisibility = newUiVisibility or ViewStub.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        when (isLightStatusBar) {
            true -> window.decorView.systemUiVisibility = (newUiVisibility or ViewStub.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            false -> window.decorView.systemUiVisibility = (newUiVisibility and ViewStub.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
        }
    }
}


fun Fragment.setStatusBarColor(@ColorInt color: Int, isLightStatusBar: Boolean) {
    if (view == null || isAdded.not()) return
    activity?.setStatusBarColor(color, isLightStatusBar)
}