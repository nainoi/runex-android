package com.think.runex.ui.component.recyclerview

interface OnItemClickListener {
    fun <T> onItemClick(position: Int, data: T? = null)
}