package com.think.runex.common

import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.think.runex.util.GlideApp

fun ImageView.loadEventsImage(url: String?,
                              @DrawableRes defaultImage: Int? = null) {

    if (url.isNullOrBlank()) {
        if (defaultImage != null) {
            this.setImageResource(defaultImage)
        }
        return
    }
    post {
        GlideApp.with(this)
                .load(url)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(width, height)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(this)
                .clearOnDetach()
    }
}