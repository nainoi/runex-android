package com.think.runex.component

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.think.runex.R
import kotlinx.android.synthetic.main.dialog_image_sources.view.*

class ImageSourcesDialog : DialogFragment() {

    companion object {
        const val SOURCE_CAMERA = 1
        const val SOURCE_GALLERY = 2
    }

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppAlertDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_image_sources, container, false)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        rootView = LayoutInflater.from(context).inflate(R.layout.dialog_image_sources, null)
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
        rootView.camera_label.setOnClickListener {
            getOnSelectImageSourceListener()?.onSelectImageSource(SOURCE_CAMERA)
            dismissAllowingStateLoss()
        }

        rootView.select_image_label.setOnClickListener {
            getOnSelectImageSourceListener()?.onSelectImageSource(SOURCE_GALLERY)
            dismissAllowingStateLoss()
        }
    }

    private fun getOnSelectImageSourceListener(): OnSelectImageSourceListener? {
        if (parentFragment != null && parentFragment is OnSelectImageSourceListener) {
            return parentFragment as OnSelectImageSourceListener
        } else if (activity != null && activity is OnSelectImageSourceListener) {
            return activity as OnSelectImageSourceListener
        }
        return null
    }

    interface OnSelectImageSourceListener {
        fun onSelectImageSource(source: Int)
    }

}