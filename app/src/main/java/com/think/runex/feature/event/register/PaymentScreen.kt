package com.think.runex.feature.event.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.setupToolbar
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.toolbar.*

class PaymentScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance() = PaymentScreen().apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar, R.string.register_event, R.drawable.ic_navigation_back)
    }

    private fun subscribeUi() {

    }
}