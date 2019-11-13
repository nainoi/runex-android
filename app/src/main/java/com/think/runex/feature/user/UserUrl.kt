package com.think.runex.feature.user

import com.think.runex.datasource.remote.ApiUrl

class UserUrl {
    companion object{
        val PROFILE_PATH = "${ApiUrl.getBaseUrl()}/api/${ApiUrl.API_VERSION}/user"
    }
}