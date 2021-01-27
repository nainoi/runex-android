package com.think.runex.ui.event.registered

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.common.getDrawable
import com.think.runex.common.getString
import com.think.runex.common.loadEventsImage
import com.think.runex.feature.event.model.EventRegistered
import kotlinx.android.synthetic.main.list_item_my_event.view.*

class MyEventsAdapter : ListAdapter<EventRegistered, MyEventsAdapter.ViewHolder>(EventsListDiffCallback()) {

    private var onItemClick: ((position: Int, event: EventRegistered) -> Unit)? = null

    fun setOnItemClick(block: (position: Int, event: EventRegistered) -> Unit) {
        onItemClick = block
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class EventsListDiffCallback : DiffUtil.ItemCallback<EventRegistered>() {
        override fun areItemsTheSame(oldItem: EventRegistered, newItem: EventRegistered): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EventRegistered, newItem: EventRegistered): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_my_event, parent, false))
        }

        fun bind(data: EventRegistered?, onItemClick: ((position: Int, event: EventRegistered) -> Unit)? = null) {

            itemView.event_image?.loadEventsImage(data?.coverImage)
            itemView.event_name_label?.text = data?.name ?: ""
            when (data?.isActive) {
                true -> {
                    itemView.event_status_icon?.background = getDrawable(R.drawable.shape_circle_accent)
                    itemView.event_status_label?.text = getString(R.string.active)
                }
                false -> {
                    itemView.event_status_icon?.background = getDrawable(R.drawable.shape_circle_disable)
                    itemView.event_status_label?.text = getString(R.string.passed)
                }
            }

        }
    }
}