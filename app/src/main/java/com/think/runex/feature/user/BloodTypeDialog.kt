package com.think.runex.feature.user

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.think.runex.R
import com.think.runex.feature.user.data.BloodType
import com.think.runex.util.extension.setTextStyle
import kotlinx.android.synthetic.main.dialog_blood_type.view.*

class BloodTypeDialog : DialogFragment() {

    companion object {
        private const val KEY_BLOOD_TYPE = "blood_type"

        @JvmStatic
        fun newInstance(currentBloodType: BloodType? = null) = BloodTypeDialog().apply {
            arguments = Bundle().apply {
                putString(KEY_BLOOD_TYPE, currentBloodType?.name)
            }
        }
    }

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Runex_AlertDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_blood_type, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_blood_type, null)
        return MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Runex_AlertDialog).apply {
            setView(rootView)
            setupComponents()
        }.create().also {
            it.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
    }

    private fun setupComponents() {
        if (::rootView.isInitialized.not()) return

        updateViewsOnBloodTypeSelected(arguments?.getString(KEY_BLOOD_TYPE)?.let { BloodType.valueOf(it) })

        rootView.blood_type_a_label?.setOnClickListener {
            getOnBloodTypeSelectedListener()?.onBloodTypeSelected(BloodType.A)
            dismissAllowingStateLoss()
        }
        rootView.blood_type_ab_label?.setOnClickListener {
            getOnBloodTypeSelectedListener()?.onBloodTypeSelected(BloodType.AB)
            dismissAllowingStateLoss()
        }
        rootView.blood_type_b_label?.setOnClickListener {
            getOnBloodTypeSelectedListener()?.onBloodTypeSelected(BloodType.B)
            dismissAllowingStateLoss()
        }
        rootView.blood_type_o_label?.setOnClickListener {
            getOnBloodTypeSelectedListener()?.onBloodTypeSelected(BloodType.O)
            dismissAllowingStateLoss()
        }
    }

    private fun updateViewsOnBloodTypeSelected(bloodType: BloodType?) {
        rootView.blood_type_a_label?.setIsSelected(bloodType == BloodType.A)
        rootView.blood_type_ab_label?.setIsSelected(bloodType == BloodType.AB)
        rootView.blood_type_b_label?.setIsSelected(bloodType == BloodType.B)
        rootView.blood_type_o_label?.setIsSelected(bloodType == BloodType.O)

    }

    private fun TextView.setIsSelected(isSelected: Boolean) {
        setTextStyle(if (isSelected) R.style.Text_BodyHeading_Accent_Bold else R.style.Text_BodyHeading_Primary)
    }


    private fun getOnBloodTypeSelectedListener(): OnBloodTypeSelectedListener? {
        if (parentFragment != null && parentFragment is OnBloodTypeSelectedListener) {
            return parentFragment as OnBloodTypeSelectedListener
        } else if (activity != null && activity is OnBloodTypeSelectedListener) {
            return activity as OnBloodTypeSelectedListener
        }
        return null
    }

    interface OnBloodTypeSelectedListener {
        fun onBloodTypeSelected(bloodType: BloodType)
    }
}