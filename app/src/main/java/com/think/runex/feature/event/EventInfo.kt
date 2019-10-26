package com.think.runex.feature.event

import com.google.gson.annotations.SerializedName

data class EventInfo(@SerializedName("event") var event: Event = Event())