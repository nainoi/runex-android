package com.think.runex.ui


import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jozzee.android.core.touch.setScreenTouchable
import com.jozzee.android.core.view.hideKeyboard
import com.think.runex.R

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
    }

    /**
     * Clear runtime qc when destroy.
     */
    override fun onDestroy() {
        Runtime.getRuntime().gc()
        super.onDestroy()
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

        MaterialAlertDialogBuilder(requireContext()).apply {
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
