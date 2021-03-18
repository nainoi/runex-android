package com.think.runex.feature.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.common.*
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.feature.auth.AuthViewModelFactory
import com.think.runex.feature.auth.login.LoginScreen
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.user.UserViewModel
import com.think.runex.feature.user.UserViewModelFactory
import com.think.runex.base.BaseScreen
import com.think.runex.feature.user.profile.ProfileEditorScreen
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_setting.*
import kotlinx.android.synthetic.main.toolbar.*

class SettingScreen : BaseScreen() {

    private lateinit var viewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getUserViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        //Get user info initial
        if (viewModel.userInfo.value == null) {
            viewModel.getUSerInfo()
        }
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.setting, R.drawable.ic_navigation_back)
        app_version_label?.text = ("${getString(R.string.app_version)} ${BuildConfig.VERSION_NAME}")
    }

    private fun subscribeUi() {
        edit_button?.setOnClickListener {
            addFragment(ProfileEditorScreen())
        }

        applications_menu_layout?.setOnClickListener {
            addFragment(ConnectApplicationsScreen())
        }

        about_us_menu_layout?.setOnClickListener {

        }

        contract_menu_layout?.setOnClickListener {

        }

        logout_button?.setOnClickListener {
            showConfirmToLogoutDialog()
        }

        viewModel.setOnHandleError(::errorHandler)

        observe(viewModel.userInfo) { userInfo ->
            if (view == null || isAdded.not()) return@observe
            updateUserInfo(userInfo)
        }
    }

    private fun updateUserInfo(userInfo: UserInfo?) {
        profile_image?.loadProfileImage(userInfo?.avatar)
        username_label?.text = userInfo?.fullName ?: ""
        email_label?.text = userInfo?.email ?: ""
    }

    private fun showConfirmToLogoutDialog() {
        showAlertDialog(R.string.confirm, R.string.confirm_to_logout, R.string.logout, R.string.cancel, onPositiveClick = {
            performLogout()
        })
    }

    private fun performLogout() = launch {
        showProgressDialog(R.string.logout)
        val authViewModel = getViewModel<AuthViewModel>(AuthViewModelFactory(requireContext()))
        val isSuccess = authViewModel.logout()
        if (isSuccess) {
            //Remove and clear user info live data before replace login screen
            removeObservers(viewModel.userInfo)
            viewModel.userInfo.postValue(null)
            replaceFragment(LoginScreen(), addToBackStack = false, clearFragment = true)
        }
    }

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
    }

    override fun onDestroy() {
        removeObservers(viewModel.userInfo)
        super.onDestroy()
    }
}