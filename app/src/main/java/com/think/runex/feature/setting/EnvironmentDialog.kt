package com.think.runex.feature.setting

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.think.runex.R
import com.think.runex.base.BaseDialog
import com.think.runex.feature.setting.data.Environment
import com.think.runex.util.AppPreference
import com.think.runex.util.extension.setTextStyle
import kotlinx.android.synthetic.main.dialog_environment.view.*

class EnvironmentDialog : BaseDialog() {

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Runex_AlertDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_environment, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_environment, null)
        return MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Runex_AlertDialog).apply {
            setView(rootView)
            setupComponents()
        }.create().also {
            it.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
    }

    private fun setupComponents() {
        if (::rootView.isInitialized.not()) return

        val environment = AppPreference.getEnvironment(requireContext())
        if (environment == Environment.DEV) {
            rootView.environment_dev_label?.setTextStyle(R.style.Text_BodyHeading_Accent_Bold)
            rootView.environment_production_label?.setTextStyle(R.style.Text_BodyHeading_Primary)
        } else if (environment == Environment.PRODUCTION) {
            rootView.environment_dev_label?.setTextStyle(R.style.Text_BodyHeading_Primary)
            rootView.environment_production_label?.setTextStyle(R.style.Text_BodyHeading_Accent_Bold)
        }

        //Subscribe Ui
        rootView.environment_dev_label?.setOnClickListener {
            getOnLanguageSelectedListener()?.onEnvironmentSelected(Environment.DEV)
            dismissAllowingStateLoss()
        }

        rootView.environment_production_label.setOnClickListener {
            getOnLanguageSelectedListener()?.onEnvironmentSelected(Environment.PRODUCTION)
            dismissAllowingStateLoss()
        }
    }

    private fun getOnLanguageSelectedListener(): OnEnvironmentSelectedListener? {
        if (parentFragment != null && parentFragment is OnEnvironmentSelectedListener) {
            return parentFragment as OnEnvironmentSelectedListener
        } else if (activity != null && activity is OnEnvironmentSelectedListener) {
            return activity as OnEnvironmentSelectedListener
        }
        return null
    }

    interface OnEnvironmentSelectedListener {
        fun onEnvironmentSelected(@Environment environment: Int)
    }
}