package com.think.runex.ui.base

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class PermissionsLauncherBottomSheet : BaseBottomSheet() {

    private var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>? = null
    private var requestPermissionsCallback: ((results: Map<String, Boolean>) -> Unit)? = null

    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null
    private var requestPermissionCallback: ((isGranted: Boolean) -> Unit)? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            requestPermissionsCallback?.invoke(results)
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            requestPermissionCallback?.invoke(isGranted)
        }
    }

    fun requestPermissions(permissions: Array<String>, callback: ((results: Map<String, Boolean>) -> Unit)? = null) {
        requestPermissionsCallback = callback
        requestPermissionsLauncher?.launch(permissions)
    }

    fun requestPermission(permission: String, callback: ((isGranted: Boolean) -> Unit)? = null) {
        requestPermissionCallback = callback
        requestPermissionLauncher?.launch(permission)
    }

    override fun onDestroy() {
        requestPermissionsLauncher?.unregister()
        requestPermissionsLauncher = null
        requestPermissionLauncher?.unregister()
        requestPermissionLauncher = null
        super.onDestroy()
    }
}