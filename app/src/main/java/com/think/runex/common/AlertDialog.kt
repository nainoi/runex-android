package com.think.runex.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.view.*
import com.think.runex.R
import kotlinx.android.synthetic.main.dialog_default.view.*

/**
 * Show default alert dialog.
 */
@SuppressLint("InflateParams")
fun Context.showAlertDialog(
        title: String?,
        message: String?,
        positiveText: String? = null,
        negativeText: String? = null,
        onPositiveClick: (() -> Unit)? = null,
        onNegativeClick: (() -> Unit)? = null,
        onDialogDismiss: (() -> Unit)? = null,
        cancelable: Boolean = true) {

    /**
     * Set alert dialog default
     */
//    val builder = MaterialAlertDialogBuilder(context, R.style.AppAlertDialog)
//    //Set cancel able
//    builder.setCancelable(cancelable)
//
//    //Set message
//    builder.setMessage(message ?: "")
//
//    //Set title (if have).
//    title?.let {
//        builder.setTitle(title)
//    }
//
//    //Default positive button (OK Button)
//    builder.setPositiveButton(positiveText ?: context.getString(R.string.ok)) { dialog, _ ->
//        onPositiveClick?.invoke()
//        dialog.dismiss()
//    }
//
//    //Set negative button (if have)
//    negativeText?.let {
//        builder.setNegativeButton(it) { dialog, _ ->
//            onNegativeClick?.invoke()
//            dialog.dismiss()
//        }
//    }
//    //Create and show
//    builder.create().apply {
//        setOnDismissListener {
//            onDialogDismiss?.invoke()
//        }
//
//    }.show()

    /**
     * Set alert dialog with custom layout
     */
    val view = LayoutInflater.from(this).inflate(R.layout.dialog_default, null, false)
    MaterialAlertDialogBuilder(this, R.style.AppAlertDialog).apply {
        setCancelable(cancelable)
        setView(view)
    }.create().apply {

        setOnDismissListener {
            onDialogDismiss?.invoke()
        }

        //Set title and message
        view.title_label?.setVisible(title?.isNotBlank() == true)
        view.title_label?.text = title ?: ""
        view.message_label?.setVisible(message?.isNotBlank() == true)
        view.message_label?.text = message ?: ""

        //Set negative button
        when (negativeText?.isNotBlank() == true) {
            true -> {
                view.negative_button?.visible()
                view.negative_button?.text = negativeText ?: ""
                view.negative_button?.setOnClickListener {
                    onNegativeClick?.invoke()
                    dismiss()
                }
            }
            false -> {
                //view.guideline_center_horizontal?.gone()
                view.warpContent()
                view.negative_button?.gone()
                view.positive_button?.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0f).apply {
                    setMargins(0, 0, 0, 0)
                }
                getDimension(R.dimen.space_56dp).also { space50Dp ->
                    view.positive_button?.setPadding(space50Dp, 0, space50Dp, 0)
                }
            }
        }

        //Set positive button
        if (positiveText?.isNotBlank() == true) {
            view.positive_button?.text = positiveText
        }
        view.positive_button?.setOnClickListener {
            onPositiveClick?.invoke()
            dismiss()
        }

    }.show()
}

fun FragmentActivity.showAlertDialog(
        title: String?,
        message: String?,
        isCancelEnable: Boolean = true,
        positiveText: String? = null,
        onPositiveClick: (() -> Unit)? = null) {

    if (isDestroyed) return
    hideKeyboard()

    showAlertDialog(title = title, message = message, positiveText = positiveText, negativeText = null,
            onPositiveClick = onPositiveClick, onNegativeClick = null, onDialogDismiss = null,
            cancelable = isCancelEnable)
}

fun FragmentActivity.showAlertDialog(
        @StringRes message: Int,
        @StringRes positiveText: Int? = null,
        isCancelEnable: Boolean = true,
        onPositiveClick: (() -> Unit)? = null) {

    if (isDestroyed) return
    hideKeyboard()

    showAlertDialog(title = null,
            message = getString(message),
            positiveText = if (positiveText != null) getString(positiveText) else null,
            negativeText = null,
            onPositiveClick = onPositiveClick,
            onNegativeClick = null,
            onDialogDismiss = null,
            cancelable = isCancelEnable)
}

fun Fragment.showAlertDialog(@StringRes title: Int?,
                             @StringRes message: Int?,
                             @StringRes positiveText: Int? = null,
                             @StringRes negativeText: Int? = null,
                             onPositiveClick: (() -> Unit)? = null,
                             onNegativeClick: (() -> Unit)? = null,
                             onDialogDismiss: (() -> Unit)? = null,
                             cancelable: Boolean = true) {
    if (isAdded.not() || view == null) return
    requireContext().showAlertDialog(
            title = if (title != null) getString(title) else null,
            message = if (message != null) getString(message) else null,
            positiveText = if (positiveText != null) getString(positiveText) else null,
            negativeText = if (negativeText != null) getString(negativeText) else null,
            onPositiveClick = onPositiveClick,
            onNegativeClick = onNegativeClick,
            onDialogDismiss = onDialogDismiss,
            cancelable = cancelable)
}

fun Fragment.showAlertDialog(title: String?,
                             message: String?,
                             positiveText: String? = null,
                             negativeText: String? = null,
                             onPositiveClick: (() -> Unit)? = null,
                             onNegativeClick: (() -> Unit)? = null,
                             onDialogDismiss: (() -> Unit)? = null,
                             cancelable: Boolean = true) {
    if (isAdded.not() || view == null) return
    requireContext().showAlertDialog(title, message, positiveText, negativeText, onPositiveClick, onNegativeClick, onDialogDismiss, cancelable)
}

fun Fragment.showAlertDialog(
        title: String?,
        message: String?,
        positiveText: String? = null,
        isCancelEnable: Boolean = true,
        onPositiveClick: (() -> Unit)? = null) {

    if (isAdded.not() || view == null) return
    view?.hideKeyboard()

    requireContext().showAlertDialog(title = title, message = message,
            positiveText = positiveText, negativeText = null,
            onPositiveClick = onPositiveClick, onNegativeClick = null, onDialogDismiss = null,
            cancelable = isCancelEnable)
}


fun Fragment.showAlertDialog(
        @StringRes message: Int,
        @StringRes positiveText: Int? = null,
        isCancelEnable: Boolean = true,
        onPositiveClick: (() -> Unit)? = null) {

    if (isAdded.not() || view == null) return
    view?.hideKeyboard()

    requireContext().showAlertDialog(title = null,
            message = getString(message),
            positiveText = if (positiveText != null) getString(positiveText) else null,
            negativeText = null,
            onPositiveClick = onPositiveClick,
            onNegativeClick = null,
            onDialogDismiss = null,
            cancelable = isCancelEnable)
}

fun Context.showSettingPermissionInSettingDialog() = showAlertDialog(
        title = getString(R.string.app_name),
        message = getString(R.string.allow_permission_in_setting),
        positiveText = getString(R.string.setting),
        onPositiveClick = {
            //On positive click, go to permission setting screen.
            val uri = Uri.parse("package:${packageName}")
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        })

