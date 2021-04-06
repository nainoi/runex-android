package com.think.runex.feature.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.showDialog
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.util.extension.*
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.feature.auth.login.LoginScreen
import com.think.runex.feature.user.data.UserInfo
import com.think.runex.feature.user.UserViewModel
import com.think.runex.base.BaseScreen
import com.think.runex.feature.user.profile.ProfileEditorScreen
import com.think.runex.util.Localization
import com.think.runex.util.NightMode
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.screen_setting.*
import kotlinx.android.synthetic.main.toolbar.*

class SettingScreen : BaseScreen(), SelectLanguageDialog.OnLanguageSelectedListener {

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
        setupToolbar(toolbar_layout, R.string.setting, R.drawable.ic_navigation_back)
        app_version_label?.text = ("${getString(R.string.app_version)} ${BuildConfig.VERSION_NAME} ${if (BuildConfig.DEBUG) "(${getString(R.string.environment_dev)})" else ""}")

        if (BuildConfig.DEBUG) {
            current_language_label?.text = ("(${Localization.setCurrentLanguageDisplay(requireContext())})")
            current_dark_mode_status_label?.text = ("(${NightMode.getDarkModeDisplay(requireContext())})")
            dark_mode_switch?.isChecked = NightMode.isNightMode(requireContext())
        } else {
            language_menu_layout?.gone()
            dark_mode_menu_layout?.gone()
        }
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

        language_menu_layout?.setOnClickListener {
            showDialog(SelectLanguageDialog())
        }

        dark_mode_menu_layout?.setOnClickListener {
            dark_mode_switch?.apply {
                isChecked = isChecked.not()
            }
        }

        dark_mode_switch?.setOnCheckedChangeListener { _, isChecked ->
            if (requireActivity() is AppCompatActivity) {
                val nightMode = if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                NightMode.setNightMode(requireActivity() as AppCompatActivity, nightMode)
            }
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

    override fun onLanguageSelected(key: String) {
        if (key.equals(Localization.getBaseLanguage(requireContext()), ignoreCase = true).not()) {
            Localization.setCurrentLanguage(requireActivity(), key)
        }
    }

    private fun updateUserInfo(userInfo: UserInfo?) {
        profile_image?.loadProfileImage(userInfo?.avatar)
        username_label?.text = userInfo?.fullName ?: ""
        email_label?.text = userInfo?.email ?: ""
    }

    private fun showConfirmToLogoutDialog() {
        showAlertDialog(R.string.logout, R.string.confirm_to_logout, R.string.logout, R.string.cancel, onPositiveClick = {
            performLogout()
        })
    }

    private fun performLogout() = launch {
        showProgressDialog(R.string.logout)
        val authViewModel = getViewModel<AuthViewModel>(AuthViewModel.Factory(requireContext()))
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