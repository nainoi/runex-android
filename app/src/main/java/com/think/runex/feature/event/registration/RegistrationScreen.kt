package com.think.runex.feature.event.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.fragment.*
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.config.KEY_DATA
import com.think.runex.config.KEY_EVENT_CODE
import com.think.runex.feature.event.data.Registered
import com.think.runex.feature.event.data.RegisterStatus
import com.think.runex.util.extension.*
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.screen_event_registration.*
import kotlinx.android.synthetic.main.toolbar.*

class RegistrationScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(eventCode: String?) = RegistrationScreen().apply {
            arguments = Bundle().apply {
                putString(KEY_EVENT_CODE, eventCode)
            }
        }

        @JvmStatic
        fun newInstance(registered: Registered?) = RegistrationScreen().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_DATA, registered)
            }
        }
    }

    private lateinit var viewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(RegistrationViewModel.Factory(requireContext()))

        //Get user info for fill out
        getUserViewModel().getUserInfoIfNotHave()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_event_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupComponents()
        subscribeUi()

        progress_bar?.visible()
        //Get event detail from event code or register data in update register data.
        val eventCode: String = arguments?.getString(KEY_EVENT_CODE) ?: ""
        val registered: Registered? = arguments?.getParcelable(KEY_DATA)

        when (registered != null) {
            true -> {
                setToolbarTitle(getString(R.string.update_registration), toolbar_title)
                viewModel.updateRegisterData(registered)
            }
            false -> viewModel.getEventDetail(eventCode)
        }
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar_layout, R.string.register_event, R.drawable.ic_navigation_back)
    }

    private fun subscribeUi() {

        viewModel.setOnHandleError(::errorHandler)

        observe(viewModel.eventDetail) { _ ->
            if (view == null || isAdded.not()) return@observe
            progress_bar?.gone()

            if (childFragmentCount() == 0 && viewModel.registerStatus == RegisterStatus.REGISTER) {
                //Show select ticket screen
                viewModel.onGetEventDetailCompleted()
            } else if (viewModel.registerStatus == RegisterStatus.WAITING_CONFIRM && viewModel.getCurrentTicketOption()?.ticket != null) {
                //Show fill out info screen
                viewModel.updateScreen.postValue(FillOutUserInfoFragment::class.java.simpleName)
            }
        }

        observe(viewModel.updateScreen) { screenName ->
            if (view == null || isAdded.not() && screenName?.isNotBlank() == true) return@observe
            when (screenName) {
                ChooseTicketFragment::class.java.simpleName -> {
                    replaceChildFragment(R.id.register_fragment_container, ChooseTicketFragment(), addToBackStack = true, clearChildFragment = true)
                }
                FillOutUserInfoFragment::class.java.simpleName -> when (childFragmentCount() == 0) {
                    true -> replaceChildFragment(R.id.register_fragment_container, FillOutUserInfoFragment(), addToBackStack = true, clearChildFragment = true)
                    false -> addChildFragment(R.id.register_fragment_container, FillOutUserInfoFragment())
                }
                ConfirmRegistrationFragment::class.java.simpleName -> {
                    addChildFragment(R.id.register_fragment_container, ConfirmRegistrationFragment())
                }
            }
        }
    }


    fun onClickBackPressed() {
        when (childFragmentCount() > 1) {
            true -> popChildFragment()
            false -> popFragment()
        }
    }

    override fun errorHandler(code: Int, message: String, tag: String?) {
        super.errorHandler(code, message, tag)
        progress_bar?.gone()
    }

    override fun onDestroy() {
        removeObservers(viewModel.eventDetail)
        removeObservers(viewModel.updateScreen)
        super.onDestroy()
    }
}