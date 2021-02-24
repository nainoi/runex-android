package com.think.runex.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.allPermissionsGranted(resultsMap: Map<String, Boolean>): Boolean = resultsMap.all { it.value }

fun Fragment.allPermissionsGranted(resultsMap: Map<String, Boolean>): Boolean = resultsMap.all { it.value }

/**
 * If we should show the rationale for requesting permission, then
 * we'll show rationale view which does this.
 *
 * If `shouldShowPermissionRationale` is false, this means the user has not only denied
 * the permission, but they've clicked "Don't ask again". In this case
 * we send the user to the settings page for the app so they can grant
 * the permission (Yay!) or uninstall the app.
 */
fun FragmentActivity.shouldShowPermissionRationale(resultsMap: Map<String, Boolean>): Boolean {
    return resultsMap.all { shouldShowRequestPermissionRationale(it.key) }
}

fun Fragment.shouldShowPermissionRationale(resultsMap: Map<String, Boolean>): Boolean {
    return resultsMap.all { shouldShowRequestPermissionRationale(it.key) }
}
