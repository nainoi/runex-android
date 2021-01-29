package com.think.runex.ui.workout.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.common.requireContext
import com.think.runex.feature.workout.model.WorkoutHistoryDay
import kotlinx.android.synthetic.main.list_item_workout_history_day.view.*

class WorkoutHistoryDayAdapter : ListAdapter<WorkoutHistoryDay, WorkoutHistoryDayAdapter.ViewHolder>(WorkoutHistoryDayDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
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
        companion object {
            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_workout_history_day, parent, false))
        }

        fun bind(data: WorkoutHistoryDay?) {
            itemView.workout_time_label?.text = data?.getWorkoutDateTime() ?: ""
            itemView.distance_label?.text = data?.getDistances(requireContext()) ?: ""
        }
    }
}