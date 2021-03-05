package com.think.runex.feature.event.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.think.runex.R
import com.think.runex.base.BaseScreen

class RegisterEventScreen : BaseScreen() {

    companion object {
        @JvmStatic
        fun newInstance() = RegisterEventScreen().apply {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_register_event, container, false)
    }
}