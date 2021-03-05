package com.think.runex.feature.event.all

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.common.loadEventsImage
import com.think.runex.feature.event.data.EventItem
import kotlinx.android.synthetic.main.list_item_all_event.view.*

class AllEventsAdapter : ListAdapter<EventItem, AllEventsAdapter.ViewHolder>(EventsListDiffCallback()) {

    var onItemClickListener: ((position: Int, event: EventItem) -> Unit)? = null

    @JvmName("setOnItemClickListenerJava")
    fun setOnItemClickListener(onItemClickListener: (position: Int, event: EventItem) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    class EventsListDiffCallback : DiffUtil.ItemCallback<EventItem>() {
        override fun areItemsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: EventItem, newItem: EventItem): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_all_event, parent, false))
        }

        fun bind(data: EventItem?, onItemClick: ((position: Int, event: EventItem) -> Unit)? = null) {
            itemView.event_image?.loadEventsImage(data?.coverImage())
            itemView.event_title_label?.text = data?.title ?: ""
            itemView.event_date_label?.text = data?.eventDateDisplay ?: ""

            itemView.list_item_all_event?.setOnClickListener {
                data?.also {
                    onItemClick?.invoke(adapterPosition, it)
                }
            }
        }
    }
}