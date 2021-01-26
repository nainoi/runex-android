package com.think.runex.ui.base

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.bumptech.glide.Glide
import com.jozzee.android.core.fragment.*
import com.jozzee.android.core.touch.setScreenTouchable
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.hideKeyboard
import com.think.runex.R
import com.think.runex.common.showAlertDialog
import com.think.runex.datasource.api.ApiExceptionMessage
import com.think.runex.ui.component.ProgressDialog

open class BaseScreen : Fragment() {

    /**
     * When fragment transition completed update screen touchable.
     */
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (nextAnim > 0) {
            val animation = AnimationUtils.loadAnimation(context, nextAnim)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(anim: Animation?) {
                    activity?.setScreenTouchable(true)
                }

                override fun onAnimationStart(anim: Animation?) {
                    activity?.setScreenTouchable(false)
                }
            })
            return animation
        }
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        view?.hideKeyboard()
    }


    /**
     * Clear of glide memory when destroy.
     */
    override fun onDestroy() {
        Glide.get(requireContext()).bitmapPool.clearMemory()
        Glide.get(requireContext()).clearMemory()
        super.onDestroy()
    }

    fun showProgressDialog(@StringRes message: Int? = null,
                           @ColorRes progressColor: Int = -1,
                           showDot: Boolean = true) {
        if (isAdded.not() || view == null) return
        val msg = if (message != null) getString(message) else ""
        showProgressDialog(msg, progressColor, if (msg.isNotBlank()) showDot else false)
    }

    /**
     * Show and update progress dialog.
     */
    fun showProgressDialog(message: String? = null,
                           @ColorRes progressColor: Int = -1,
                           showDot: Boolean = true) {
        if (isAdded.not() || view == null) return

        var progressDialog = childFragmentManager.findFragmentByTag(ProgressDialog::class.java.simpleName)
        if (progressDialog != null && progressDialog is ProgressDialog) {
            progressDialog.setProgressColor(progressColor)
            progressDialog.setMessage(message)
        } else {
            progressDialog = ProgressDialog
                    .newInstance("$message${if (showDot) "..." else ""}", progressColor)
            childFragmentManager.commit(allowStateLoss = true) {
                add(progressDialog, progressDialog::class.java.simpleName)
            }
            childFragmentManager.executePendingTransactions()
        }
    }

    fun hideProgressDialog() {
        childFragmentManager.findFragmentByTag(ProgressDialog::class.java.simpleName)?.also { fragment ->
            childFragmentManager.commit(allowStateLoss = true) {
                remove(fragment)
            }
            childFragmentManager.executePendingTransactions()
        }
    }

    open fun errorHandler(statusCode: Int, message: String) {
        if (isAdded.not() || view == null) return

        val errorMessage = ApiExceptionMessage.getExceptionMessageFromStatusCode(resources, statusCode, message)
        Logger.error(simpleName(), "Error Handler: Status code: $statusCode, Message: $errorMessage")

        activity?.runOnUiThread {
            hideProgressDialog()
            if (errorMessage.isNotBlank()) {
                //Show alert dialog if have error message.
                showAlertDialog(getString(R.string.error), errorMessage)
            }
        }
    }

    open fun addFragment(fragment: Fragment,
                         animations: FragmentAnimations? = null,
                         hidePrevious: Int = fragmentCount(),
                         tag: String? = null) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).addFragment(fragment, animations, hidePrevious, tag)
        } else {
            addFragment(fragment, animations, FragmentContainer.id, hidePrevious, tag)
        }
    }

    open fun replaceFragment(fragment: Fragment,
                             animations: FragmentAnimations? = null,
                             addToBackStack: Boolean = true,
                             clearFragment: Boolean = false,
                             tag: String? = null) {
        if (activity is BaseActivity) {
            (activity as BaseActivity).replaceFragment(fragment, animations, addToBackStack, clearFragment, tag)
        } else {
            replaceFragment(fragment, animations, FragmentContainer.id, addToBackStack, clearFragment, tag)
        }
    }

    /**
     * Hid keyboard before destroy view.
     */
    override fun onDestroyView() {
        view?.hideKeyboard()
        super.onDestroyView()
    }

}