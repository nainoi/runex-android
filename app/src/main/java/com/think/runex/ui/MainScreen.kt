package com.think.runex.ui

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.jozzee.android.core.fragment.childFragmentCount
import com.jozzee.android.core.fragment.replaceChildFragment
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.resource.getDrawable
import com.think.runex.R
import com.think.runex.common.setStatusBarColor
import com.think.runex.ui.base.BaseScreen
import com.think.runex.ui.event.AllEventsScreen
import com.think.runex.ui.event.MyEventsScreen
import com.think.runex.ui.profile.ProfileScreen
import com.think.runex.ui.workout.WorkoutHistoryScreen
import com.think.runex.ui.workout.WorkoutScreen
import com.think.runex.util.runOnUiThread
import kotlinx.android.synthetic.main.screen_main.*

class MainScreen : BaseScreen() {

    private var selectedBottomBarPosition: Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusBarColor(isLightStatusBar = false)
        setupComponents()
        subscribeUi()
    }

    override fun onStart() {
        super.onStart()
        //If not have any screen will be initial screen.
        if (childFragmentCount() == 0) {
            updateScreen(selectedBottomBarPosition)
        }
    }

    private fun setupComponents() {
    }

    private fun subscribeUi() {
        bottom_bar_menu_all_event?.setOnClickListener {
            if (selectedBottomBarPosition == 1) return@setOnClickListener
            updateScreen(1)
        }

        bottom_bar_menu_my_event?.setOnClickListener {
            if (selectedBottomBarPosition == 2) return@setOnClickListener
            updateScreen(2)
        }

        bottom_bar_menu_workout?.setOnClickListener {
            if (selectedBottomBarPosition == 3) return@setOnClickListener
            updateScreen(3)
        }

        bottom_bar_menu_history?.setOnClickListener {
            if (selectedBottomBarPosition == 4) return@setOnClickListener
            updateScreen(4)
        }

        bottom_bar_menu_profile?.setOnClickListener {
            if (selectedBottomBarPosition == 5) return@setOnClickListener
            updateScreen(5)
        }
    }

    private fun updateScreen(position: Int) = runOnUiThread {
        selectedBottomBarPosition = position
        disableAllMenuInBottomBar()
        when (position) {
            1 -> {
                setActiveMeuBottomBar(bottom_bar_menu_all_event_icon, bottom_bar_menu_all_event_label)
                replaceChildFragment(R.id.navigation_host_fragment, AllEventsScreen(), clearChildFragment = true)
            }
            2 -> {
                setActiveMeuBottomBar(bottom_bar_menu_my_event_icon, bottom_bar_menu_my_event_label)
                replaceChildFragment(R.id.navigation_host_fragment, MyEventsScreen(), clearChildFragment = true)
            }
            3 -> {
                bottom_bar_menu_workout?.background = getDrawable(R.drawable.shape_circle_accent)
                bottom_bar_menu_workout_label?.isEnabled = true
                replaceChildFragment(R.id.navigation_host_fragment, WorkoutScreen(), clearChildFragment = true)
            }
            4 -> {
                setActiveMeuBottomBar(bottom_bar_menu_history_icon, bottom_bar_menu_history_label)
                replaceChildFragment(R.id.navigation_host_fragment, WorkoutHistoryScreen(), clearChildFragment = true)
            }
            5 -> {
                setActiveMeuBottomBar(bottom_bar_menu_profile_icon, bottom_bar_menu_profile_label)
                replaceChildFragment(R.id.navigation_host_fragment, ProfileScreen(), clearChildFragment = true)
            }
        }
    }

    private fun disableAllMenuInBottomBar() {
        val iconColor = getColor(R.color.iconColorSecondaryInDarkBackground)
        bottom_bar_menu_all_event_icon?.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY)
        bottom_bar_menu_my_event_icon?.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY)
        bottom_bar_menu_workout?.background = getDrawable(R.drawable.bg_bottom_bar_center_menu_icon)
        bottom_bar_menu_history_icon?.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY)
        bottom_bar_menu_profile_icon?.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY)
        bottom_bar_menu_all_event_label?.isEnabled = false
        bottom_bar_menu_my_event_label?.isEnabled = false
        bottom_bar_menu_workout_label?.isEnabled = false
        bottom_bar_menu_history_label?.isEnabled = false
        bottom_bar_menu_profile_label?.isEnabled = false
    }

    private fun setActiveMeuBottomBar(icon: AppCompatImageView?, label: AppCompatTextView?) {
        icon?.setColorFilter(getColor(R.color.iconColorAccent), PorterDuff.Mode.MULTIPLY)
        label?.isEnabled = true
    }
}
