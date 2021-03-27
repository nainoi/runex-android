package com.think.runex.feature.payment.data.response

import com.google.gson.annotations.SerializedName
import com.think.runex.config.KEY_DATA
import com.think.runex.feature.payment.data.QRCodeImage

class QRCodeImageResult {
    @SerializedName("status")
    var status: Status? = null

    @SerializedName(KEY_DATA)
    var data: QRCodeImage? = null

    data class Status(
            @SerializedName("code") var code: Int? = null,
            @SerializedName("description") var description: String? = null)
}

