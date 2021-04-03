package com.think.runex.util

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class GetContentHelper() {

    constructor(fragment: Fragment, withMultipleContent: Boolean = false) : this() {
        when (withMultipleContent) {
            true -> registerGetMultipleContentResult(fragment)
            false -> registerGetContentResult(fragment)
        }
    }

    constructor(activity: FragmentActivity, withMultipleContent: Boolean = false) : this() {
        when (withMultipleContent) {
            true -> registerGetMultipleContentResult(activity)
            false -> registerGetContentResult(activity)
        }
    }

    private var getContentLauncher: ActivityResultLauncher<String>? = null
    private var getContentCallback: ((uri: Uri?) -> Unit)? = null

    private var getMultipleContentLauncher: ActivityResultLauncher<String>? = null
    private var getMultipleContentCallback: ((contents: List<Uri>?) -> Unit)? = null


    fun registerGetContentResult(fragment: Fragment) {
        try {
            getContentLauncher = fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                getContentCallback?.invoke(uri)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun registerGetMultipleContentResult(fragment: Fragment) {
        try {
            getMultipleContentLauncher = fragment.registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { contents ->
                getMultipleContentCallback?.invoke(contents)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun registerGetContentResult(activity: FragmentActivity) {
        try {
            getContentLauncher = activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                getContentCallback?.invoke(uri)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun registerGetMultipleContentResult(activity: FragmentActivity) {
        try {
            getMultipleContentLauncher = activity.registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { contents ->
                getMultipleContentCallback?.invoke(contents)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }


    fun unregisterGetContentResult() {
        getContentLauncher?.unregister()
        getContentLauncher = null
    }

    fun unregisterGetMultipleContentResult() {
        getMultipleContentLauncher?.unregister()
        getMultipleContentLauncher = null
    }

    fun unregisterAllResult() {
        unregisterGetContentResult()
        unregisterGetMultipleContentResult()
    }

    fun getImage(callback: ((image: Uri?) -> Unit)) {
        getContentCallback = callback
        getContentLauncher?.launch("image/*")
    }

    fun getImages(callback: ((images: List<Uri>?) -> Unit)) {
        getMultipleContentCallback = callback
        getMultipleContentLauncher?.launch("image/*")
    }
}