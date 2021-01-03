package com.think.runex.ui.event

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
import com.think.runex.feature.event.model.Event
import com.think.runex.feature.event.model.registered.RegisteredEvent
import kotlinx.android.synthetic.main.list_item_my_event.view.*

class MyEventsAdapter : ListAdapter<RegisteredEvent, MyEventsAdapter.ViewHolder>(MyEventsAdapter.EventsListDiffCallback()) {

    private var onItemClick: ((position: Int, event: RegisteredEvent) -> Unit)? = null

    fun setOnItemClick(block: (position: Int, event: RegisteredEvent) -> Unit) {
        onItemClick = block
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class EventsListDiffCallback : DiffUtil.ItemCallback<RegisteredEvent>() {
        override fun areItemsTheSame(oldItem: RegisteredEvent, newItem: RegisteredEvent): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        override fun areContentsTheSame(oldItem: RegisteredEvent, newItem: RegisteredEvent): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_my_event, parent, false))
        }

        fun bind(data: RegisteredEvent?, onItemClick: ((position: Int, event: RegisteredEvent) -> Unit)? = null) {
            data?.registerInfoList?.get(0)?.event?.also { event ->
                itemView.event_image?.loadEventsImage(event.coverImage())
                itemView.event_name_label?.text = event.name
                when (event.isActive) {
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
}