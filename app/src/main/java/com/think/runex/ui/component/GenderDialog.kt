package com.think.runex.ui.component

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jozzee.android.core.view.content
import com.think.runex.R
import com.think.runex.feature.user.Gender
import kotlinx.android.synthetic.main.dialog_gender.view.*

class GenderDialog : DialogFragment() {

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = false
        setStyle(STYLE_NORMAL, R.style.AppAlertDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_gender, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_gender, null)
        return MaterialAlertDialogBuilder(requireContext(), R.style.AppAlertDialog).apply {
            setView(rootView)
            setupComponents()
        }.create().also {
            it.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
    }

    private fun setupComponents() {
        if (::rootView.isInitialized.not()) return

        //Subscribe Ui
        rootView.female_label.setOnClickListener {
            getOnGenderSelectedListener()?.onGenderSelected(Gender.FEMALE)
            dismissAllowingStateLoss()
        }

        rootView.male_label.setOnClickListener {
            getOnGenderSelectedListener()?.onGenderSelected(Gender.MALE)
            dismissAllowingStateLoss()
        }

        rootView.other_label.setOnClickListener {
            getOnGenderSelectedListener()?.onGenderSelected(Gender.OTHER)
            dismissAllowingStateLoss()
        }
    }

    private fun getOnGenderSelectedListener(): OnGenderSelectedListener? {
        if (parentFragment != null && parentFragment is OnGenderSelectedListener) {
            return parentFragment as OnGenderSelectedListener
        } else if (activity != null && activity is OnGenderSelectedListener) {
            return activity as OnGenderSelectedListener
        }
        return null;
    }

    interface OnGenderSelectedListener {
        fun onGenderSelected(gender: String)
    }
}