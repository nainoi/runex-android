package com.think.runex.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.findFragmentByTag(tag: String?): Fragment? {
    return supportFragmentManager.findFragmentByTag(tag)
}

fun Fragment.findFragmentByTag(tag: String?): Fragment? {
    return activity?.findFragmentByTag(tag)
}

fun Fragment.findChildFragmentByTag(tag: String?): Fragment? {
    return childFragmentManager.findFragmentByTag(tag)
}