package com.think.runex.component.recyclerview.swipemenu

import androidx.annotation.IntDef

@IntDef(value = [SwipeState.GONE, SwipeState.START_VISIBLE, SwipeState.END_VISIBLE])
annotation class SwipeState {
    companion object {
        const val GONE = 0
        const val START_VISIBLE = 1
        const val END_VISIBLE = 2
    }
}