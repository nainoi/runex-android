package com.think.runex.feature.event

import com.google.gson.annotations.SerializedName
import com.think.runex.datasource.remote.ApiUrl
import com.think.runex.feature.product.Product
import com.think.runex.feature.running.Category
import com.think.runex.feature.ticket.Ticket

data class Event(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("description") var description: String = "",
        @SerializedName("body") var body: String = "",
        @SerializedName("captions") var captions: List<Caption>? = null,
        @SerializedName("cover") var coverImage: String = "",
        @SerializedName("category") var category: Category = Category(),
        @SerializedName("product") var products: List<Product>? = null,
        @SerializedName("ticket") var ticket: List<Ticket>? = null,
        @SerializedName("tags") var tags: List<Tag>? = null,
        @SerializedName("owner_id") var ownerId: String = "",
        @SerializedName("status") var status: String = "",
        @SerializedName("location") var location: String = "",
        @SerializedName("start_reg") var startReg: String = "",
        @SerializedName("end_reg") var endReg: String = "",
        @SerializedName("start_event") var startEvent: String = "",
        @SerializedName("end_event") var endEvent: String = "",
        @SerializedName("created_time") var createdAt: String = "",
        @SerializedName("updated_time") var updatedAt: String = "") {

    fun coverImage(): String = if (coverImage.isNotBlank()) "${ApiUrl.getBaseUrl()}$coverImage" else ""
}