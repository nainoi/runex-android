package com.think.runex.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*


/**
 * Build main thread with new coroutine scope.
 * @param block the coroutine code
 */
fun launchMainThread(delay: Long = 0, block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.Main).launch {
        delay(delay)
        block()
    }
}


/**
 * Build background thread with new coroutine scope.
 */
fun launchIoThread(block: suspend CoroutineScope.() -> Unit): Job {
    return CoroutineScope(Dispatchers.IO).launch {
        block()
    }
}

fun FragmentActivity.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return lifecycleScope.launch {
        block()
    }
}

/**
 * Build main thread with activity lifecycle scope.
 */
fun FragmentActivity.launchMainThread(delay: Long = 0, block: suspend CoroutineScope.() -> Unit): Job {
    //if (isDestroyed) return
    return lifecycleScope.launch(Dispatchers.Main) {
        kotlinx.coroutines.delay(delay)
        block()
    }
}

/**
 * Build background thread with activity lifecycle scope.
 */
fun FragmentActivity.launchIoThread(block: suspend CoroutineScope.() -> Unit): Job {
    //if (isDestroyed) return
    return lifecycleScope.launch(Dispatchers.IO) {
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

fun Fragment.launch(block: suspend CoroutineScope.() -> Unit): Job {
    return viewLifecycleOwner.lifecycleScope.launch {
        block()
    }
}

/**
 * Build main thread with fragment lifecycle scope.
 */
fun Fragment.launchMainThread(delay: Long = 0, block: suspend CoroutineScope.() -> Unit): Job {
    //if (view == null || isAdded.not()) return
    return viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
        kotlinx.coroutines.delay(delay)
        block()
    }
}

/**
 * Build background thread with fragment lifecycle scope.
 */
fun Fragment.launchIoThread(block: suspend CoroutineScope.() -> Unit): Job {
    //if (view == null || isAdded.not()) return
    return viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
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

fun ViewModel.launchMainThread(delay: Long = 0, block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(Dispatchers.Main) {
        kotlinx.coroutines.delay(delay)
        block()
    }
}

fun ViewModel.launchIoThread(block: suspend CoroutineScope.() -> Unit): Job {
    return viewModelScope.launch(Dispatchers.Main) {
        block()
    }
}

fun ViewModel.cancelJobs() {
    viewModelScope.coroutineContext.cancelChildren()
}