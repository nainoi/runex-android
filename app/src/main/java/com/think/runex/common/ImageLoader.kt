package com.think.runex.common

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jozzee.android.core.resource.getDimen
import com.think.runex.R
import com.think.runex.utility.GlideApp

fun ImageView.loadEventImage(url: String,
                             @DrawableRes defaultImage: Int? = null,
                             clearOnDetach: Boolean = true) {

    if (url.isBlank()) {
        if (defaultImage != null) {
            this.setImageResource(defaultImage)
        }
        return
    }
    GlideApp.with(this)
            .load(url)
            .format(DecodeFormat.PREFER_ARGB_8888)
            .transform(CenterCrop(), RoundedCorners(getDimen(R.dimen.space_8dp)))
            .apply {
                if (defaultImage != null) {
                    placeholder(defaultImage)
                }
                if (defaultImage != null) {
                    error(defaultImage)
                }
            }
            .into(this)
            .also {
                if (clearOnDetach) {
                    it.clearOnDetach()
                }
            }
}