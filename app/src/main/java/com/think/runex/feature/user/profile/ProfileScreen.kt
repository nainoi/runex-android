package com.think.runex.feature.user.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.*
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.user.UserViewModel
import com.think.runex.base.BaseScreen
import com.think.runex.feature.setting.SettingScreen
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.screen_profile.*

class ProfileScreen : BaseScreen() {

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getUserViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        //Get user info initial
        if (viewModel.userInfo.value == null) {
            progress_bar?.visible()
            profile_layout?.gone()
            viewModel.getUSerInfo()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) {
            setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        }
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
    }

    private fun subscribeUi() {
        setting_button?.setOnClickListener {
            addFragment(SettingScreen())
        }

        viewModel.setOnHandleError(::errorHandler)

        observe(viewModel.userInfo) { userInfo ->
            if (view == null || isAdded.not()) return@observe
            updateUserInfo(userInfo)
        }
    }

    private fun updateUserInfo(userInfo: UserInfo?) {
        progress_bar?.gone()
        profile_layout?.visible()
        profile_image?.loadProfileImage(userInfo?.avatar)
        username_label?.text = userInfo?.fullName ?: ""
        email_label?.text = userInfo?.email ?: ""
        total_distances_label?.text = userInfo?.getTotalDistance(getString(R.string.km)) ?: ""
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        progress_bar?.gone()
    }

    override fun onDestroy() {
        removeObservers(viewModel.userInfo)
        super.onDestroy()
    }
}