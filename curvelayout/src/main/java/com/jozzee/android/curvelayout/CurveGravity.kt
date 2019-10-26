package com.jozzee.android.curvelayout

import androidx.annotation.IntDef
import com.jozzee.android.curvelayout.CurveGravity.Companion.BOTTOM
import com.jozzee.android.curvelayout.CurveGravity.Companion.LEFT
import com.jozzee.android.curvelayout.CurveGravity.Companion.RIGHT
import com.jozzee.android.curvelayout.CurveGravity.Companion.TOP


@IntDef(value = [LEFT, TOP, RIGHT, BOTTOM])
@Retention(AnnotationRetention.SOURCE)
annotation class CurveGravity {
    companion object {
        const val LEFT = 1
        const val TOP = 2
        const val RIGHT = 3
        const val BOTTOM = 4
    }
}