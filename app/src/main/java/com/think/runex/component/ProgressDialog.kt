package com.think.runex.component

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.fragment.app.DialogFragment
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.touch.setScreenTouchable
import com.jozzee.android.core.view.setVisible
import com.think.runex.R
import com.think.runex.common.setColorFilter
import kotlinx.android.synthetic.main.dialog_progress.*

class ProgressDialog : DialogFragment() {

    companion object {
        private const val KEY_MESSAGE = "msg"
        private const val KEY_PROGRESS_BAR_COLOR = "pColor"

        fun newInstance(message: String? = null,
                        @ColorRes progressColor: Int = -1) = ProgressDialog().apply {
            arguments = Bundle().apply {
                putString(KEY_MESSAGE, message)
                putInt(KEY_PROGRESS_BAR_COLOR, progressColor)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = false
        isCancelable = false
        activity?.setScreenTouchable(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.window?.setBackgroundDrawable(provideBackgroundDialog())
        return inflater.inflate(R.layout.dialog_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            setMessage(it.getString(KEY_MESSAGE))
            setProgressColor(it.getInt(KEY_PROGRESS_BAR_COLOR))
        }
    }

    fun setMessage(message: String?) {
        tv_message?.let {
            it.text = message ?: ""
            it.setVisible(message?.isNotBlank() == true)
        }
    }

    fun setProgressColor(@ColorRes color: Int) {
        if (color == -1) return
        progress_bar?.indeterminateDrawable?.setColorFilter(getColor(color))
    }

    private fun provideBackgroundDialog() = GradientDrawable().apply {
        shape = GradientDrawable.RECTANGLE
        setColor(this@ProgressDialog.getColor(android.R.color.transparent))
    }

    override fun onDestroy() {
        Runtime.getRuntime().gc()
        activity?.setScreenTouchable(true)
        //Logger.warning(simpleName(), "onDestroy")
        super.onDestroy()
    }
}