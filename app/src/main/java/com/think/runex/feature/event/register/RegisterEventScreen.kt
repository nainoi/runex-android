package com.think.runex.feature.event.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.fragment.childFragmentCount
import com.jozzee.android.core.fragment.replaceChildFragment
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.common.*
import com.think.runex.config.KEY_CODE
import com.think.runex.feature.event.detail.EventDetailsViewModel
import com.think.runex.feature.event.detail.EventDetailsViewModelFactory
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_register_event.*
import kotlinx.android.synthetic.main.toolbar.*

class RegisterEventScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(code: String?) = RegisterEventScreen().apply {
            arguments = Bundle().apply {
                putString(KEY_CODE, code)
            }
        }
    }

    private lateinit var viewModel: RegisterEventViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(RegisterEventViewModelFactory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_register_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        viewModel.getEventDetail(arguments?.getString(KEY_CODE) ?: "")
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

        observe(viewModel.changeChildScreen) { screenName ->
            if (view == null || isAdded.not()) return@observe
            when (screenName) {
                ChooseTicketFragment::class.java.simpleName -> {
                    replaceChildFragment(R.id.register_fragment_container, ChooseTicketFragment(), addToBackStack = true)
                }
                FillOutUserInfoFragment::class.java.simpleName -> {
                    replaceChildFragment(R.id.register_fragment_container, FillOutUserInfoFragment())
                }
            }
        }
    }

    override fun errorHandler(statusCode: Int, message: String) {
        super.errorHandler(statusCode, message)
        progress_bar?.gone()
    }

    override fun onDestroy() {
        removeObservers(viewModel.eventDetail)
        removeObservers(viewModel.changeChildScreen)
        super.onDestroy()
    }
}