package com.think.runex.common

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.hideKeyboard
import com.jozzee.android.core.view.setVisible
import com.jozzee.android.core.view.visible
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
                view.negative_button?.gone()
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

/**
 * Show normal alert dialog with activity.
 * [title] text character
 * [message] text character
 * [onPositiveClick] event when click positive button (OK Button)
 */
fun FragmentActivity.showAlertDialog(
        title: String?,
        message: String?,
        onPositiveClick: (() -> Unit)? = null) {

    if (isDestroyed) return
    hideKeyboard()

    showAlertDialog(title, message, null, null,
            onPositiveClick, null, null, true)
}


fun FragmentActivity.showAlertDialog(
        message: String,
        onPositiveClick: (() -> Unit)? = null) {

    if (isDestroyed) return
    hideKeyboard()

    showAlertDialog(getString(R.string.app_name), message, null, null,
            onPositiveClick, null, null, true)
}

/**
 * Show normal alert dialog with activity.
 * [title] string resource id
 * [message]  string resource id
 * [onPositiveClick] event when click positive button (OK Button)
 */
fun FragmentActivity.showAlertDialog(
        @StringRes title: Int?,
        @StringRes message: Int?,
        onPositiveClick: (() -> Unit)? = null) {

    if (isDestroyed) return
    hideKeyboard()

    val titleText = if (title != null) getString(title) else null
    val messageText = if (message != null) getString(message) else null

    showAlertDialog(titleText, messageText, null, null,
            onPositiveClick, null, null, true)
}

fun FragmentActivity.showAlertDialog(
        @StringRes title: Int?,
        @StringRes message: Int?,
        @StringRes positiveButton: Int = R.string.ok,
        @StringRes negativeButton: Int = R.string.cancel,
        onPositiveClick: (() -> Unit)? = null) {

    if (isDestroyed) return
    hideKeyboard()

    val titleText = if (title != null) getString(title) else null
    val messageText = if (message != null) getString(message) else null

    showAlertDialog(titleText, messageText, getString(positiveButton), getString(negativeButton),
            onPositiveClick, null, null, true)
}

fun FragmentActivity.showAlertDialog(
        @StringRes message: Int,
        onPositiveClick: (() -> Unit)? = null) {

    if (isDestroyed) return
    hideKeyboard()

    showAlertDialog(getString(R.string.app_name), getString(message), null, null,
            onPositiveClick, null, null, true)
}

/**
 * Show normal alert dialog with fragment.
 * [title] text character
 * [message] text character
 * [onPositiveClick] event when click positive button (OK Button)
 */
fun Fragment.showAlertDialog(
        title: String?,
        message: String?,
        onPositiveClick: (() -> Unit)? = null) {

    if (isAdded.not() || view == null) return
    view?.hideKeyboard()

    activity?.showAlertDialog(title, message, null, null,
            onPositiveClick, null, null, true)
}

fun Fragment.showAlertDialog(
        message: String,
        onPositiveClick: (() -> Unit)? = null) {

    if (isAdded.not() || view == null) return
    view?.hideKeyboard()

    activity?.showAlertDialog(getString(R.string.app_name), message, null, null,
            onPositiveClick, null, null, true)
}


/**
 * Show normal alert dialog with fragment.
 * [title] string resource id
 * [message]  string resource id
 * [onPositiveClick] event when click positive button (OK Button)
 */
fun Fragment.showAlertDialog(
        @StringRes title: Int?,
        @StringRes message: Int?,
        onPositiveClick: (() -> Unit)? = null) {

    if (isAdded.not() || view == null) return
    view?.hideKeyboard()

    val titleText = if (title != null) getString(title) else null
    val messageText = if (message != null) getString(message) else null
    activity?.showAlertDialog(titleText, messageText, null, null,
            onPositiveClick, null, null, true)
}

fun Fragment.showAlertDialog(
        @StringRes message: Int,
        isCancelEnable: Boolean = true,
        onPositiveClick: (() -> Unit)? = null) {

    if (isAdded.not() || view == null) return
    view?.hideKeyboard()

    activity?.showAlertDialog(getString(R.string.app_name), getString(message), null, null,
            onPositiveClick, null, null, isCancelEnable)
}
