package com.think.runex.component.recyclerview.swipemenu

import android.graphics.Color
import android.view.Gravity
import androidx.annotation.ColorInt

/**
 * Menu data class for swipe menu
 * [gravity] Gravity.START or Gravity.END.
 */
data class SwipeMenu(
    var id: Int = 0,
    var name: String = "",
    @ColorInt var backgroundColor: Int = Color.TRANSPARENT,
    var gravity: Int
)
