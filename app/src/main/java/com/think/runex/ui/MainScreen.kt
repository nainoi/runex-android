package com.think.runex.ui

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.jozzee.android.core.fragment.addChildFragment
import com.jozzee.android.core.fragment.childFragmentCount
import com.jozzee.android.core.fragment.childFragments
import com.jozzee.android.core.resource.getColor
import com.think.runex.R
import com.think.runex.config.KEY_SCREEN
import com.think.runex.config.RC_OPEN_GPS
import com.think.runex.ui.base.BaseScreen
import com.think.runex.ui.event.AllEventsScreen
import com.think.runex.ui.event.MyEventsScreen
import com.think.runex.ui.profile.ProfileScreen
import com.think.runex.ui.workout.WorkoutHistoryScreen
import com.think.runex.ui.workout.WorkoutScreen
import kotlinx.android.synthetic.main.screen_main.*

class MainScreen : BaseScreen() {

    companion object {
        fun newInstance(initialScreen: String? = null) = MainScreen().apply {
            arguments = Bundle().apply {
                putString(KEY_SCREEN, initialScreen)
            }
        }
    }

    private var selectedBottomBarPosition: Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        childFragments().forEach { childFragment ->
            childFragment.onHiddenChanged(hidden)
        }
    }

    override fun onStart() {
        super.onStart()
        //If not have any screen will be initial screen.
        if (childFragmentCount() == 0) {
            //Check request screen from click notification.
            when (arguments?.getString(KEY_SCREEN)) {
                //When initial screen is WorkoutScreen
                WorkoutScreen::class.java.simpleName -> {
                    selectedBottomBarPosition = 3
                    setActiveScreen<WorkoutScreen>(bottom_bar_menu_workout, bottom_bar_menu_workout_label)
                }
                //Default set home screen (AllEventsScreen)
                else -> {
                    selectedBottomBarPosition = 1
                    setActiveScreen<AllEventsScreen>(bottom_bar_menu_all_event_icon, bottom_bar_menu_all_event_label)
                }
            }
        }
    }

    private fun setupComponents() {
    }

    private fun subscribeUi() {
        bottom_bar_menu_all_event?.setOnClickListener {
            if (selectedBottomBarPosition == 1) return@setOnClickListener
            selectedBottomBarPosition = 1
            setActiveScreen<AllEventsScreen>(bottom_bar_menu_all_event_icon, bottom_bar_menu_all_event_label)
        }

        bottom_bar_menu_my_event?.setOnClickListener {
            if (selectedBottomBarPosition == 2) return@setOnClickListener
            selectedBottomBarPosition = 2
            setActiveScreen<MyEventsScreen>(bottom_bar_menu_my_event_icon, bottom_bar_menu_my_event_label)
        }

        bottom_bar_menu_workout?.setOnClickListener {
            if (selectedBottomBarPosition == 3) return@setOnClickListener
            selectedBottomBarPosition = 3
            setActiveScreen<WorkoutScreen>(bottom_bar_menu_workout, bottom_bar_menu_workout_label)
        }

        bottom_bar_menu_history?.setOnClickListener {
            if (selectedBottomBarPosition == 4) return@setOnClickListener
            selectedBottomBarPosition = 4
            setActiveScreen<WorkoutHistoryScreen>(bottom_bar_menu_history_icon, bottom_bar_menu_history_label)
        }

        bottom_bar_menu_profile?.setOnClickListener {
            if (selectedBottomBarPosition == 5) return@setOnClickListener
            selectedBottomBarPosition = 5
            setActiveScreen<ProfileScreen>(bottom_bar_menu_profile_icon, bottom_bar_menu_profile_label)
        }
    }

    fun forceToWorkoutScreen() {
        bottom_bar_menu_workout?.callOnClick()
    }

    private fun disableAllMenuInBottomBar() {
        val iconColor = getColor(R.color.iconColorSecondary)
        bottom_bar_menu_all_event_icon?.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY)
        bottom_bar_menu_my_event_icon?.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY)
        bottom_bar_menu_workout?.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY)
        bottom_bar_menu_history_icon?.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY)
        bottom_bar_menu_profile_icon?.setColorFilter(iconColor, PorterDuff.Mode.MULTIPLY)
        bottom_bar_menu_all_event_label?.isEnabled = false
        bottom_bar_menu_my_event_label?.isEnabled = false
        bottom_bar_menu_workout_label?.isEnabled = false
        bottom_bar_menu_history_label?.isEnabled = false
        bottom_bar_menu_profile_label?.isEnabled = false
    }

    private inline fun <reified T : Fragment> setActiveScreen(icon: ImageView?, label: AppCompatTextView?) {
        //Update bottom bar
        disableAllMenuInBottomBar()
        icon?.setColorFilter(getColor(R.color.iconColorAccent), PorterDuff.Mode.MULTIPLY)
        label?.isEnabled = true

        //Update screen fragment
        var fragmentIsAlready = false
        childFragments().forEach { childFragment ->
            childFragmentManager.commit(true) {
                if (childFragment is T) {
                    show(childFragment)
                    fragmentIsAlready = true
                } else {
                    hide(childFragment)
                }
            }
        }
        childFragmentManager.executePendingTransactions()
        if (fragmentIsAlready.not()) {
            addChildFragment(R.id.navigation_host_fragment, T::class.java.newInstance(),
                    hidePrevious = childFragmentCount(), tag = T::class.java.simpleName)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_OPEN_GPS -> childFragments().forEach { childFragment ->
                if (childFragment is WorkoutScreen) {
                    childFragment.onActivityResult(requestCode, resultCode, data)
                    return@forEach
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
