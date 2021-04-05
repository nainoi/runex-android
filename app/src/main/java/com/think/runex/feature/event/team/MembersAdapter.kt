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
import com.think.runex.feature.event.data.RegisterData
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.util.extension.getString
import com.think.runex.util.extension.loadProfileImage
import kotlinx.android.synthetic.main.list_item_member_in_team.view.*
import kotlinx.coroutines.launch

class MembersAdapter(context: Context, private val owner: LifecycleOwner) : ListAdapter<RegisterData, MembersAdapter.ViewHolder>(EventRegisteredDataDiffCallback()) {

    private var repository: TeamRepository? = null

    init {
        repository = TeamRepository(ApiService().provideService(context, EventApi::class.java))
    }

    var onItemClickListener: ((registerData: RegisterData) -> Unit)? = null

    @JvmName("setOnItemClickListenerJava")
    fun setOnItemClickListener(onItemClick: (registerData: RegisterData) -> Unit) {
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

    class EventRegisteredDataDiffCallback : DiffUtil.ItemCallback<RegisterData>() {
        override fun areItemsTheSame(oldItem: RegisterData, newItem: RegisterData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RegisterData, newItem: RegisterData): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        companion object {
//            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
//                    .inflate(R.layout.list_item_team_member, parent, false))
//        }

        constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_member_in_team, parent, false))

        fun bind(data: RegisterData?, onItemClick: ((registerData: RegisterData) -> Unit)? = null) {

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
            itemView.full_name_label?.text = ("${userInfo.getFullName()}${if (isTeamLeader) " (${getString(R.string.team_leader)})" else ""}")

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