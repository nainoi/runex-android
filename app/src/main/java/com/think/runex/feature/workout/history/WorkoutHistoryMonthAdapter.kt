package com.think.runex.feature.workout.history

import android.graphics.Canvas
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.*
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.setVisible
import com.think.runex.R
import com.think.runex.util.extension.*
import com.think.runex.feature.workout.data.WorkoutHistoryMonth
import com.think.runex.component.recyclerview.LineSeparatorItemDecoration
import com.think.runex.component.recyclerview.swipemenu.SwipeMenu
import com.think.runex.component.recyclerview.swipemenu.SwipeMenuListItemCallback
import com.think.runex.feature.workout.data.WorkoutInfo
import kotlinx.android.synthetic.main.list_item_workout_history_month.view.*

class WorkoutHistoryMonthAdapter(

    private val recyclerView: RecyclerView,

    var onItemClickListener: ((
        WorkoutHistoryDay: WorkoutInfo
    ) -> Unit)? = null,

    var onDeleteWorkoutListener: ((
        monthPosition: Int,
        WorkoutHistoryDay: WorkoutInfo
    ) -> Unit)? = null

) : ListAdapter<WorkoutHistoryMonth, WorkoutHistoryMonthAdapter.ViewHolder>(WorkoutHistoryMonthDiffCallback()) {

    companion object {
        const val ID_MENU_DELETE = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    @JvmName("setOnItemClickListenerJava")
    fun setOnItemClickListener(onItemClickListener: ((workoutInfo: WorkoutInfo) -> Unit)?) {
        this.onItemClickListener = onItemClickListener
    }

    @JvmName("setOnDeleteWorkoutListenerJava")
    fun setOnDeleteWorkoutListener(onDeleteWorkoutListener: ((monthPosition: Int, workoutInfo: WorkoutInfo) -> Unit)? = null) {
        this.onDeleteWorkoutListener = onDeleteWorkoutListener
    }

    class WorkoutHistoryMonthDiffCallback : DiffUtil.ItemCallback<WorkoutHistoryMonth>() {
        override fun areItemsTheSame(oldItem: WorkoutHistoryMonth, newItem: WorkoutHistoryMonth): Boolean {
            return oldItem.year == newItem.year
                    && oldItem.month == newItem.month
                    && oldItem.totalDistances == newItem.totalDistances
                    && oldItem.workouts?.size == newItem.workouts?.size
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

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_workout_history_month, parent, false)
        )

        fun bind(data: WorkoutHistoryMonth?) {
            //Set workout summary month details.
            (data?.workouts?.size ?: 0).also { workoutTimes ->
                itemView.workout_times_in_month_label?.text = workoutTimes.toString()
                itemView.workout_times_in_month_placeholder?.text = getString(
                    when (workoutTimes > 1) {
                        true -> R.string.number_of_times
                        false -> R.string.number_of_time
                    }
                )
            }
            itemView.month_year_label?.text = data?.getMontAndYear() ?: ""
            itemView.distance_label?.text = data?.totalDistances?.displayFormat(awaysShowDecimal = true)
                ?: ""
            itemView.duration_label?.text = data?.totalDuration ?: ""
            itemView.calorie_label?.text = data?.calories?.displayFormat() ?: ""

            //Setup workout day list
            val itemDecoration = LineSeparatorItemDecoration(
                lineSeparator = getDrawable(R.drawable.line_separator_list_item),
                addLineOnBottomOrRightOfLastItem = true
            ).apply {
                marginLeftOrTop = getDimension(R.dimen.space_44dp).toInt()
                marginRightOrBottom = getDimension(R.dimen.space_16dp).toInt()
            }

            itemView.summary_month_layout?.setOnClickListener {
                when (itemView.workout_day_list?.adapter?.itemCount ?: 0 > 0) {
                    true -> setVisibleWorkoutDaysWithAnimation(itemView.workout_day_list?.isVisible?.not())
                    false -> itemView.workout_day_list?.gone()
                }
            }

            itemView.workout_day_list?.addItemDecoration(itemDecoration)
            itemView.workout_day_list?.layoutManager = LinearLayoutManager(requireContext())
            itemView.workout_day_list?.adapter = WorkoutHistoryDayAdapter(onItemClickListener).apply {
                submitList(data?.workouts?.toMutableList())
            }

            //Set swipe menu to recycler view.
            val swipeMenu = SwipeMenuListItemCallback(createSwipeToDeleteMenu())
            swipeMenu.setMenuWidth(getDimension(R.dimen.space_84dp).toInt())
            swipeMenu.setOnSwipeMenuSelected { dayPosition, menu ->
                if(dayPosition < data?.workouts?.size ?: 0) {
                    data?.workouts?.get(dayPosition)?.also { workoutInfo ->
                        if (menu.id == ID_MENU_DELETE) {
                            requireContext().showAlertDialog(title = getString(R.string.warning),
                                message = getString(R.string.delete_workout_confirm_msg),
                                negativeText = getString(R.string.cancel),
                                positiveText = getString(R.string.delete),
                                onPositiveClick = {
                                    onDeleteWorkoutListener?.invoke(adapterPosition, workoutInfo)
                                })
                        }
                    }
                }
            }
            ItemTouchHelper(swipeMenu).attachToRecyclerView(itemView.workout_day_list)
            itemView.workout_day_list?.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    swipeMenu.onDraw(c)
                }
            })
        }

        private fun setVisibleWorkoutDaysWithAnimation(isVisible: Boolean?) {
            if (isVisible == null) return
            TransitionManager.beginDelayedTransition(recyclerView, AutoTransition())
            itemView.workout_day_list?.setVisible(isVisible)
        }

        private fun createSwipeToDeleteMenu() = listOf(
            SwipeMenu(
                id = ID_MENU_DELETE,
                name = getString(R.string.delete),
                backgroundColor = getColor(R.color.error),
                gravity = Gravity.END
            )
        )
    }
}