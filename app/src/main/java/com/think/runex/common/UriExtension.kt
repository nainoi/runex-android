package com.think.runex.common

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

fun Uri.getDisplayName(context: Context?): String? {
    return (context?.contentResolver?.query(this, null,
            null, null, null)?.use { cursor ->
        when (cursor.moveToFirst()) {
            true -> cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            false -> null
        }
    })
}

fun Uri.removeFile(context: Context?) = try {
    //Log.e("Uri extension", "Remove file: $this")
    val rowsDeleted = context?.contentResolver?.delete(this, null, null)
    //Log.e("Uri extension", "rows deleted: $rowsDeleted")
} catch (error: Throwable) {
    error.printStackTrace()
}