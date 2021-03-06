package com.think.runex.feature.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.common.getString
import com.think.runex.feature.event.data.Ticket
import kotlinx.android.synthetic.main.list_item_ticket_type.view.*

class TicketsAdapter(private val isClickable: Boolean = false) : ListAdapter<Ticket, TicketsAdapter.ViewHolder>(TicketTypeDiffCallback()) {

    var onItemClickListener: ((ticket: Ticket) -> Unit)? = null

    @JvmName("setOnItemClickListenerJava")
    fun setOnItemClickListener(onItemClickListener: (ticket: Ticket) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    class TicketTypeDiffCallback : DiffUtil.ItemCallback<Ticket>() {
        override fun areItemsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ticket, newItem: Ticket): Boolean {
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

        fun bind(data: Ticket?, onItemClick: ((ticket: Ticket) -> Unit)? = null) {
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