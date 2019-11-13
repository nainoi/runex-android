package com.think.runex.feature.auth

import com.think.runex.datasource.remote.ApiUrl

class AuthUrl {
    companion object {

        val REGISTER_PATH = "${ApiUrl.getBaseUrl()}/api/${ApiUrl.API_VERSION}/user/ep"

        val LOGIN_PATH = "${ApiUrl.getBaseUrl()}/api/${ApiUrl.API_VERSION}/user/login"
    }
}