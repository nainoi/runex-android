package com.think.runex.common

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


fun RecyclerView.ViewHolder.requireContext(): Context {
    return itemView.context
}

@ColorInt
fun RecyclerView.ViewHolder.getColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(itemView.context, id)
}

fun RecyclerView.ViewHolder.getDrawable(@DrawableRes drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(itemView.context, drawableId)
}

fun RecyclerView.ViewHolder.getDimension(@DimenRes id: Int): Float {
    return itemView.resources.getDimension(id)
}

fun RecyclerView.ViewHolder.getString(@StringRes stringId: Int): String {
    return itemView.resources.getString(stringId)
}