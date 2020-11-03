package com.think.runex.ui.base

import androidx.fragment.app.DialogFragment
import com.jozzee.android.core.view.hideKeyboard

open class BaseDialog : DialogFragment() {

    /**
     * Hid keyboard before destroy view.
     */
    override fun onDestroyView() {
        view?.hideKeyboard()
        super.onDestroyView()
    }
}