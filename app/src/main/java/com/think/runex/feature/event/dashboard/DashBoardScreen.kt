package com.think.runex.feature.event.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.toolbar.*

class DashBoardScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance() = DashBoardScreen().apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_dash_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.dashBoard, R.drawable.ic_navigation_back)
    }

    private fun subscribeUi() {

    }
}