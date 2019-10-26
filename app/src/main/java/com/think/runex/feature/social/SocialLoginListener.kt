package com.think.runex.feature.social


import com.think.runex.feature.user.UserProvider
import java.lang.Exception

interface SocialLoginListener {
    fun onLoginWithSocialCompleted(@SocialPlatform platform: Int, userProvider: UserProvider)
    fun onLoginWithSocialCancel()
    fun onLoginWithSocialError(exception: Exception)
}