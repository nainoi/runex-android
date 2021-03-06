package com.think.runex.feature.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.feature.event.data.Shirt
import kotlinx.android.synthetic.main.list_item_shirt_size.view.*

class ShirtsAdapter : ListAdapter<Shirt, ShirtsAdapter.ViewHolder>(ShirtsDiffCallback()) {

    var onItemClickListener: ((shirts: Shirt) -> Unit)? = null

    @JvmName("setOnItemClickListenerJava")
    fun setOnItemClickListener(onItemClickListener: (shirts: Shirt) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder.create(parent)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    class ShirtsDiffCallback : DiffUtil.ItemCallback<Shirt>() {
        override fun areItemsTheSame(oldItem: Shirt, newItem: Shirt): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Shirt, newItem: Shirt): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_shirt_size, parent, false))
        }

        fun bind(data: Shirt?, onItemClick: ((shirts: Shirt) -> Unit)? = null) {
            itemView.shirt_size_label?.text = data?.size ?: ""

            itemView.shirt_size_label?.setOnClickListener {
                data?.also { onItemClick?.invoke(it) }
            }
        }
    }
}