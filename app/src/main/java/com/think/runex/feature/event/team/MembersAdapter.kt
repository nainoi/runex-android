package com.think.runex.feature.event.team

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.think.runex.R
import com.think.runex.datasource.api.ApiService
import com.think.runex.feature.event.EventApi
import com.think.runex.feature.event.data.EventRegisteredData
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.util.extension.getString
import com.think.runex.util.extension.loadProfileImage
import kotlinx.android.synthetic.main.list_item_team_member.view.*
import kotlinx.coroutines.launch

class MembersAdapter(context: Context, private val owner: LifecycleOwner) : ListAdapter<EventRegisteredData, MembersAdapter.ViewHolder>(EventRegisteredDataDiffCallback()) {

    private var repository: TeamRepository? = null

    init {
        repository = TeamRepository(ApiService().provideService(context, EventApi::class.java))
    }

    var onItemClickListener: ((registerData: EventRegisteredData) -> Unit)? = null

    @JvmName("setOnItemClickListenerJava")
    fun setOnItemClickListener(onItemClick: (registerData: EventRegisteredData) -> Unit) {
        onItemClickListener = onItemClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun clear() {
        submitList(null)
        repository = null
    }

    class EventRegisteredDataDiffCallback : DiffUtil.ItemCallback<EventRegisteredData>() {
        override fun areItemsTheSame(oldItem: EventRegisteredData, newItem: EventRegisteredData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EventRegisteredData, newItem: EventRegisteredData): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        companion object {
//            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
//                    .inflate(R.layout.list_item_team_member, parent, false))
//        }

        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_team_member, parent, false))

        fun bind(data: EventRegisteredData?, onItemClick: ((registerData: EventRegisteredData) -> Unit)? = null) {

            //Subscribe Ui
            itemView.list_item_team_member?.setOnClickListener {
                data?.also { onItemClick?.invoke(it) }
            }

            //Get user in from api
            owner.lifecycleScope.launch {

                showSkeleton()

                val result = repository?.getUserInfoById(data?.userId ?: "")

                showContents()

                //Update user  data
                result?.data?.also { userInfo ->
                    updateUi(userInfo, data?.isTeamLead == true)
                }
            }
        }

        private fun updateUi(userInfo: UserInfo, isTeamLeader: Boolean) {

            //Set up components
            itemView.profile_image?.loadProfileImage(userInfo.avatar)

            val fullName = when (userInfo.firstName?.isNotBlank() == true && userInfo.lastName?.isNotBlank() == true) {
                true -> ("${userInfo.firstName} ${userInfo.lastName}")
                false -> userInfo.firstName ?: ""
            }
            itemView.full_name_label?.text = ("$fullName${if (isTeamLeader) " (${getString(R.string.team_leader)})" else ""}")

        }

        private fun showSkeleton() {
            itemView.list_item_team_member?.isClickable = false
            itemView.full_name_label?.setBackgroundResource(R.drawable.shape_rectangle_thirdly_corner_radius_8dp)
        }

        private fun showContents() {
            itemView.list_item_team_member?.isClickable = true
            itemView.full_name_label?.background = null
        }
    }
}