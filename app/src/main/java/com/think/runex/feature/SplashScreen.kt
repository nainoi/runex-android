package com.think.runex.feature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.think.runex.R
import com.think.runex.common.setStatusBarColor
import com.think.runex.base.BaseScreen
import com.think.runex.feature.auth.login.LoginScreen
import com.think.runex.util.NightMode
import kotlinx.android.synthetic.main.screen_splash.*

class SplashScreen : BaseScreen() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Setup components
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
    }
}
