package com.think.runex.feature.event

import com.think.runex.datasource.api.ApiConfig

class EventUrl {
    companion object {
        const val ALL_EVENT_PATH = "${ApiConfig.BASE_URL}/api/{${ApiConfig.API_VERSION}}/event/all"
        const val ALL_EVENT_BY_STATUS_PATH = "${ApiConfig.BASE_URL}/api/{${ApiConfig.API_VERSION}}/event/all"
    }
}