package com.think.runex.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jozzee.android.core.ui.setVisible
import com.think.runex.R
import com.think.runex.common.displayFormat
import com.think.runex.common.loadEventImage
import com.think.runex.feature.event.Event
import kotlinx.android.synthetic.main.list_item_event.view.*

class EventsAdapter : ListAdapter<Event, EventsAdapter.ViewHolder>(EventsListDiffCallback()) {

    private var onItemClick: ((position: Int, eventId: String) -> Unit)? = null

    fun setOnItemClick(block: (position: Int, eventId: String) -> Unit) {
        onItemClick = block
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class EventsListDiffCallback : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_event, parent, false))
        }

        fun bind(data: Event, onItemClick: ((position: Int, eventId: String) -> Unit)? = null) {
            itemView.imv_event?.loadEventImage(data.coverImage())
            itemView.tv_event_name.text = data.name
            itemView.tv_event_description.text = data.description
            itemView.tv_event_description.setVisible(data.description.isNotBlank())
            itemView.tv_event_time.text = data.startEvent

            when (data.ticket?.isNotEmpty() == true) {
                true -> itemView.btn_price.text = ("$${(data.ticket?.get(0)?.price
                        ?: 0f).displayFormat()}")
                false -> itemView.btn_price.text = ("$0.00")
            }

            //Set on click item
            itemView.setOnClickListener {
                onItemClick?.invoke(adapterPosition, data.id)
            }
        }
    }
}