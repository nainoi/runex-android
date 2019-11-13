package com.think.runex.common

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.resource.getDrawable
import com.jozzee.android.core.resource.getStrings

fun Fragment.getActionBar(): ActionBar? = (activity as AppCompatActivity).supportActionBar

fun Fragment.initialToolbar(toolbar: Toolbar,
                            @StringRes title: Int? = null,
                            @ColorRes titleColor: Int? = null,
                            @DrawableRes homeButton: Int? = null) {

    val titleString: String? = if (title != null) getStrings(title) else null
    val titleColorInt: Int? = if (titleColor != null) getColor(titleColor) else null
    val iconHome: Drawable? = if (homeButton != null) getDrawable(homeButton) else null
    initialToolbar(toolbar, titleString, titleColorInt, iconHome)
}

fun Fragment.initialToolbar(toolbar: Toolbar,
                            title: String? = null,
                            @ColorInt titleColor: Int? = null,
                            homeButton: Drawable? = null) {
    if (activity == null || view == null) return

    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    setToolbarTitle(title, titleColor /*,toolbar.findViewById(R.id.toolbar_title)*/)
    setToolbarHomeButton(homeButton)
}

fun Fragment.setToolbarHomeButton(homeButton: Drawable? = null) = getActionBar()?.run {
    if (activity == null || view == null) return@run
    setHomeButtonEnabled(homeButton != null)
    setDisplayHomeAsUpEnabled(homeButton != null)
    setHomeAsUpIndicator(homeButton)
}

/**
 * Support custom title such as toolbar that have a center title.
 * but if not have custom title will be use title from action bar.
 */
fun Fragment.setToolbarTitle(title: String?,
                             @ColorInt titleColor: Int? = null,
                             customTextViewTitle: TextView? = null) = getActionBar()?.run {
    if (activity == null || view == null) return@run
    when (customTextViewTitle != null) {
        true -> {
            this.setDisplayShowTitleEnabled(false)
            this.title = ""
            customTextViewTitle.text = title ?: ""
            titleColor?.also {
                customTextViewTitle.setTextColor(it)
            }
        }
        false -> {
            this.setDisplayShowTitleEnabled(title != null)
            this.title = title ?: ""
        }
    }
}