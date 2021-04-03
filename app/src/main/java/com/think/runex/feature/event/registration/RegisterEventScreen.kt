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
import com.think.runex.config.KEY_EVENT_CODE
import com.think.runex.util.extension.*
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.screen_register_event.*
import kotlinx.android.synthetic.main.toolbar.*

class RegisterEventScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(eventCode: String?) = RegisterEventScreen().apply {
            arguments = Bundle().apply {
                putString(KEY_EVENT_CODE, eventCode)
            }
        }
    }

    private lateinit var viewModel: RegisterEventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getViewModel(RegisterEventViewModel.Factory(requireContext()))

        //Get user info for fill out
        getUserViewModel().getUserInfoIfNotHave()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_register_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        progress_bar?.visible()
        viewModel.getEventDetail(arguments?.getString(KEY_EVENT_CODE) ?: "")
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.register_event, R.drawable.ic_navigation_back)
    }

    private fun subscribeUi() {

        viewModel.setOnHandleError(::errorHandler)

        observe(viewModel.eventDetail) { _ ->
            if (view == null || isAdded.not()) return@observe
            progress_bar?.gone()

            //If not have any child fragment
            if (childFragmentCount() == 0) {
                viewModel.onGetEventDetailCompleted()
            }
        }

        observe(viewModel.updateScreen) { screenName ->
            if (view == null || isAdded.not() && screenName?.isNotBlank() == true) return@observe
            when (screenName) {
                ChooseTicketFragment::class.java.simpleName -> {
                    replaceChildFragment(R.id.register_fragment_container, ChooseTicketFragment(), addToBackStack = true, clearChildFragment = true)
                }
                FillOutUserInfoFragment::class.java.simpleName -> {
                    addChildFragment(R.id.register_fragment_container, FillOutUserInfoFragment())
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

    override fun errorHandler(statusCode: Int, message: String, tag: String?) {
        super.errorHandler(statusCode, message, tag)
        progress_bar?.gone()
    }

    override fun onDestroy() {
        removeObservers(viewModel.eventDetail)
        removeObservers(viewModel.updateScreen)
        super.onDestroy()
    }
}