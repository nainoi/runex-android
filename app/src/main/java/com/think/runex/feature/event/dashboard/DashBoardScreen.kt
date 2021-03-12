package com.think.runex.feature.event.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.think.runex.R
import com.think.runex.base.BaseScreen

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
}