package com.think.runex.feature.workout.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.common.requireContext
import com.think.runex.feature.workout.data.WorkoutHistoryDay
import kotlinx.android.synthetic.main.list_item_workout_history_day.view.*

class WorkoutHistoryDayAdapter(private var onItemClickListener: ((WorkoutHistoryDay: WorkoutHistoryDay) -> Unit)? = null) : ListAdapter<WorkoutHistoryDay, WorkoutHistoryDayAdapter.ViewHolder>(WorkoutHistoryDayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClickListener)
    }

    class WorkoutHistoryDayDiffCallback : DiffUtil.ItemCallback<WorkoutHistoryDay>() {
        override fun areItemsTheSame(oldItem: WorkoutHistoryDay, newItem: WorkoutHistoryDay): Boolean {
            return oldItem.workoutDate == newItem.workoutDate
                    && oldItem.workoutTime == newItem.workoutTime
        }

        override fun areContentsTheSame(oldItem: WorkoutHistoryDay, newItem: WorkoutHistoryDay): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        companion object {
//            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
//                    .inflate(R.layout.list_item_workout_history_day, parent, false))
//        }

        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_workout_history_day, parent, false))

        fun bind(data: WorkoutHistoryDay?, onItemClickListener: ((WorkoutHistoryDay: WorkoutHistoryDay) -> Unit)? = null) {
            itemView.workout_time_label?.text = data?.getWorkoutDateTime() ?: ""
            itemView.distance_label?.text = data?.getDistances(requireContext()) ?: ""
            itemView.list_item_workout_day?.setOnClickListener {
                data?.also { onItemClickListener?.invoke(it) }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(workoutHistoryDay: WorkoutHistoryDay)
    }
}