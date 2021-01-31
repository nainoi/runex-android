package com.think.runex.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.*
import com.think.runex.feature.user.UserInfo
import com.think.runex.feature.user.UserViewModel
import com.think.runex.feature.user.UserViewModelFactory
import com.think.runex.ui.SettingScreen
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_profile.*

class ProfileScreen : BaseScreen() {

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(UserViewModelFactory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
        performGetUserInfo()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden.not()) {
            setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
            if (profile_card_layout?.isVisible == false) {
                performGetUserInfo()
            }
        }
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
    }

    private fun subscribeUi() {
        setting_button?.setOnClickListener {
            addFragment(SettingScreen())
        }
    }

    private fun performGetUserInfo() = launch {
        progress_bar?.visible()
        profile_card_layout?.gone()
        val userInfo = viewModel.getUSerInfo()
        progress_bar?.gone()
        if (userInfo != null) {
            profile_card_layout?.visible()
            updateUi(userInfo)
        }
    }

    private fun updateUi(userInfo: UserInfo) {
        profile_image?.loadProfileImage(userInfo.avatar)
        username_label?.text = userInfo.fullName ?: ""
        email_label?.text = userInfo.email ?: ""
        total_distances_label?.text = userInfo.getTotalDistance(getString(R.string.km))
    }

    override fun errorHandler(statusCode: Int, message: String) {
        super.errorHandler(statusCode, message)
        progress_bar?.gone()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}