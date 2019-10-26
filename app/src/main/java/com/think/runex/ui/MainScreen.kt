package com.think.runex.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.fragment.replaceChildFragment
import com.jozzee.android.core.fragment.replaceFragment
import com.jozzee.android.core.simpleName
import com.jozzee.android.core.utility.Logger

import com.think.runex.R
import com.think.runex.ui.home.HomeScreen
import kotlinx.android.synthetic.main.screen_main.*

class MainScreen : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeUi()
    }

    override fun onStart() {
        super.onStart()
        //Initial screen to feed screen first.
        updateScreen(bottom_navigation.selectedItemId)
    }

    private fun setupComponents() {

    }

    private fun subscribeUi() {
        bottom_navigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId != bottom_navigation.selectedItemId) {
                true -> {
                    Logger.info(simpleName(), "On navigation item selected Id: ${menuItem.itemId}")
                    updateScreen(menuItem.itemId)
                    true
                }
                false -> false
            }

        }
    }

    private fun updateScreen(itemId: Int) {
        //val lightStatusBar = (activity as AppCompatActivity).getCurrentNightMode() != AppCompatDelegate.MODE_NIGHT_YES
        when (itemId) {
            R.id.menu_home -> replaceChildFragment(R.id.navigation_host_fragment, HomeScreen())
            R.id.menu_my_events -> replaceChildFragment(R.id.navigation_host_fragment, MyEventsScreen())
            R.id.menu_record -> replaceFragment(RecordScreen())
            R.id.menu_challenges -> replaceChildFragment(R.id.navigation_host_fragment, ChallengesScreen())
            R.id.menu_runex_sync -> replaceChildFragment(R.id.navigation_host_fragment, RunexConnectScreen())
        }
    }
}
