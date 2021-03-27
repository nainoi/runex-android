package com.think.runex.feature.event.data

import androidx.recyclerview.widget.DiffUtil

class EventRegisteredDiffCallback : DiffUtil.ItemCallback<EventRegistered>() {
    override fun areItemsTheSame(oldItem: EventRegistered, newItem: EventRegistered): Boolean {
        return oldItem.eventCode == newItem.eventCode
    }

    override fun areContentsTheSame(oldItem: EventRegistered, newItem: EventRegistered): Boolean {
        return oldItem == newItem
    }
}
