package com.think.runex.feature.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.fragment.onBackPressed
import com.think.runex.R
import com.think.runex.base.BaseScreen
import com.think.runex.config.KEY_EVENT_NAME
import com.think.runex.util.extension.setStatusBarColor
import com.think.runex.config.KEY_ORDER_ID
import kotlinx.android.synthetic.main.screen_payment_success.*


class PayEventSuccessScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance(eventName: String, orderId: String) = PayEventSuccessScreen().apply {
            arguments = Bundle().apply {
                putString(KEY_EVENT_NAME, eventName)
                putString(KEY_ORDER_ID, orderId)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_payment_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = true)

        event_name_label?.text = arguments?.getString(KEY_EVENT_NAME) ?: ""
        order_id_label?.text = ("${getString(R.string.order_no)}: ${arguments?.getString(KEY_ORDER_ID) ?: ""}")
    }

    private fun subscribeUi() {
        ok_button?.setOnClickListener {
            onBackPressed()
        }
    }
}