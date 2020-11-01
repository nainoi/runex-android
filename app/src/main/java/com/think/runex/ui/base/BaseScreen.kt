package com.think.runex.ui.base

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.jozzee.android.core.fragment.*
import com.jozzee.android.core.touch.setScreenTouchable
import com.jozzee.android.core.view.hideKeyboard

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
     * Hid keyboard before destroy view.
     */
    override fun onDestroyView() {
        view?.hideKeyboard()
        super.onDestroyView()
    }

    /**
     * Clear of glide memory when destroy.
     */
    override fun onDestroy() {
        Glide.get(requireContext()).bitmapPool.clearMemory()
        Glide.get(requireContext()).clearMemory()
        super.onDestroy()
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
}