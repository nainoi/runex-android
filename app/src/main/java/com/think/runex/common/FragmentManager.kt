package com.think.runex.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.jozzee.android.core.util.simpleName

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