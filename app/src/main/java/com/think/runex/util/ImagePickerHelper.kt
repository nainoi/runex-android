package com.think.runex.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import com.jozzee.android.core.datetime.dateTimeFormat
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.think.runex.BuildConfig
import com.think.runex.config.APP_SCHEME
import java.io.File

class ImagePickerHelper {

    fun provideCameraIntent(context: Context): Intent? {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(context.packageManager) == null) {
            Logger.error(simpleName(), "This Application do not have Camera Application")
            return null
        }
        //Create temp file.
        val directory = File(context.cacheDir, "images").let { directory ->
            if (directory.exists().not()) {
                directory.mkdirs()
            }
            directory
        }
        val fireName = "${APP_SCHEME}_${System.currentTimeMillis()}"
        val tempFile = File.createTempFile(fireName, ".jpg", directory)

        val uri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", tempFile)
        val resolvedIntentActivities = context.packageManager.queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolvedIntentInfo: ResolveInfo in resolvedIntentActivities) {
            context.grantUriPermission(resolvedIntentInfo.activityInfo.packageName, uri,
                    (Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION))
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        return cameraIntent
    }

    fun provideGalleryIntent() = Intent(Intent.ACTION_PICK).apply {
        type = "*/*"
        putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpg", "image/jpeg", "image/png"))
        data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    fun clearTempFile(context: Context) {
        File(context.cacheDir, "images").deleteRecursively()
    }
}