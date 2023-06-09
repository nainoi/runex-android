package com.think.runex.feature.event.detail

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jozzee.android.core.view.content
import com.jozzee.android.core.view.showToast
import com.think.runex.R
import com.think.runex.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_register_event_with_ebib_dialog.view.*

class RegisterEventWithEBIBDialog : BaseDialog() {

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_Runex_AlertDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        return inflater.inflate(R.layout.dialog_register_event_with_ebib_dialog, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_register_event_with_ebib_dialog, null)
        return MaterialAlertDialogBuilder(requireContext(), R.style.Theme_Runex_AlertDialog).apply {
            setView(rootView)
            setupComponents()
        }.create().also {
            it.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
    }

    private fun setupComponents() {
        if (::rootView.isInitialized.not()) return

        //Subscribe Ui
        rootView.negative_button?.setOnClickListener {
            dismissAllowingStateLoss()
        }

        rootView.positive_button?.setOnClickListener {
            rootView.ebib_input.content().also { eBib ->
                when (eBib.isNotBlank()) {
                    true -> {
                        getOnEBIBSpecifiedListener()?.onEBIBSpecified(eBib)
                        dismissAllowingStateLoss()
                    }
                    false -> showToast(R.string.please_specify_your_ebib)
                }
            }

        }
    }

    private fun getOnEBIBSpecifiedListener(): OnEBIBSpecifiedListener? {
        if (parentFragment != null && parentFragment is OnEBIBSpecifiedListener) {
            return parentFragment as OnEBIBSpecifiedListener
        } else if (activity != null && activity is OnEBIBSpecifiedListener) {
            return activity as OnEBIBSpecifiedListener
        }
        return null
    }

    interface OnEBIBSpecifiedListener {
        fun onEBIBSpecified(eBib: String)
    }

}