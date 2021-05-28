package com.think.runex.feature.workout.history

import android.graphics.Canvas
import android.transition.AutoTransition
import android.transition.TransitionManager
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
import kotlinx.android.synthetic.main.list_item_workout_history_month.view.*

class WorkoutHistoryMonthAdapter(
    private val recyclerView: RecyclerView,
    var clickWorkoutListener: WorkoutHistoryDayAdapter.OnClickWorkoutListener? = null,
    var deleteWorkoutListener: WorkoutHistoryDayAdapter.OnDeleteWorkoutListener? = null
) : RecyclerView.Adapter<WorkoutHistoryMonthAdapter.ViewHolder>() {

    companion object {
        const val ID_MENU_DELETE = -1
    }

    private var list: ArrayList<WorkoutHistoryMonth>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list?.get(position))
    }

    override fun getItemCount(): Int = list?.size ?: 0

//    class WorkoutHistoryMonthDiffCallback : DiffUtil.ItemCallback<WorkoutHistoryMonth>() {
//        override fun areItemsTheSame(oldItem: WorkoutHistoryMonth, newItem: WorkoutHistoryMonth): Boolean {
//            return oldItem.year == newItem.year
//                    && oldItem.month == newItem.month
//                    && oldItem.totalDistances == newItem.totalDistances
//                    && oldItem.workouts?.size == newItem.workouts?.size
//        }
//
//        override fun areContentsTheSame(oldItem: WorkoutHistoryMonth, newItem: WorkoutHistoryMonth): Boolean {
//            return oldItem == newItem
//        }
//    }

    fun submitList(list: List<WorkoutHistoryMonth>?) {
        this.list = ArrayList(list ?: emptyList())
        notifyDataSetChanged()
    }

    fun addList(list: List<WorkoutHistoryMonth>?) {
        if (this.list == null) {
            this.list = ArrayList()
        }
        val start = this.list?.size ?: 0
        this.list?.addAll(list ?: emptyList())
        notifyItemRangeInserted(start, this.list?.size ?: 0)
    }

    fun onDeleteWorkout(monthPosition: Int, historyMonth: WorkoutHistoryMonth?) {
        if (monthPosition >= list?.size ?: 0) return
        if (historyMonth == null || historyMonth.workouts?.isNullOrEmpty() == true) {
            list?.removeAt(monthPosition)
            notifyItemRemoved(monthPosition)
        } else {
            list?.set(monthPosition, historyMonth)
//            val viewHolder = recyclerView.findViewHolderForAdapterPosition(monthPosition)
//            if (viewHolder is WorkoutHistoryDayAdapter.OnDeleteWorkoutListener) {
//                viewHolder.onDeleteWorkout(0, 0, 0, dayPosition, null)
//            }
//            if (viewHolder is WorkoutHistoryMonthAdapter.ViewHolder) {
//                viewHolder.updateSummaryDetail(monthHistory)
//            }
            notifyItemChanged(monthPosition, Unit)
        }
    }

    fun clear() {
        list?.clear()
        list = null
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
            updateSummaryDetail(data)

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
            itemView.workout_day_list?.adapter = WorkoutHistoryDayAdapter(
                data?.workouts ?: emptyList(),
                clickWorkoutListener
            )

            //Set swipe menu to recycler view.
            val swipeMenu = SwipeMenuListItemCallback(createSwipeToDeleteMenu())
            swipeMenu.setMenuWidth(getDimension(R.dimen.space_84dp).toInt())
            swipeMenu.setOnSwipeMenuSelected { dayPosition, menu ->
                if (menu.id != ID_MENU_DELETE) return@setOnSwipeMenuSelected
                try {
                    list?.get(adapterPosition)?.also { historyMonth ->
                        historyMonth.workouts?.get(dayPosition)?.also { workoutInfo ->
                            requireContext().showAlertDialog(title = getString(R.string.warning),
                                message = getString(R.string.delete_workout_confirm_msg),
                                negativeText = getString(R.string.cancel),
                                positiveText = getString(R.string.delete),
                                onPositiveClick = {
                                    deleteWorkoutListener?.onDeleteWorkout(
                                        historyMonth.month ?: 0,
                                        historyMonth.year ?: 0,
                                        adapterPosition,
                                        dayPosition,
                                        workoutInfo
                                    )
                                })
                        }
                    }

                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            swipeMenu.setOnTouchReleased {
                itemView.workout_day_list?.adapter?.notifyDataSetChanged()
            }
            ItemTouchHelper(swipeMenu).attachToRecyclerView(itemView.workout_day_list)
            itemView.workout_day_list?.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    swipeMenu.onDraw(c)
                }
            })
        }

        //Set workout summary month details.
        private fun updateSummaryDetail(data: WorkoutHistoryMonth?) {
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
            itemView.distance_label?.text = data?.totalDistances?.displayFormat(awaysShowDecimal = true) ?: ""
            itemView.duration_label?.text = data?.totalDuration ?: ""
            itemView.calorie_label?.text = data?.calories?.displayFormat() ?: ""
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

//        override fun onDeleteWorkout(
//            month: Int,
//            year: Int,
//            monthPosition: Int,
//            dayPosition: Int,
//            workoutInfo: WorkoutInfo?
//        ) {
//            itemView.workout_day_list?.adapter?.notifyItemRemoved(dayPosition)
//        }
    }
}