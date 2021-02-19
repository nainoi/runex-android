package com.think.runex.ui.workout.history

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.setVisible
import com.think.runex.R
import com.think.runex.common.*
import com.think.runex.feature.workout.model.WorkoutHistoryDay
import com.think.runex.feature.workout.model.WorkoutHistoryMonth
import com.think.runex.ui.component.recyclerview.LineSeparatorItemDecoration
import kotlinx.android.synthetic.main.list_item_workout_history_month.view.*

class WorkoutHistoryMonthAdapter(private val recyclerView: RecyclerView,
                                 var onItemClickListener: ((WorkoutHistoryDay: WorkoutHistoryDay) -> Unit)? = null) : ListAdapter<WorkoutHistoryMonth, WorkoutHistoryMonthAdapter.ViewHolder>(WorkoutHistoryMonthDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutHistoryMonthAdapter.ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    @JvmName("setOnItemClickListenerJava")
    fun setOnItemClickListener(onItemClickListener: ((WorkoutHistoryDay: WorkoutHistoryDay) -> Unit)? = null) {
        this.onItemClickListener = onItemClickListener
    }

    class WorkoutHistoryMonthDiffCallback : DiffUtil.ItemCallback<WorkoutHistoryMonth>() {
        override fun areItemsTheSame(oldItem: WorkoutHistoryMonth, newItem: WorkoutHistoryMonth): Boolean {
            return oldItem.year == newItem.year
                    && oldItem.month == newItem.month
                    && oldItem.totalDistances == newItem.totalDistances
        }

        override fun areContentsTheSame(oldItem: WorkoutHistoryMonth, newItem: WorkoutHistoryMonth): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

//        companion object {
//            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
//                    .inflate(R.layout.list_item_workout_history_month, parent, false))
//        }

        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_workout_history_month, parent, false))

        fun bind(data: WorkoutHistoryMonth?) {
            //Set workout summary month details.
            (data?.workouts?.size ?: 0).also { workoutTimes ->
                itemView.workout_times_in_month_label?.text = workoutTimes.toString()
                itemView.workout_times_in_month_placeholder?.text = getString(when (workoutTimes > 1) {
                    true -> R.string.number_of_times
                    false -> R.string.number_of_time
                })
            }
            itemView.month_year_label?.text = data?.getMontAndYear() ?: ""
            itemView.distance_label?.text = data?.totalDistances?.displayFormat(awaysShowDecimal = true)
                    ?: ""
            itemView.duration_label?.text = data?.totalDuration ?: ""
            itemView.calorie_label?.text = data?.calories?.displayFormat() ?: ""

            //Setup workout day list
            val itemDecoration = LineSeparatorItemDecoration(
                    lineSeparator = getDrawable(R.drawable.line_separator_list_item),
                    addLineOnBottomOrRightOfLastItem = true).apply {
                marginLeftOrTop = getDimension(R.dimen.space_44dp).toInt()
                marginRightOrBottom = getDimension(R.dimen.space_16dp).toInt()
            }
            itemView.workout_day_list?.addItemDecoration(itemDecoration)
            itemView.workout_day_list?.layoutManager = LinearLayoutManager(requireContext())
            itemView.workout_day_list?.adapter = WorkoutHistoryDayAdapter(onItemClickListener).apply {
                submitList(data?.workouts?.toMutableList())
            }

            itemView.summary_month_layout?.setOnClickListener {
                when (itemView.workout_day_list?.adapter?.itemCount ?: 0 > 0) {
                    true -> setVisibleWorkoutDaysWithAnimation(itemView.workout_day_list?.isVisible?.not())
                    false -> itemView.workout_day_list?.gone()
                }
            }
        }

        private fun setVisibleWorkoutDaysWithAnimation(isVisible: Boolean?) {
            if (isVisible == null) return
            TransitionManager.beginDelayedTransition(recyclerView, AutoTransition())
            itemView.workout_day_list?.setVisible(isVisible)
        }
    }
}