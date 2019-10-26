package com.jozzee.android.curvelayout

import androidx.annotation.IntDef
import com.jozzee.android.curvelayout.CurveFormat.Companion.CONCAVE
import com.jozzee.android.curvelayout.CurveFormat.Companion.CONVEX

@IntDef(value = [CONVEX, CONCAVE])
annotation class CurveFormat {
    companion object {
        const val CONVEX = 1
        const val CONCAVE = 2
    }
}