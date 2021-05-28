package com.think.runex.feature.workout.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.feature.workout.data.WorkoutInfo
import com.think.runex.util.extension.requireContext
import com.think.runex.util.extension.loadWorkoutIcon
import kotlinx.android.synthetic.main.list_item_workout_history_day.view.*

class WorkoutHistoryDayAdapter(
    val list: List<WorkoutInfo>,
    var clickWorkoutListener: OnClickWorkoutListener? = null
) : RecyclerView.Adapter<WorkoutHistoryDayAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

//    class WorkoutHistoryDayDiffCallback : DiffUtil.ItemCallback<WorkoutInfo>() {
//        override fun areItemsTheSame(oldItem: WorkoutInfo, newItem: WorkoutInfo): Boolean {
//            return oldItem.workoutDate == newItem.workoutDate
//                    && oldItem.timeDisplay == newItem.timeDisplay
//        }
//
//        override fun areContentsTheSame(oldItem: WorkoutInfo, newItem: WorkoutInfo): Boolean {
//            return oldItem == newItem
//        }
//    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

//        companion object {
//            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
//                    .inflate(R.layout.list_item_workout_history_day, parent, false))
//        }

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_workout_history_day, parent, false)
        )

        fun bind(data: WorkoutInfo?) {
            itemView.workout_time_label?.text = data?.getWorkoutDateTime("dd/MM/yyyy\nHH:mm:ss") ?: ""
            itemView.distance_label?.text = data?.getDistances(requireContext()) ?: ""
            itemView.workout_type_icon?.loadWorkoutIcon(data?.getPartnerIcon())
            itemView.list_item_workout_day?.setOnClickListener {
                data?.also { clickWorkoutListener?.onClickWorkout(data) }
            }
        }
    }


    interface OnClickWorkoutListener {
        fun onClickWorkout(workoutInfo: WorkoutInfo)
    }

    interface OnDeleteWorkoutListener {
        fun onDeleteWorkout(
            month: Int,
            year: Int,
            monthPosition: Int,
            dayPosition: Int,
            workoutInfo: WorkoutInfo
        )
    }
}