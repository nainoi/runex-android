package com.think.runex.common

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.think.runex.BuildConfig
import java.io.File

/**
 * * @param [child] The child pathname string
 */
fun Context.getTempDirectory(child: String? = null): File {
    return File(cacheDir, "temp${if (child?.isNotBlank() == true) "/$child" else ""}").let { directory ->
        if (directory.exists().not()) {
            directory.mkdirs()
        }
        directory
    }
}

/**
 * * @param [child] The child pathname string
 */
fun Context.clearTempDirectory(child: String? = null) {
    File(cacheDir, "temp${if (child?.isNotBlank() == true) "/$child" else ""}").deleteRecursively()
}

fun File.getUriProvider(context: Context): Uri {
    return FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", this)
}