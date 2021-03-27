package com.think.runex.common

import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.jozzee.android.core.util.simpleName
import java.lang.Exception

/**
 * Find Fragment
 */
fun FragmentActivity.findFragmentByTag(tag: String?): Fragment? {
    return supportFragmentManager.findFragmentByTag(tag)
}

fun Fragment.findFragmentByTag(tag: String?): Fragment? {
    return activity?.findFragmentByTag(tag)
}

fun Fragment.findChildFragmentByTag(tag: String?): Fragment? {
    return childFragmentManager.findFragmentByTag(tag)
}

inline fun <reified T : Fragment> FragmentActivity.findFragment(): T? {
    return supportFragmentManager.fragments.find { it::class.java.simpleName == T::class.java.simpleName }.let {
        if (it != null) it as T else null
    }
}

inline fun <reified T : Fragment> Fragment.findFragment(): T? {
    return activity?.supportFragmentManager?.fragments?.find { it::class.java.simpleName == T::class.java.simpleName }.let {
        if (it != null) it as T else null
    }
}

inline fun <reified T : Fragment> Fragment.findChildFragment(): T? {
    return childFragmentManager.fragments.find { it::class.java.simpleName == T::class.java.simpleName }.let {
        if (it != null) it as T else null
    }
}

/**
 * Get fragment
 */
fun FragmentActivity.getTopFragment(@IdRes fragmentHostId: Int): Fragment? {
    return supportFragmentManager.findFragmentById(fragmentHostId)
}

fun Fragment.getTopFragment(@IdRes fragmentHostId: Int): Fragment? {
    return requireActivity().getTopFragment(fragmentHostId)
}

fun Fragment.getTopChildFragment(@IdRes fragmentHostId: Int): Fragment? {
    return childFragmentManager.findFragmentById(fragmentHostId)
}

/**
 * Remove fragment
 */
fun FragmentActivity.removeFragment(fragment: Fragment?) {
    if (fragment == null) return
    try {
        supportFragmentManager.commit(allowStateLoss = true) {
            remove(fragment)
        }
        supportFragmentManager.executePendingTransactions()

        val fragmentCount = supportFragmentManager.fragments.size - 1
        if (fragmentCount >= 0) {
            for (i in fragmentCount downTo 0) {
                supportFragmentManager.fragments[i].also {
                    if ((it is SupportRequestManagerFragment).not()) {
                        if (it.isHidden) {
                            supportFragmentManager.commit(allowStateLoss = true) { show(it) }
                        }
                        return
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.removeFragment(fragment: Fragment?) {
    activity?.removeFragment(fragment)
}
