package com.think.runex.util.extension

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap

fun Uri.isImage(context: Context?): Boolean {
    return context?.contentResolver?.getType(this)?.startsWith("image") == true
}

fun Uri.isVideo(context: Context?): Boolean {
    return context?.contentResolver?.getType(this)?.startsWith("video") == true
}

fun Uri.isAudio(context: Context?): Boolean {
    return context?.contentResolver?.getType(this)?.startsWith("audio") == true
}

fun Uri.isTextPlan(context: Context?): Boolean {
    return context?.contentResolver?.getType(this)?.startsWith("text") == true
}

fun Uri.isValidAudio(context: Context?): Boolean = try {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, this)
    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO) == "yes"
} catch (e: Throwable) {
    false
}

fun Uri.getSize(context: Context?): Long? {
    return (context?.contentResolver?.query(this, null,
            null, null, null)?.use { cursor ->
        when (cursor.moveToFirst()) {
            true -> cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE))
            false -> null
        }
    })
}

fun Uri.getSizeKb(context: Context?): Float = (getSize(context) ?: 0) / 1024f

fun Uri.getDisplayName(context: Context?): String? {
    return (context?.contentResolver?.query(this, null,
            null, null, null)?.use { cursor ->
        when (cursor.moveToFirst()) {
            true -> cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            false -> null
        }
    })
}

fun Uri.getMimeType(context: Context?): String? {
    return context?.contentResolver?.getType(this)
}

fun Uri.getExtensionFromMimeType(context: Context?): String? {
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(context?.contentResolver?.getType(this))
}

fun Uri.removeFile(context: Context?) = try {
    //Log.e("Uri extension", "Remove file: $this")
    val rowsDeleted = context?.contentResolver?.delete(this, null, null)
    //Log.e("Uri extension", "rows deleted: $rowsDeleted")
} catch (error: Throwable) {
    error.printStackTrace()
}