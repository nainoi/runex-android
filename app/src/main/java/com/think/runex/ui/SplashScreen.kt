package com.think.runex.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.jozzee.android.core.fragment.replaceFragment
import com.think.runex.R
import com.think.runex.common.fadeIn
import com.think.runex.feature.auth.AuthViewModel
import com.think.runex.feature.auth.TokenManager
import com.think.runex.utility.InjectorUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : ScreenFragment() {

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProviders.of(this, InjectorUtils.provideAuthViewModelFactory(context!!)).get(AuthViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_splash, container, false)
    }

    override fun onStart() {
        super.onStart()

        authViewModel.initialToken()
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            replaceFragment(if (TokenManager.isAlive()) MainScreen() else LoginScreen(),
                    fadeIn(), clearStack = true, addToBackStack = false)
        }
    }
}
