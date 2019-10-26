package com.think.runex.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.fragment.onBackPressed

import com.think.runex.R
import kotlinx.android.synthetic.main.screen_record.*

class RecordScreen : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_record, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_black.setOnClickListener {
            onBackPressed()
        }
    }
}
