package com.think.runex.utility

import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.util.*
import kotlin.concurrent.schedule

fun runOnMainThread(delay: Long = 0, block: () -> Unit) {
    when (delay > 0) {
        true -> Timer().schedule(delay) {
            CoroutineScope(Main).launch { block() }
        }
        false -> CoroutineScope(Main).launch { block() }
    }
}

fun runOnIoThread(block: () -> Unit) = CoroutineScope(IO).launch {
    block()
}

fun Fragment.runOnUiThread(block: () -> Unit) = activity?.runOnUiThread {
    block()
}



