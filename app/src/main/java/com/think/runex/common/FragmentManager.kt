package com.think.runex.common

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import com.jozzee.android.core.util.simpleName

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
    supportFragmentManager.commit(allowStateLoss = true) {
        remove(fragment)
    }
    supportFragmentManager.executePendingTransactions()
}
