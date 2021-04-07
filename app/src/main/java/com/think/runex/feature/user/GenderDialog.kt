package com.think.runex.feature.user

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.think.runex.R
import com.think.runex.feature.user.data.Gender
import com.think.runex.util.extension.setTextStyle
import kotlinx.android.synthetic.main.dialog_gender.view.*

class GenderDialog : DialogFragment() {

    private lateinit var rootView: View

    companion object {
        private const val KEY_GENDER = "gender"

        @JvmStatic
        fun newInstance(currentGender: Gender? = null) = GenderDialog().apply {
            arguments = Bundle().apply {
                putString(KEY_GENDER, currentGender?.name)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Runex_AlertDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_gender, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_gender, null)
        return MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Runex_AlertDialog).apply {
            setView(rootView)
            setupComponents()
        }.create().also {
            it.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
    }

    private fun setupComponents() {
        if (::rootView.isInitialized.not()) return

        updateViewsOnGenderSelected(arguments?.getString(KEY_GENDER)?.let { Gender.valueOf(it) })

        //Subscribe Ui
        rootView.female_label?.setOnClickListener {
            getOnGenderSelectedListener()?.onGenderSelected(Gender.Female)
            dismissAllowingStateLoss()
        }

        rootView.male_label?.setOnClickListener {
            getOnGenderSelectedListener()?.onGenderSelected(Gender.Male)
            dismissAllowingStateLoss()
        }

        rootView.other_label?.setOnClickListener {
            getOnGenderSelectedListener()?.onGenderSelected(Gender.Other)
            dismissAllowingStateLoss()
        }
    }

    private fun updateViewsOnGenderSelected(gender: Gender?) {
        rootView.female_label?.setIsSelected(gender == Gender.Female)
        rootView.male_label?.setIsSelected(gender == Gender.Male)
        rootView.other_label?.setIsSelected(gender == Gender.Other)
    }

    private fun TextView.setIsSelected(isSelected: Boolean) {
        setTextStyle(if (isSelected) R.style.Text_BodyHeading_Accent_Bold else R.style.Text_BodyHeading_Primary)
    }

    private fun getOnGenderSelectedListener(): OnGenderSelectedListener? {
        if (parentFragment != null && parentFragment is OnGenderSelectedListener) {
            return parentFragment as OnGenderSelectedListener
        } else if (activity != null && activity is OnGenderSelectedListener) {
            return activity as OnGenderSelectedListener
        }
        return null
    }

    interface OnGenderSelectedListener {
        fun onGenderSelected(gender: Gender)
    }
}