package com.think.runex.feature.event.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.common.getString
import com.think.runex.common.requireContext
import com.think.runex.feature.event.data.EventItem
import com.think.runex.feature.event.data.TicketEventDetail
import kotlinx.android.synthetic.main.list_item_ticket_type.view.*

class TicketTypeAdapter(private val isClickable: Boolean = false) : ListAdapter<TicketEventDetail, TicketTypeAdapter.ViewHolder>(TicketTypeDiffCallback()) {

    var onItemClickListener: ((ticket: TicketEventDetail) -> Unit)? = null

    @JvmName("setOnItemClickListenerJava")
    fun setOnItemClickListener(onItemClickListener: (ticket: TicketEventDetail) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    class TicketTypeDiffCallback : DiffUtil.ItemCallback<TicketEventDetail>() {
        override fun areItemsTheSame(oldItem: TicketEventDetail, newItem: TicketEventDetail): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TicketEventDetail, newItem: TicketEventDetail): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        //companion object {
        //    fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
        //            .inflate(R.layout.list_item_ticket_type, parent, false))
        //}

        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_ticket_type, parent, false))

        fun bind(data: TicketEventDetail?, onItemClick: ((ticket: TicketEventDetail) -> Unit)? = null) {
            itemView.title_label?.text = data?.getTitle(getString(R.string.km)) ?: ""
            itemView.price_label?.text = data?.getPrice() ?: ""

            when (isClickable) {
                true -> {
                    itemView.list_item_ticket?.isClickable = true
                    itemView.list_item_ticket?.setOnClickListener {
                        data?.also { onItemClick?.invoke(it) }
                    }
                }
                false -> itemView.list_item_ticket?.isClickable = false
            }
        }
    }
}