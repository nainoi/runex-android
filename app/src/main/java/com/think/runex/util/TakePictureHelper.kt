package com.think.runex.util

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.think.runex.BuildConfig
import com.think.runex.common.clearTempDirectory
import com.think.runex.common.getTempDirectory
import com.think.runex.common.getUriProvider
import com.think.runex.config.APP_SCHEME
import java.io.File
import java.util.concurrent.TimeUnit

class TakePictureHelper() {

    constructor(fragment: Fragment) : this() {
        registerTakePictureResult(fragment)
    }

    constructor(activity: FragmentActivity) : this() {
        registerTakePictureResult(activity)
    }

    private var takePictureLauncher: ActivityResultLauncher<Uri>? = null
    private var takePictureCallback: ((uri: Uri?) -> Unit)? = null

    private var uri: Uri? = null

    fun getPictureUri() = uri

    fun registerTakePictureResult(fragment: Fragment) {
        try {
            takePictureLauncher = fragment.registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                when (isSuccess) {
                    true -> takePictureCallback?.invoke(uri)
                    false -> takePictureCallback?.invoke(null)
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun registerTakePictureResult(activity: FragmentActivity) {
        try {
            takePictureLauncher = activity.registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                when (isSuccess) {
                    true -> takePictureCallback?.invoke(uri)
                    false -> takePictureCallback?.invoke(null)
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun unregisterTakePictureResult() {
        takePictureLauncher?.unregister()
        takePictureLauncher = null
    }

    fun takePicture(context: Context, callback: (uri: Uri?) -> Unit) {
        takePictureCallback = callback
        uri = createTempUri(context)
        takePictureLauncher?.launch(uri)
    }

    private fun createTempUri(context: Context): Uri {
        val fireName = "${APP_SCHEME}_${TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())}"
        val tempFile = File.createTempFile(fireName, ".jpg", context.getTempDirectory("images"))
        return tempFile.getUriProvider(context)
    }

    fun clear(context: Context) = context.clearTempDirectory("images")
}