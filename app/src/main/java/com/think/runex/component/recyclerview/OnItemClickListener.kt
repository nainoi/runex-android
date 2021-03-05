package com.think.runex.component.recyclerview

interface OnItemClickListener {
    fun <T> onItemClick(position: Int, data: T? = null)
}