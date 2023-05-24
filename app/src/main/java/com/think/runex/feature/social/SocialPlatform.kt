package com.think.runex.feature.social

import androidx.annotation.IntDef
import com.think.runex.feature.social.SocialPlatform.Companion.APPLE
import com.think.runex.feature.social.SocialPlatform.Companion.FACEBOOK
import com.think.runex.feature.social.SocialPlatform.Companion.GOOGLE
import com.think.runex.feature.social.SocialPlatform.Companion.LINE

@IntDef(value = [GOOGLE, FACEBOOK, LINE, APPLE])
annotation class SocialPlatform {
    companion object {
        const val GOOGLE = 1
        const val FACEBOOK = 2
        const val LINE = 3
        const val APPLE = 4

        fun platformText(@SocialPlatform platform: Int): String = when (platform) {
            GOOGLE -> "google"
            FACEBOOK -> "facebook"
            LINE -> "line"
            APPLE -> "apple"
            else -> ""
        }
    }
}