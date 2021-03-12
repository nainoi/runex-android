package com.think.runex.feature.event.registered

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.common.loadEventsImage
import com.think.runex.common.requireContext
import com.think.runex.feature.event.data.EventRegistered
import com.think.runex.feature.payment.data.PaymentStatus
import kotlinx.android.synthetic.main.list_item_my_event.view.*

class MyEventsAdapter : ListAdapter<EventRegistered, MyEventsAdapter.ViewHolder>(EventRegisteredDiffCallback()) {

    var onItemClickListener: ((event: EventRegistered) -> Unit)? = null

    @JvmName("setOnItemClickListenerJava")
    fun setOnItemClickListener(onItemClick: (event: EventRegistered) -> Unit) {
        onItemClickListener = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    class EventRegisteredDiffCallback : DiffUtil.ItemCallback<EventRegistered>() {
        override fun areItemsTheSame(oldItem: EventRegistered, newItem: EventRegistered): Boolean {
            return oldItem.eventCode == newItem.eventCode
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

        fun bind(data: EventRegistered?, onItemClick: ((event: EventRegistered) -> Unit)? = null) {

            val eventDetail = data?.getEventDetail()
            itemView.event_image?.loadEventsImage(eventDetail?.getCoverImage())
            itemView.event_name_label?.text = eventDetail?.title ?: ""

            val paymentStatus = data?.getPaymentStatus() ?: ""
            itemView.event_status_icon?.background = PaymentStatus.getPaymentStatusBackground(requireContext(), paymentStatus)
            itemView.event_status_label?.text = PaymentStatus.getPaymentStatusText(requireContext(), paymentStatus)

            itemView.list_item_event_registration?.setOnClickListener {
                data?.also { onItemClick?.invoke(it) }
            }
        }
    }
}