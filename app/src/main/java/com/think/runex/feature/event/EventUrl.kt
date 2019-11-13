package com.think.runex.feature.event

import com.think.runex.datasource.remote.ApiUrl

class EventUrl {
    companion object {
        val ALL_EVENT_PATH = "${ApiUrl.getBaseUrl()}/api/{${ApiUrl.API_VERSION}}/event/all"
        val ALL_EVENT_BY_STATUS_PATH = "${ApiUrl.getBaseUrl()}/api/{${ApiUrl.API_VERSION}}/event/all"
    }
}