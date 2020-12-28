package com.think.runex.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.fragment.replaceChildFragment
import com.jozzee.android.core.fragment.replaceFragment
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName

import com.think.runex.R
import com.think.runex.common.setStatusBarColor
import com.think.runex.ui.home.AllEventsScreen
import kotlinx.android.synthetic.main.screen_main.*

class MainScreen : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(isLightStatusBar = false)
        subscribeUi()
    }

    override fun onStart() {
        super.onStart()
        //Initial screen to feed screen first.
        //updateScreen(bottom_navigation.selectedItemId)
    }

    private fun setupComponents() {
    }

    private fun subscribeUi() {
//        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId != bottom_navigation.selectedItemId) {
//                true -> {
//                    Logger.info(simpleName(), "On navigation item selected Id: ${menuItem.itemId}")
//                    updateScreen(menuItem.itemId)
//                    true
//                }
//                false -> false
//            }
//
//        }
    }

    private fun updateScreen(itemId: Int) {
        //val lightStatusBar = (activity as AppCompatActivity).getCurrentNightMode() != AppCompatDelegate.MODE_NIGHT_YES
        when (itemId) {
            R.id.menu_home -> replaceChildFragment(R.id.navigation_host_fragment, AllEventsScreen())
            R.id.menu_my_events -> replaceChildFragment(R.id.navigation_host_fragment, MyEventsScreen())
            R.id.menu_record -> replaceFragment(RecordScreen())
        }
    }
}
