package com.think.runex.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class PermissionsLauncherActivity : BaseActivity() {

    private var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>? = null
    private var requestPermissionsCallback: ((resultsMap: Map<String, Boolean>) -> Unit)? = null

    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null
    private var requestPermissionCallback: ((isGranted: Boolean) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        requestPermissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultsMap ->
            requestPermissionsCallback?.invoke(resultsMap)
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            requestPermissionCallback?.invoke(isGranted)
        }

        super.onCreate(savedInstanceState, persistentState)
    }

    fun requestPermissions(permissions: Array<String>, callback: ((resultsMap: Map<String, Boolean>) -> Unit)? = null) {
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