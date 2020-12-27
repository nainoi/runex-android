package com.think.runex.common

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.ViewStub
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment

/**
 * Set status bar color
 * [color] color want to set to status bar
 * [isLightStatusBar] if 'true' it mean status bar has light theme such as writhe color
 * if 'false' it mean status bar has dark theme such as black color
 */
fun Activity.setStatusBarColor(@ColorInt color: Int = Color.TRANSPARENT, isLightStatusBar: Boolean) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
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


fun Fragment.setStatusBarColor(@ColorInt color: Int = Color.TRANSPARENT, isLightStatusBar: Boolean) {
    if (view == null || isAdded.not()) return
    activity?.setStatusBarColor(color, isLightStatusBar)
}