package com.think.runex.common

import android.graphics.drawable.Drawable
import android.widget.TextView
import androidx.annotation.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.resource.getDrawable
import com.think.runex.R

fun FragmentActivity.setupToolbar(toolbar: Toolbar) {
    if (isDestroyed || isFinishing) return
    (this as AppCompatActivity).setSupportActionBar(toolbar)
}

fun FragmentActivity.setupToolbar(toolbar: Toolbar,
                                  @StringRes title: Int? = null,
                                  @DrawableRes homeButton: Int? = null,
                                  @StyleRes titleTextAppearance: Int? = null) {
    if (isDestroyed || isFinishing) return
    val titleText: String? = if (title != null) getString(title) else null
    val homeDrawable: Drawable? = if (homeButton != null) ContextCompat.getDrawable(this, homeButton) else null
    setupToolbar(toolbar, titleText, homeDrawable, titleTextAppearance)
}

fun FragmentActivity.setupToolbar(toolbar: Toolbar,
                                  title: String? = null,
                                  homeButton: Drawable? = null,
                                  @StyleRes titleTextAppearance: Int? = null) {
    if (isDestroyed || isFinishing) return
    (this as AppCompatActivity).setSupportActionBar(toolbar)
    setToolbarTitle(title, toolbar.findViewById(R.id.toolbar_title), titleTextAppearance)
    setToolbarHomeButton(homeButton)
}

fun FragmentActivity.setToolbarHomeButton(homeButton: Drawable? = null) {
    if (isDestroyed || isFinishing) return
    (this as AppCompatActivity).supportActionBar?.also { actionBar ->
        actionBar.setHomeButtonEnabled(homeButton != null)
        actionBar.setDisplayHomeAsUpEnabled(homeButton != null)
        actionBar.setHomeAsUpIndicator(homeButton)
    }
}

fun FragmentActivity.setToolbarTitle(title: String?,
                                     customTitle: TextView? = null,
                                     @StyleRes titleTextAppearance: Int? = null) {
    if (isDestroyed || isFinishing) return
    (this as AppCompatActivity).supportActionBar?.also { actionBar ->
        if (customTitle != null) {
            actionBar.setDisplayShowTitleEnabled(false)
            actionBar.title = ""

            customTitle.text = title ?: ""
            if (titleTextAppearance != null) {
                customTitle.setTextAppearance(titleTextAppearance)
            }
        } else {
            actionBar.setDisplayShowTitleEnabled(title?.isNotBlank() == true)
            actionBar.title = title ?: ""
        }
    }
}


fun Fragment.getActionBar(): ActionBar? = (activity as AppCompatActivity).supportActionBar

fun Fragment.setupToolbar(toolbar: Toolbar) {
    if (view == null || isAdded.not()) return
    activity?.setupToolbar(toolbar)
}

fun Fragment.setupToolbar(toolbar: Toolbar,
                          @StringRes title: Int? = null,
                          @DrawableRes homeButton: Int? = null,
                          @StyleRes titleTextAppearance: Int? = null) {
    if (activity == null || view == null) return
    activity?.setupToolbar(toolbar, title, homeButton, titleTextAppearance)
}

fun Fragment.setupToolbarDarkBackground(toolbar: Toolbar,
                                        @StringRes title: Int? = null,
                                        @DrawableRes homeButton: Int? = null) {
    if (activity == null || view == null) return
    val toolbarTitle = title?.let { getString(it) }
    val toolbarHomeButton = homeButton?.let { getDrawable(it) }
    toolbarHomeButton?.setColorFilter(getColor(R.color.iconColorPrimaryInDarkBackground))
    activity?.setupToolbar(toolbar, toolbarTitle, toolbarHomeButton, R.style.TextTitle_Primary_InDarkBackground)
}

fun Fragment.setupToolbar(toolbar: Toolbar,
                          title: String? = null,
                          homeButton: Drawable? = null,
                          @StyleRes titleTextAppearance: Int? = null) {
    if (activity == null || view == null) return
    activity?.setupToolbar(toolbar, title, homeButton, titleTextAppearance)
}

fun Fragment.setToolbarHomeButton(homeButton: Drawable? = null) = getActionBar()?.also { actionBar ->
    if (activity == null || view == null) return@also
    activity?.setToolbarHomeButton(homeButton)
}

fun Fragment.setToolbarTitle(title: String?,
                             customTitle: TextView? = null,
                             @StyleRes titleTextAppearance: Int? = null) {
    if (activity == null || view == null) return
    activity?.setToolbarTitle(title, customTitle, titleTextAppearance)
}