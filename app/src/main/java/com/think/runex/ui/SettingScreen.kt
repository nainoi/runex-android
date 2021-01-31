package com.think.runex.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.think.runex.R
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.toolbar.*

class SettingScreen : BaseScreen() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.setting, R.drawable.ic_navigation_back)
    }

    private fun subscribeUi() {

    }
}