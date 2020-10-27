package com.think.runex.feature.user

import com.think.runex.datasource.api.ApiConfig

class UserUrl {
    companion object {
        const val PROFILE_PATH = "${ApiConfig.BASE_URL}/api/${ApiConfig.API_VERSION}/user"
    }
}