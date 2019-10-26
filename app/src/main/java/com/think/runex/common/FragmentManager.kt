package com.think.runex.common

import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.jozzee.android.core.fragment.getFragments
import com.jozzee.android.core.simpleName

fun Fragment.getAppCompatActivity(): AppCompatActivity = (activity as AppCompatActivity)

/**
 * Find fragment in support fragment manager with simple class name of the fragment.
 */
fun FragmentActivity.findFragment(simpleName: String): Fragment? = getFragments()?.let {
    it.forEach { fragment ->
        if (fragment.simpleName() == simpleName) {
            return fragment
        }
    }
    return null
}


fun Fragment.findFragment(simpleName: String): Fragment? = activity?.findFragment(simpleName)

fun Fragment.showDialog(dialog: DialogFragment) {
    dialog.show(childFragmentManager, dialog::class.java.simpleName)
}

fun Fragment.showBottomSheet(bottomSheet: BottomSheetDialogFragment) {
    bottomSheet.show(childFragmentManager, bottomSheet::class.java.simpleName)
}

fun Fragment.showSnackBar(message: String, duration: Int = Snackbar.LENGTH_SHORT, v: View? = null) {
    if (v != null) {
        activity?.runOnUiThread {
            Snackbar.make(v, message, duration).show()
        }
    } else if (view != null) {
        activity?.runOnUiThread {
            Snackbar.make(view!!, message, duration).show()
        }
    }
}

fun Fragment.checkSelfPermission(permissions: Array<String>, requestCode: Int): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context!!, permission) != PackageManager.PERMISSION_GRANTED) {
                //Permission is not granted
                requestPermissions(permissions, requestCode)
                return false
            }
        }
    }
    return true
}

fun Fragment.isPermissionsGranted(permissions: Array<out String>, grantResults: IntArray): Boolean {
    if (permissions.isEmpty() || grantResults.isEmpty() && permissions.size != grantResults.size) {
        return false
    }
    for (result in grantResults) {
        if (result != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

fun Fragment.isPermissionNeverAskAgain(permissions: Array<out String>): Boolean {
    for (i in 0 until (permissions.size - 1)) {
        if (shouldShowRequestPermissionRationale(permissions[i])) {
            return true
        }
    }
    return false
}