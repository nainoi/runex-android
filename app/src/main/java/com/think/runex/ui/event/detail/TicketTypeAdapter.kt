package com.think.runex.ui.event.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.common.requireContext
import com.think.runex.feature.event.model.EventItem
import com.think.runex.feature.event.model.TicketEventDetail
import com.think.runex.ui.event.all.AllEventsAdapter
import kotlinx.android.synthetic.main.list_item_ticket_type.view.*

class TicketTypeAdapter : ListAdapter<TicketEventDetail, TicketTypeAdapter.ViewHolder>(TicketTypeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TicketTypeDiffCallback : DiffUtil.ItemCallback<TicketEventDetail>() {
        override fun areItemsTheSame(oldItem: TicketEventDetail, newItem: TicketEventDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TicketEventDetail, newItem: TicketEventDetail): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_ticket_type, parent, false))
        }

        fun bind(data: TicketEventDetail?) {
            itemView.title_label?.text = data?.getTitle(requireContext().getString(R.string.km))
                    ?: ""
            itemView.price_label?.text = data?.getPrice() ?: ""
        }
    }
}