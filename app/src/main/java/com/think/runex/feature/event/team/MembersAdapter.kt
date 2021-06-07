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
import com.think.runex.feature.event.data.RegisteredData
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.user.data.UserInfoRequestBody
import com.think.runex.util.extension.getString
import com.think.runex.util.extension.loadProfileImage
import kotlinx.android.synthetic.main.list_item_member_in_team.view.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class MembersAdapter(context: Context, private val owner: LifecycleOwner) :
    ListAdapter<RegisteredData, MembersAdapter.ViewHolder>(EventRegisteredDataDiffCallback()) {

    private var repository: TeamRepository? = null

    init {
        repository = TeamRepository(ApiService().provideService(context, TeamApi::class.java))
    }

    private var onItemClickListener: ((registeredData: RegisteredData) -> Unit)? = null

    @JvmName("setOnItemClickListenerJava")
    fun setOnItemClickListener(onItemClick: (registeredData: RegisteredData) -> Unit) {
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

    class EventRegisteredDataDiffCallback : DiffUtil.ItemCallback<RegisteredData>() {
        override fun areItemsTheSame(oldItem: RegisteredData, newItem: RegisteredData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RegisteredData, newItem: RegisteredData): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        companion object {
//            fun create(parent: ViewGroup) = ViewHolder(LayoutInflater.from(parent.context)
//                    .inflate(R.layout.list_item_team_member, parent, false))
//        }

        constructor(parent: ViewGroup) : this(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_member_in_team, parent, false)
        )

        fun bind(data: RegisteredData?, onItemClick: ((registeredData: RegisteredData) -> Unit)? = null) {

            if (data == null) return

            itemView.full_name_label?.text = data.ticketOptions?.firstOrNull()?.userOption?.getFullName() ?: ""

            //Get profile image from api
            owner.lifecycleScope.launch(IO) {
                val result = repository?.getUserInfoById(UserInfoRequestBody(data.userId ?: ""))
                result?.data?.also { userInfo ->
                    owner.lifecycleScope.launch(Main) {
                        itemView.profile_image?.loadProfileImage(userInfo.avatar)
                    }
                }
            }

            //Subscribe Ui
            //itemView.list_item_team_member?.setOnClickListener {
            //    onItemClick?.invoke(data)
            //}
            //TODO("Force to disable click for now")
            itemView.list_item_team_member?.isClickable = false
        }
    }
}