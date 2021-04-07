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
import com.think.runex.util.Localization
import com.think.runex.util.extension.setTextStyle
import kotlinx.android.synthetic.main.dialog_language.view.*

class LanguageDialog : BaseDialog() {

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Runex_AlertDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_language, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_language, null)
        return MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Runex_AlertDialog).apply {
            setView(rootView)
            setupComponents()
        }.create().also {
            it.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
    }

    private fun setupComponents() {
        if (::rootView.isInitialized.not()) return

        val currentLanguage = Localization.getCurrentLanguage(requireContext())
        if (currentLanguage == Localization.THAI_LANGUAGE) {
            rootView.thai_label?.setTextStyle(R.style.Text_BodyHeading_Accent_Bold)
            rootView.english_label?.setTextStyle(R.style.Text_BodyHeading_Primary)
        } else if (currentLanguage == Localization.ENGLISH_LANGUAGE) {
            rootView.thai_label?.setTextStyle(R.style.Text_BodyHeading_Primary)
            rootView.english_label?.setTextStyle(R.style.Text_BodyHeading_Accent_Bold)
        }

        //Subscribe Ui
        rootView.english_label?.setOnClickListener {
            getOnLanguageSelectedListener()?.onLanguageSelected(Localization.ENGLISH_LANGUAGE)
            dismissAllowingStateLoss()
        }

        rootView.thai_label.setOnClickListener {
            getOnLanguageSelectedListener()?.onLanguageSelected(Localization.THAI_LANGUAGE)
            dismissAllowingStateLoss()
        }
    }

    private fun getOnLanguageSelectedListener(): OnLanguageSelectedListener? {
        if (parentFragment != null && parentFragment is OnLanguageSelectedListener) {
            return parentFragment as OnLanguageSelectedListener
        } else if (activity != null && activity is OnLanguageSelectedListener) {
            return activity as OnLanguageSelectedListener
        }
        return null
    }

    interface OnLanguageSelectedListener {
        fun onLanguageSelected(key: String)
    }
}