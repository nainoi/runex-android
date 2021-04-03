package com.think.runex.util.extension

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.think.runex.R

fun Fragment.showSaveFileCompleteSnackBar(uri: Uri?) {
    if (uri == null) return
    val filename = uri.getDisplayName(requireContext()) ?: "Save file success."
    Snackbar.make(requireView(), "$filename Saved.", Snackbar.LENGTH_LONG)
            .setAction(R.string.open) {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    setDataAndType(uri, uri.getMimeType(requireContext()))
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                })
            }
            .show()
}