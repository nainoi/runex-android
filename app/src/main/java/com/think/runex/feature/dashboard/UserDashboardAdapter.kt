package com.think.runex.feature.dashboard

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.setVisible
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.*
import com.think.runex.component.recyclerview.LineSeparatorItemDecoration
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.dashboard.data.UserDashboard
import kotlinx.android.synthetic.main.list_item_dashboard_user.view.*
import kotlinx.coroutines.launch

class UserDashboardAdapter(private val recyclerView: RecyclerView,
                           private val owner: LifecycleOwner) : ListAdapter<UserDashboard, UserDashboardAdapter.ViewHolder>(UserActivityDiffCallback()) {

    private var repository: DashboardRepository? = null

    init {
        repository = DashboardRepository(ApiService().provideService(recyclerView.context, DashboardApi::class.java))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), repository, owner)
    }

    class UserActivityDiffCallback : DiffUtil.ItemCallback<UserDashboard>() {
        override fun areItemsTheSame(oldItem: UserDashboard, newItem: UserDashboard): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserDashboard, newItem: UserDashboard): Boolean {
            return oldItem == newItem
        }
    }

    fun clear() {
        repository = null
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

//        companion object {
//            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
//                    .inflate(R.layout.list_item_dashboard, parent, false))
//        }

        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_dashboard_user, parent, false))

        fun bind(data: UserDashboard?, repo: DashboardRepository?, owner: LifecycleOwner) {

            //Set views to skeleton on loading
            showSkeleton()

            //Setup views
            itemView.activity_times_label?.text = (data?.activityInfo?.size ?: 0).displayFormat()
            itemView.total_distance_label?.text = data?.getTotalDistanceDisplay(getString(R.string.km))

            //Setup user activity info list
            val itemDecoration = LineSeparatorItemDecoration(
                    lineSeparator = getDrawable(R.drawable.line_separator_list_item),
                    addLineOnBottomOrRightOfLastItem = true).apply {
                marginLeftOrTop = getDimension(R.dimen.space_44dp).toInt()
                marginRightOrBottom = getDimension(R.dimen.space_16dp).toInt()
            }
            itemView.user_activity_list?.addItemDecoration(itemDecoration)
            itemView.user_activity_list?.layoutManager = LinearLayoutManager(requireContext())
            itemView.user_activity_list?.adapter = UserActivityAdapter().apply {
                submitList(data?.activityInfo?.toMutableList())
            }

            //Subscribe Ui
            itemView.user_dashboard_layout?.setOnClickListener {
                when (itemView.user_activity_list?.adapter?.itemCount ?: 0 > 0) {
                    true -> setVisibleWorkoutDaysWithAnimation(itemView.user_activity_list?.isVisible?.not())
                    false -> itemView.user_activity_list?.gone()
                }
            }

            //Get user name from api
            owner.lifecycleScope.launch {

                val result = repo?.getUserInfoById(data?.userId ?: "")

                showContents()

                //Update user name data
                result?.data?.also { userInfo ->
                    if (userInfo.firstName?.isNotBlank() == true && userInfo.lastName?.isNotBlank() == true) {
                        itemView.full_name_label?.text = ("${userInfo.firstName} ${userInfo.lastName}")
                    } else {
                        itemView.full_name_label?.text = userInfo.firstName ?: ""
                    }
                }
            }
        }

        private fun setVisibleWorkoutDaysWithAnimation(isVisible: Boolean?) {
            if (isVisible == null) return
            TransitionManager.beginDelayedTransition(recyclerView, AutoTransition())
            itemView.user_activity_list?.setVisible(isVisible)
        }

        private fun showSkeleton() {
            itemView.user_dashboard_layout?.isClickable = false
            itemView.activity_times_label?.inVisible()
            itemView.activity_times_label_placeholder?.inVisible()
            itemView.username_layout?.setBackgroundResource(R.drawable.shape_rectangle_thirdly_corner_radius_8dp)
            itemView.full_name_label?.inVisible()
            itemView.running_icon.inVisible()
            itemView.total_distance_label?.inVisible()
        }

        private fun showContents() {
            itemView.user_dashboard_layout?.isClickable = true
            itemView.activity_times_label?.visible()
            itemView.activity_times_label_placeholder?.visible()
            itemView.username_layout?.background = null
            itemView.full_name_label?.visible()
            itemView.running_icon.visible()
            itemView.total_distance_label?.visible()
        }
    }
}