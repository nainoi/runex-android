package com.think.runex.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.common.loadEventsImage
import com.think.runex.common.requireContext
import com.think.runex.common.toJson
import com.think.runex.feature.event.model.Event
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
            Log.e("Jozzee","data: ${data.toJson()}")
            itemView.event_image?.loadEventsImage(data.coverImage())
            itemView.event_name_label?.text = data.name
            itemView.event_category_label?.text = data.category ?: ""
            itemView.event_date_label?.text = data.eventPeriod(requireContext())
            itemView.register_date_label?.text = data.registerPeriod(requireContext())

            itemView.list_item_event?.setOnClickListener {
                onItemClick?.invoke(adapterPosition, data.id)
            }
        }
    }
}