package com.think.runex.feature.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.common.getString
import com.think.runex.common.requireContext
import com.think.runex.feature.dashboard.data.UserActivity
import kotlinx.android.synthetic.main.list_item_dashboard_user_activity.view.*

class UserActivityAdapter : ListAdapter<UserActivity, UserActivityAdapter.ViewHolder>(UserActivityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder.create(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserActivityDiffCallback : DiffUtil.ItemCallback<UserActivity>() {
        override fun areItemsTheSame(oldItem: UserActivity, newItem: UserActivity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserActivity, newItem: UserActivity): Boolean {
            return oldItem == newItem
        }
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_dashboard_user_activity, parent, false))
        }

        fun bind(data: UserActivity? /*onItemClickListener: ((userActivity: UserActivity) -> Unit)? = null*/) {

            itemView.distance_label?.text = data?.getDistanceDisplay(getString(R.string.km)) ?: ""
            itemView.date_time_label?.text = data?.getActivityDateDisplay() ?: ""

            itemView.status_icon?.background = ActivityStatus.getStatusBackground(requireContext(), data?.status)
            itemView.status_label?.text = ActivityStatus.getStatusText(requireContext(), data?.status
                    ?: "")
        }
    }
}