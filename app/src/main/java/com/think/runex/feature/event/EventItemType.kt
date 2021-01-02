package com.think.runex.feature.event

import androidx.annotation.IntDef
import com.think.runex.feature.event.EventItemType.Companion.MY_EVENT
import com.think.runex.feature.event.EventItemType.Companion.NORMAL

@IntDef(value = [NORMAL, MY_EVENT])
annotation class EventItemType {
    companion object {
        const val NORMAL = 0
        const val MY_EVENT = 1
    }
}