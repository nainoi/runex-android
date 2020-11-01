package com.think.runex.feature.event.model

import com.google.gson.annotations.SerializedName

data class Partner(
        @SerializedName("partner_id") var partnerId: String = "",
        @SerializedName("partner_name") var partnerName: String = "",
        @SerializedName("slug") var slug: String = "",
        @SerializedName("ref_event_key") var refEventKey: String = "",
        @SerializedName("ref_activity_key") var refActivityKey: String = "")