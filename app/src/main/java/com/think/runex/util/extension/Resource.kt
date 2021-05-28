package com.think.runex.util.extension

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jozzee.android.core.resource.getColor

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
@ColorInt
fun Context.getColorAttr(@AttrRes colorId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(colorId, typedValue, true)
    return typedValue.data
}


fun Context.getDrawable(@DrawableRes drawableId: Int, @ColorRes filterColor: Int? = null): Drawable? {
    return ContextCompat.getDrawable(this, drawableId).apply {
        if (this != null && filterColor != null) {
            setColorFilter(ContextCompat.getColor(this@getDrawable, filterColor))
        }
    }
}

fun Fragment.getDrawable(@DrawableRes drawableId: Int, @ColorRes filterColor: Int? = null): Drawable? {
    return ContextCompat.getDrawable(requireContext(), drawableId).apply {
        if (this != null && filterColor != null) {
            setColorFilter(getColor(filterColor))
        }
    }
}

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