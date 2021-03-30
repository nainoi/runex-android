package com.think.runex.feature.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.feature.dashboard.data.DashboardInfo

class DashboardAdapter(private val recyclerView: RecyclerView) : ListAdapter<DashboardInfo, DashboardAdapter.ViewHolder>(DashboardInfoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DashboardInfoDiffCallback : DiffUtil.ItemCallback<DashboardInfo>() {
        override fun areItemsTheSame(oldItem: DashboardInfo, newItem: DashboardInfo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DashboardInfo, newItem: DashboardInfo): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

//        companion object {
//            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
//                    .inflate(R.layout.list_item_dashboard, parent, false))
//        }

        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_dashboard, parent, false))

        fun bind(dashboardInfo: DashboardInfo?) {

        }
    }
}