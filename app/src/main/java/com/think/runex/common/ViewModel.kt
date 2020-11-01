package com.think.runex.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(viewModelFactory: ViewModelProvider.Factory? = null): T {
    return viewModelFactory?.let { ViewModelProvider(this, it).get(T::class.java) }
            ?: ViewModelProvider(this).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.getViewModel(viewModelFactory: ViewModelProvider.Factory? = null): T {
    return viewModelFactory?.let { ViewModelProvider(this, it).get(T::class.java) }
            ?: ViewModelProvider(this).get(T::class.java)
}

//internal fun <T : ViewModel> FragmentActivity.getViewModel(modelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory? = null): T {
//    return viewModelFactory?.let { ViewModelProvider(this, it).get(modelClass) }
//            ?: ViewModelProvider(this).get(modelClass)
//}
//
//internal fun <T : ViewModel> Fragment.getViewModel(modelClass: Class<T>, viewModelFactory: ViewModelProvider.Factory? = null): T {
//    return viewModelFactory?.let { ViewModelProvider(this, it).get(modelClass) }
//            ?: ViewModelProvider(this).get(modelClass)
//}