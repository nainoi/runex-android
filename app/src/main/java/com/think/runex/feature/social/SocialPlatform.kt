package com.think.runex.feature.social

import androidx.annotation.IntDef
import com.think.runex.feature.social.SocialPlatform.Companion.FACEBOOK
import com.think.runex.feature.social.SocialPlatform.Companion.GOOGLE

@IntDef(value = [GOOGLE, FACEBOOK])
annotation class SocialPlatform {
    companion object {
        const val GOOGLE = 1
        const val FACEBOOK = 2

        fun platformText(@SocialPlatform platform: Int): String = when (platform) {
            GOOGLE -> "Google"
            FACEBOOK -> "Facebook"
            else -> ""
        }
    }
}