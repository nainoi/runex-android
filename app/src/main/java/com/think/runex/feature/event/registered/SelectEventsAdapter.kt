package com.think.runex.feature.event.registered

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.feature.event.data.EventRegistered
import com.think.runex.feature.event.data.EventRegisteredDiffCallback
import com.think.runex.feature.event.data.EventRegisteredForSubmitResult
import com.think.runex.feature.event.data.Ticket
import kotlinx.android.synthetic.main.list_item_event_selection.view.*

class SelectEventsAdapter : ListAdapter<EventRegistered, SelectEventsAdapter.ViewHolder>(EventRegisteredDiffCallback()) {

    private var selectedList: ArrayList<Int>? = null

    private var onSelectionChange: ((selectedSize: Int) -> Unit)? = null

    fun setOnSelectionChange(block: ((selectedSize: Int) -> Unit)?) {
        onSelectionChange = block
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder.create(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.also { event ->
            holder.bind(event.getEventName(), selectedList?.find { it == position } != null, ::onItemClick)
        }
    }

    private fun onItemClick(position: Int) {
        if (selectedList?.find { it == position } != null) {
            //Is have selected will bee remove from selected
            selectedList?.remove(position)
            notifyItemChanged(position)
        } else {
            //Not have a event code, will be add to selected
            if (selectedList == null) {
                selectedList = ArrayList()
            }
            selectedList?.add(position)
            notifyItemChanged(position)
        }

        onSelectionChange?.invoke(selectedList?.size ?: 0)
    }

    fun getEventSelectedList(): List<EventRegisteredForSubmitResult> {
        val list = ArrayList<EventRegisteredForSubmitResult>()
        selectedList?.forEach { position ->
            getItem(position).also { event ->
                list.add(EventRegisteredForSubmitResult(
                        eventCode = event.getEventCode(),
                        ticket = event.getTicketAtRegister() ?: Ticket(),
                        orderId = event.getOrderId(),
                        regId = event.getRegisterId(),
                        parentRegId = event.getParentRegisterId()))
            }
        }
        return list
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_event_selection, parent, false))
        }

        fun bind(eventName: String, isSelected: Boolean, onItemClick: ((position: Int) -> Unit)? = null) {

            itemView.checkbox_button?.setImageResource(if (isSelected) R.mipmap.ic_checkbox_checked else R.mipmap.ic_checkbox_normal)
            itemView.event_name_label?.text = eventName

            itemView.checkbox_button?.setOnClickListener {
                onItemClick?.invoke(adapterPosition)
            }
        }
    }
}