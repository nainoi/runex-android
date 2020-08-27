package com.think.runex.common

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.jozzee.android.core.resource.getDimension
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.think.runex.R
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

fun ImageView.loadEventImage(url: String,
                             @DrawableRes defaultImage: Int? = null,
                             skipMemoryCache: Boolean = false) {

    if (url.isBlank()) {
        if (defaultImage != null) {
            this.setImageResource(defaultImage)
        }
        return
    }
    Picasso.get()
            .load(url)
            .apply {
                if (defaultImage != null) {
                    placeholder(defaultImage)
                }
                if (defaultImage != null) {
                    error(defaultImage)
                }
            }
            .transform(RoundedCornersTransformation(getDimension(R.dimen.space_8dp), 0))
            .apply {
                if (skipMemoryCache) {
                    memoryPolicy(MemoryPolicy.NO_CACHE)
                }
            }
            .into(this)
}