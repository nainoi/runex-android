package com.think.runex.ui.workout.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.resource.getDrawable
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.common.setColorFilter
import com.think.runex.common.showAlertDialog
import com.think.runex.feature.workout.model.WorkoutStatus
import kotlinx.android.synthetic.main.fragment_action_controls.*

class ActionControlsFragment : Fragment() {

    @WorkoutStatus
    var status: Int = WorkoutStatus.UNKNOWN
        set(value) {
            if (field != value) {
                field = value
                updateViews()
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_action_controls, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateViews()
    }

    private fun updateViews() {
        when (status) {
            WorkoutStatus.UNKNOWN -> setViewForDisableAction()
            WorkoutStatus.READY -> setViewsForStartAction()
            WorkoutStatus.WORKING_OUT -> setViewsForPauseAction()
            WorkoutStatus.PAUSE -> setViewForResumeOrStopAction()
            WorkoutStatus.STOP -> setViewForDisableAction()
        }
    }

    private fun setViewForDisableAction() {
        main_action_button?.isEnabled = false
        main_action_button?.setBackgroundResource(R.drawable.shape_circle_border_disable)
        main_action_icon?.setBackgroundResource(R.drawable.shape_circle_disable)
        main_action_icon?.drawable?.apply {
            setColorFilter(getColor(R.color.iconColorDisable))
        }
        //main_control_icon?.setColorFilter(getColor(R.color.disable), PorterDuff.Mode.MULTIPLY)
        main_action_label?.setTextColor(getColor(R.color.textColorHint))

        resume_action_button_layout?.gone()
    }

    private fun setViewsForStartAction() {
        main_action_button?.isEnabled = true
        main_action_button?.setBackgroundResource(R.drawable.shape_circle_border)
        main_action_icon?.setBackgroundResource(R.drawable.shape_circle_accent)
        main_action_icon?.setImageResource(R.drawable.ic_play)
        main_action_label?.setTextColor(getColor(R.color.textColorThirdly))
        main_action_label?.setText(R.string.start_recording)
        main_action_button?.setOnClickListener {
            getActionControlsListener()?.onActionStart()
        }

        resume_action_button_layout?.gone()
    }

    private fun setViewsForPauseAction() {
        main_action_button?.isEnabled = true
        main_action_button?.setBackgroundResource(R.drawable.shape_circle_border_asscent)
        main_action_icon?.setBackgroundResource(R.drawable.shape_circle_secondary)
        main_action_icon?.setImageDrawable(getDrawable(R.drawable.ic_pause)?.apply {
            setColorFilter(getColor(R.color.iconColorAccent))
        })
        main_action_label?.setTextColor(getColor(R.color.textColorThirdly))
        main_action_label?.setText(R.string.pause_recording)
        main_action_button?.setOnClickListener {
            getActionControlsListener()?.onActionPause()
        }

        resume_action_button_layout?.gone()
    }

    private fun setViewForResumeOrStopAction() {
        main_action_button?.isEnabled = true
        main_action_button?.setBackgroundResource(R.drawable.shape_circle_border_secondary)
        main_action_icon?.setBackgroundResource(R.drawable.shape_circle_secondary)
        main_action_icon?.setImageResource(R.drawable.ic_stop)
        main_action_label?.setTextColor(getColor(R.color.textColorThirdly))
        main_action_label?.setText(R.string.stop_recording)
        main_action_button?.setOnClickListener {
            showConfirmToStopDialog()
        }

        resume_action_button_layout?.visible()
        resume_action_button?.setOnClickListener {
            getActionControlsListener()?.onActionResume()
        }
    }

    private fun showConfirmToStopDialog() {
        showAlertDialog(R.string.confirm, R.string.confirm_to_ending_run, R.string.confirm, R.string.cancel, onPositiveClick = {
            getActionControlsListener()?.onActionStop()
        })
    }

    private fun getActionControlsListener(): ActionControlsListener? {
        if (parentFragment != null && parentFragment is ActionControlsListener) {
            return parentFragment as ActionControlsListener
        } else if (activity != null && activity is ActionControlsListener) {
            return activity as ActionControlsListener
        }
        return null
    }

    interface ActionControlsListener {
        fun onActionStart()

        fun onActionPause()

        fun onActionResume()

        fun onActionStop()
    }
}