package com.think.runex.common

import android.content.Intent
import android.provider.Settings
import android.view.View
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.think.runex.R

fun Fragment.showAlertDialog(title: String? = null, message: String?,
                             onPositiveClick: (() -> Unit)? = null,
                             positiveText: String? = null,
                             onNegativeClick: (() -> Unit)? = null,
                             negativeText: String? = null,
                             cancelable: Boolean = true) {

    if (isAdded.not() || view == null) return

    val builder = MaterialAlertDialogBuilder(context!!)
    builder.setCancelable(cancelable)
    builder.setMessage(message ?: "")
    title?.let {
        builder.setTitle(title)
    }

    builder.setPositiveButton(positiveText ?: getString(R.string.ok)) { dialog, _ ->
        onPositiveClick?.invoke()
        dialog.dismiss()
    }

    negativeText?.let {
        builder.setNegativeButton(it) { dialog, _ ->
            onNegativeClick?.invoke()
            dialog.dismiss()
        }
    }

    builder.create().show()
}

fun Fragment.showAlertDialog(@StringRes title: Int = 0, @StringRes message: Int = 0,
                             @StringRes positiveText: Int = R.string.ok,
                             @StringRes negativeText: Int = 0,
                             onPositiveClick: (() -> Unit)? = null,
                             onNegativeClick: (() -> Unit)? = null,
                             cancelable: Boolean = true) {

    if (isAdded.not() || view == null) return

    val builder = MaterialAlertDialogBuilder(context!!)
    builder.setCancelable(cancelable)

    if (title != 0) {
        builder.setTitle(getString(title))
    }
    if (message != 0) {
        builder.setMessage(getString(message))
    }

    builder.setPositiveButton(getString(positiveText)) { dialog, _ ->
        onPositiveClick?.invoke()
        dialog.dismiss()
    }

    if (negativeText != 0) {
        builder.setNegativeButton(getString(negativeText)) { dialog, _ ->
            onNegativeClick?.invoke()
            dialog.dismiss()
        }
    }

    builder.create().show()
}

fun Fragment.showSnackBar(message: String, duration: Int = Snackbar.LENGTH_SHORT, v: View? = view) {
    if (isAdded.not() || v == null) return
    Snackbar.make(v, message, duration).show()
}

fun Fragment.showSnackBar(@StringRes message: Int, duration: Int = Snackbar.LENGTH_SHORT, v: View? = view) {
    if (isAdded.not() || v == null) return
    Snackbar.make(v, message, duration).show()
}

fun Fragment.showSnackBarToInternetSetting() {
    if (isAdded.not() || view == null) return
    Snackbar.make(view!!, R.string.no_internet_connection, Snackbar.LENGTH_LONG)
            .setAction(R.string.setting) {
                startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))
            }
            .show()
}