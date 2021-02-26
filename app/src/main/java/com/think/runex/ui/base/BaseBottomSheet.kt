package com.think.runex.ui.base

import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class BaseBottomSheet : BottomSheetDialogFragment() {

     fun getBottomSheetBehavior() = (dialog as? BottomSheetDialog)?.behavior
}
