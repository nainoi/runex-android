package com.think.runex.common

import android.app.Activity
import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.RequestOptions
import com.think.runex.R

fun ImageView.loadImage(source: Any?,
                        scaleType: ImageView.ScaleType? = null,
                        isOverrideSize: Boolean = false,
                        @DrawableRes defaultImage: Int? = null
        /*diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE*/) {
    if (context == null) return
    if (source == null) {
        if (defaultImage != null) {
            setImageResource(defaultImage)
        }
        return
    }

    post {
        if (context is Activity) {
            val activity = (context as Activity)
            if (activity.isDestroyed || activity.isFinishing) {
                return@post
            }
        }

        Glide.with(this)
                .load(source)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .apply {
                    //TODO("Wait other scale type")
                    when (scaleType) {
                        ImageView.ScaleType.CENTER_CROP -> apply(RequestOptions.centerCropTransform())
                        ImageView.ScaleType.CENTER_INSIDE -> apply(RequestOptions.centerInsideTransform())
                        ImageView.ScaleType.FIT_CENTER -> apply(RequestOptions.fitCenterTransform())
                        else -> {
                        }
                    }

                    if (isOverrideSize) {
                        apply(RequestOptions.overrideOf(width, height))
                    }

                    if (defaultImage != null) {
                        error(defaultImage)
                    }
                }
                //.diskCacheStrategy(diskCacheStrategy)
                .into(this)
                .clearOnDetach()
    }
}

fun ImageView.loadEventsImage(url: String?,
                              @DrawableRes defaultImage: Int? = null) {

    if (context == null) return
    if (url.isNullOrBlank()) {
        if (defaultImage != null) {
            this.setImageResource(defaultImage)
        }
        return
    }
    post {
        Glide.with(this)
                .load(url)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(width, height)
                .centerCrop()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(this)
                .clearOnDetach()
    }
}

fun ImageView.loadProfileImage(url: String?) {
    if (context == null) return
    if (url.isNullOrEmpty()) {
        setImageResource(R.mipmap.ic_profile)
        return
    }
    post {
        Glide.with(this)
                .load(url)
                .placeholder(R.mipmap.ic_profile)
                .format(DecodeFormat.PREFER_ARGB_8888)
                .override(width, height)
                .circleCrop()
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(this)
                .clearOnDetach()
    }
}