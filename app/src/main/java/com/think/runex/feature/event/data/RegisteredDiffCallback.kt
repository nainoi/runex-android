package com.think.runex.feature.event.data

import androidx.recyclerview.widget.DiffUtil

class RegisteredDiffCallback : DiffUtil.ItemCallback<Registered>() {
    override fun areItemsTheSame(oldItem: Registered, newItem: Registered): Boolean {
        return oldItem.eventCode == newItem.eventCode
    }

    override fun areContentsTheSame(oldItem: Registered, newItem: Registered): Boolean {
        return oldItem == newItem
    }
}
