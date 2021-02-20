package com.think.runex.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun Fragment.showBottomSheet(bottomSheet: BottomSheetDialogFragment) {
    if (isAdded.not() || view == null) return
    try {
        bottomSheet.show(childFragmentManager, "${bottomSheet::class.java.simpleName}${System.currentTimeMillis()}")
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    }

//    childFragmentManager.commit(allowStateLoss = true) {
//        add(bottomSheet, "${bottomSheet::class.java.simpleName}${System.currentTimeMillis()}")
//    }
}

fun FragmentActivity.showBottomSheet(bottomSheet: BottomSheetDialogFragment) {
    if (isDestroyed) return
    try {
        bottomSheet.show(supportFragmentManager, "${bottomSheet::class.java.simpleName}${System.currentTimeMillis()}")
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    }

//    supportFragmentManager.commit(allowStateLoss = true) {
//        add(bottomSheet, "${bottomSheet::class.java.simpleName}${System.currentTimeMillis()}")
//    }
}