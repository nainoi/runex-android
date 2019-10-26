package com.think.runex.ui


import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jozzee.android.core.ui.hideKeyboard
import com.jozzee.android.core.ui.setScreenTouchable
import com.think.runex.R
import com.think.runex.application.MainActivity
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.utility.InjectorUtils

open class ScreenFragment : Fragment() {

//    fun getAuthViewModel(): AuthViewModel {
//        return ViewModelProviders.of(activity!!, InjectorUtils.provideAuthViewModelFactory(context!!))
//                .get(AuthViewModel::class.java)
//    }

    /**
     * When fragment transition completed update screen touchable.
     */
    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        if (nextAnim > 0) {
            val anim = AnimationUtils.loadAnimation(context, nextAnim)
            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(p0: Animation?) {
                }

                override fun onAnimationEnd(p0: Animation?) {
                    activity?.setScreenTouchable(true)
                }

                override fun onAnimationStart(p0: Animation?) {
                    activity?.setScreenTouchable(false)
                }
            })
            return anim
        }
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        view?.hideKeyboard()
    }


    /**
     * Hid keyboard before destroy view.
     */
    override fun onDestroyView() {
        view?.hideKeyboard()
        super.onDestroyView()

        Glide.get(context!!).bitmapPool.clearMemory()
        Glide.get(context!!).clearMemory()
    }

    /**
     * Clear runtime qc when destroy.
     */
    override fun onDestroy() {
        Runtime.getRuntime().gc()
        super.onDestroy()
    }

    fun isBackPressedEnabled(): Boolean {
        if (activity != null && activity is MainActivity) {
            return (activity as MainActivity).isBackPressedEnabled()
        }
        return true
    }

    fun setBackPressedEnabled(isBackPressed: Boolean) {
        if (activity != null && activity is MainActivity) {
            (activity as MainActivity).setBackPressedEnabled(isBackPressed)
        }
    }

    fun showAlertDialog(title: String? = null, message: String,
                        onPositiveClick: (() -> Unit)? = null,
                        positiveText: String? = null,
                        onNegativeClick: (() -> Unit)? = null,
                        negativeText: String? = null,
                        cancelable: Boolean = true) {

        if (context == null || view == null) {
            return
        }

        MaterialAlertDialogBuilder(context).apply {
            title?.let {
                setTitle(it)
            }
            setMessage(message)
            setCancelable(cancelable)
            setPositiveButton(positiveText ?: getString(R.string.ok)) { dialog, _ ->
                onPositiveClick?.invoke()
                dialog.dismiss()
            }
            negativeText?.let {
                setNegativeButton(it) { dialog, _ ->
                    onNegativeClick?.invoke()
                    dialog.dismiss()
                }
            }
        }.run {
            create().show()
        }
    }
}
