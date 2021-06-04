package com.think.runex.feature.dashboard

import android.graphics.Canvas
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.setVisible
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.util.extension.*
import com.think.runex.component.recyclerview.LineSeparatorItemDecoration
import com.think.runex.component.recyclerview.swipemenu.SwipeMenu
import com.think.runex.component.recyclerview.swipemenu.SwipeMenuListItemCallback
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.dashboard.data.DeleteActivityBody
import com.think.runex.feature.dashboard.data.UserActivityDashboard
import com.think.runex.feature.user.data.UserInfoRequestBody
import com.think.runex.feature.workout.history.WorkoutHistoryMonthAdapter
import kotlinx.android.synthetic.main.list_item_dashboard_user.view.*
import kotlinx.coroutines.launch

class UserDashboardAdapter(
    private val recyclerView: RecyclerView,
    private val owner: LifecycleOwner,
    var deleteActivityListener: OnDeleteActivityListener? = null
) : RecyclerView.Adapter<UserDashboardAdapter.ViewHolder>() {

    private var repository: DashboardRepository? = null
    private var list: ArrayList<UserActivityDashboard>? = null

    init {
        repository = DashboardRepository(ApiService().provideService(recyclerView.context, DashboardApi::class.java))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list?.get(position))
    }

    override fun getItemCount(): Int = list?.size ?: 0

//    class UserActivityDiffCallback : DiffUtil.ItemCallback<UserActivityDashboard>() {
//        override fun areItemsTheSame(oldItem: UserActivityDashboard, newItem: UserActivityDashboard): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: UserActivityDashboard, newItem: UserActivityDashboard): Boolean {
//            return oldItem == newItem
//        }
//    }

    fun submitList(list: List<UserActivityDashboard>?) {
        this.list = ArrayList(list ?: emptyList())
        notifyDataSetChanged()
    }

    fun onDeleteActivity(userPosition: Int, userDashboard: UserActivityDashboard?) {
        if (userPosition >= list?.size ?: 0) return
        if (userDashboard == null || userDashboard.activityInfoList?.isNullOrEmpty() == true) {
            list?.removeAt(userPosition)
            notifyItemRemoved(userPosition)
        } else {
            list?.set(userPosition, userDashboard)
            notifyItemChanged(userPosition, Unit)
        }
    }

    fun clear() {
        list?.clear()
        list = null
        repository = null
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_dashboard_user, parent, false)
        )

        fun bind(data: UserActivityDashboard?) {

            if (data == null) return

            //Set views to skeleton on loading
            itemView.list_item_dashboard_user?.inVisible()

            //Setup views
            itemView.activity_times_label?.text = (data.activityInfoList?.size ?: 0).displayFormat()
            itemView.total_distance_label?.text = data.getTotalDistanceDisplay(getString(R.string.km))

            //Setup user activity info list
            val itemDecoration = LineSeparatorItemDecoration(
                lineSeparator = getDrawable(R.drawable.line_separator_list_item),
                addLineOnBottomOrRightOfLastItem = true
            ).apply {
                marginLeftOrTop = getDimension(R.dimen.space_44dp).toInt()
                marginRightOrBottom = getDimension(R.dimen.space_16dp).toInt()
            }
            itemView.user_activity_list?.addItemDecoration(itemDecoration)
            itemView.user_activity_list?.layoutManager = LinearLayoutManager(requireContext())
            itemView.user_activity_list?.adapter = UserActivityAdapter(data.activityInfoList ?: emptyList())

            //Set swipe menu to recycler view.
            val swipeMenu = SwipeMenuListItemCallback(createSwipeToDeleteMenu())
            swipeMenu.setMenuWidth(getDimension(R.dimen.space_84dp).toInt())
            swipeMenu.setOnSwipeMenuSelected { activityPosition, menu ->
                if (menu.id != WorkoutHistoryMonthAdapter.ID_MENU_DELETE) return@setOnSwipeMenuSelected
                try {
                    list?.get(adapterPosition)?.also { userDashboard ->
                        userDashboard.activityInfoList?.get(activityPosition)?.also { activityInfo ->
                            requireContext().showAlertDialog(title = getString(R.string.warning),
                                message = getString(R.string.delete_workout_confirm_msg),
                                negativeText = getString(R.string.cancel),
                                positiveText = getString(R.string.delete),
                                onPositiveClick = {
                                    deleteActivityListener?.onDeleteActivity(
                                        adapterPosition,
                                        activityPosition,
                                        userDashboard.id ?: "",
                                        DeleteActivityBody().apply {
                                            this.activityInfo = activityInfo
                                            this.eventCode = userDashboard.eventCode ?: ""
                                            this.orderId = userDashboard.orderId ?: ""
                                            this.registerId = userDashboard.registerId ?: ""
                                            this.parentRegisterId = userDashboard.parentRegisterId ?: ""
                                            this.ticketId = userDashboard.ticket?.id ?: ""
                                        }
                                    )
                                })
                        }
                    }

                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
            swipeMenu.setOnTouchReleased {
                itemView.user_activity_list?.adapter?.notifyDataSetChanged()
            }
            ItemTouchHelper(swipeMenu).attachToRecyclerView(itemView.user_activity_list)
            itemView.user_activity_list?.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                    swipeMenu.onDraw(c)
                }
            })


            //Subscribe Ui
            itemView.user_dashboard_layout?.setOnClickListener {
                when (itemView.user_activity_list?.adapter?.itemCount ?: 0 > 0) {
                    true -> setVisibleWorkoutDaysWithAnimation(itemView.user_activity_list?.isVisible?.not())
                    false -> itemView.user_activity_list?.gone()
                }
            }

            //Get user name from api
            owner.lifecycleScope.launch {

                val result = repository?.getUserInfoById(UserInfoRequestBody(data.userId ?: ""))

                itemView.list_item_dashboard_user?.visible()

                //Update user name data
                result?.data?.also { userInfo ->
                    itemView.full_name_label?.text = userInfo.getFullName()
                }
            }
        }

        private fun setVisibleWorkoutDaysWithAnimation(isVisible: Boolean?) {
            if (isVisible == null) return
            TransitionManager.beginDelayedTransition(recyclerView, AutoTransition())
            itemView.user_activity_list?.setVisible(isVisible)
        }

        private fun createSwipeToDeleteMenu() = listOf(
            SwipeMenu(
                id = WorkoutHistoryMonthAdapter.ID_MENU_DELETE,
                name = getString(R.string.delete),
                backgroundColor = getColor(R.color.error),
                gravity = Gravity.END
            )
        )
    }
}