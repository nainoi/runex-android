package com.think.runex.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.think.runex.R
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.RC_LOGIN
import kotlinx.android.synthetic.main.screen_on_boarding.*

class OnBoardingScreen : BaseScreen() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_on_boarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //Subscribe Ui
        get_started_button.setOnClickListener {
            requireActivity().startActivityForResult(Intent(requireContext(), LoginActivity::class.java), RC_LOGIN)
        }
    }
}