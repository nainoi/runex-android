package com.think.runex.base

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.think.runex.R
import com.think.runex.util.extension.showAlertDialog
import com.think.runex.datasource.api.ApiExceptionMessage

open class BaseBottomSheet : BottomSheetDialogFragment() {

    fun getBottomSheetBehavior() = (dialog as? BottomSheetDialog)?.behavior

    fun closeBottomSheet() {
        getBottomSheetBehavior()?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    open fun errorHandler(code: Int, message: String, tag: String? = null) {
        if (isAdded.not() || view == null) return

        val errorMessage = ApiExceptionMessage.getExceptionMessageFromStatusCode(resources, code, message)
        Logger.error(simpleName(), "Error Handler: Status code: $code, Message: $errorMessage")

        activity?.runOnUiThread {
            if (errorMessage.isNotBlank()) {
                //Show alert dialog if have error message.
                showAlertDialog(getString(R.string.error), errorMessage)
            }
        }
    }
}
