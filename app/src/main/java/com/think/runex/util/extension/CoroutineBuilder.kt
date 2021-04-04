package com.think.runex.util.extension

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun launch(context: CoroutineContext = EmptyCoroutineContext,
           block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(context).launch {
        block()
    }
}


fun FragmentActivity.launch(context: CoroutineContext = EmptyCoroutineContext,
                            block: suspend CoroutineScope.() -> Unit): Job {
    return lifecycleScope.launch(context) {
        block()
    }
}

/**
 * Cancel all children job in activity lifecycle scope.
 */
fun FragmentActivity.cancelJobs() {
    if (isDestroyed) return
    lifecycleScope.coroutineContext.cancelChildren()
}

fun Fragment.launch(context: CoroutineContext = EmptyCoroutineContext,
                    block: suspend CoroutineScope.() -> Unit): Job {
    return viewLifecycleOwner.lifecycleScope.launch(context) {
        block()
    }
}

/**
 * Cancel all children job in fragment lifecycle scope.
 */
fun Fragment.cancelJobs() {
    if (view == null || isAdded.not()) return
    viewLifecycleOwner.lifecycleScope.coroutineContext.cancelChildren()
}

/**
 * Use run ui thread from fragment.
 */
fun Fragment.runOnUiThread(block: () -> Unit) = activity?.runOnUiThread {
    block()
}

fun ViewModel.launch(context: CoroutineContext = EmptyCoroutineContext,
                     block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(context) {
        block()
    }
}

fun ViewModel.cancelJobs() {
    viewModelScope.coroutineContext.cancelChildren()
}