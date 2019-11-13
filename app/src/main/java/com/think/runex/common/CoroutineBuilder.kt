package com.think.runex.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

/**
 * Build main thread with new coroutine scope.
 * @param block the coroutine code
 */
fun launchOnMainThread(delay: Long = 0, block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        kotlinx.coroutines.delay(delay)
        block()
    }
}


/**
 * Build background thread with new coroutine scope.
 */
fun launchOnIoThread(block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        block()
    }
}


fun runOnMainThread(delay: Long = 0, block: () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        kotlinx.coroutines.delay(delay)
        block()
    }
}

/**
 * Build main thread with activity lifecycle scope.
 */
fun FragmentActivity.launchOnMainThread(delay: Long = 0, block: suspend CoroutineScope.() -> Unit) {
    if (isDestroyed) return
    lifecycleScope.launch(Dispatchers.Main) {
        kotlinx.coroutines.delay(delay)
        block()
    }
}

/**
 * Build background thread with activity lifecycle scope.
 */
fun FragmentActivity.launchOnIoThread(block: suspend CoroutineScope.() -> Unit) {
    if (isDestroyed) return
    lifecycleScope.launch(Dispatchers.IO) {
        block()
    }
}

fun FragmentActivity.runOnMainThread(block: () -> Unit) {
    if (isDestroyed) return
    lifecycleScope.launch(Dispatchers.Main) {
        block()
    }
}


/**
 * Cancel all children job in activity lifecycle scope.
 */
fun FragmentActivity.cancelCoroutines() {
    if (isDestroyed) return
    lifecycleScope.coroutineContext.cancelChildren()
}

/**
 * Build main thread with fragment lifecycle scope.
 */
fun Fragment.launchOnMainThread(delay: Long = 0, block: suspend CoroutineScope.() -> Unit) {
    if (view == null || isAdded.not()) return
    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
        kotlinx.coroutines.delay(delay)
        block()
    }
}

/**
 * Build background thread with fragment lifecycle scope.
 */
fun Fragment.launchOnIoThread(block: suspend CoroutineScope.() -> Unit) {
    if (view == null || isAdded.not()) return
    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
        block()
    }
}

fun Fragment.runOnMainThread(block: () -> Unit) {
    if (view == null || isAdded.not()) return
    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
        block()
    }
}

/**
 * Cancel all children job in fragment lifecycle scope.
 */
fun Fragment.cancelCoroutines() {
    if (view == null || isAdded.not()) return
    viewLifecycleOwner.lifecycleScope.coroutineContext.cancelChildren()
}

/**
 * Use run ui thread from fragment.
 */
fun Fragment.runOnUiThread(block: () -> Unit) = activity?.runOnUiThread {
    block()
}