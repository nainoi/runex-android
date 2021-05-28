package com.think.runex.feature.workout.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.feature.workout.data.WorkoutInfo
import com.think.runex.util.extension.requireContext
import com.think.runex.util.extension.loadWorkoutIcon
import kotlinx.android.synthetic.main.list_item_workout_history_day.view.*

class WorkoutHistoryDayAdapter(private var onItemClickListener: ((WorkoutHistoryDay: WorkoutInfo) -> Unit)? = null) :
    ListAdapter<WorkoutInfo, WorkoutHistoryDayAdapter.ViewHolder>(WorkoutHistoryDayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    class WorkoutHistoryDayDiffCallback : DiffUtil.ItemCallback<WorkoutInfo>() {
        override fun areItemsTheSame(oldItem: WorkoutInfo, newItem: WorkoutInfo): Boolean {
            return oldItem.workoutDate == newItem.workoutDate
                    && oldItem.timeDisplay == newItem.timeDisplay
        }

        override fun areContentsTheSame(oldItem: WorkoutInfo, newItem: WorkoutInfo): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        companion object {
//            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
//                    .inflate(R.layout.list_item_workout_history_day, parent, false))
//        }

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_workout_history_day, parent, false)
        )

        fun bind(
            data: WorkoutInfo?,
            onItemClickListener: ((WorkoutHistoryDay: WorkoutInfo) -> Unit)? = null
        ) {
            itemView.workout_time_label?.text = data?.getWorkoutDateTimeForHistoryDay() ?: ""
            itemView.distance_label?.text = data?.getDistances(requireContext()) ?: ""
            itemView.workout_type_icon?.loadWorkoutIcon(data?.getPartnerIcon())
            itemView.list_item_workout_day?.setOnClickListener {
                data?.also { onItemClickListener?.invoke(it) }
            }
        }
    }
}