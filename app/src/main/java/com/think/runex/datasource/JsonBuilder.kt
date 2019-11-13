package com.think.runex.datasource

import org.json.JSONObject

class JsonBuilder {
    private var jsonObject: JSONObject? = null

    init {
        jsonObject = JSONObject()
    }

    fun add(key: String, value: String): JsonBuilder {
        jsonObject?.put(key, value)
        return this
    }

    fun build(): String = jsonObject?.toString() ?: ""

    fun clear() {
        if (jsonObject == null) return
        while (jsonObject?.keys()?.hasNext() == true) {
            jsonObject?.keys()?.next()?.also { key ->
                jsonObject?.remove(key)
            }
        }
    }
}