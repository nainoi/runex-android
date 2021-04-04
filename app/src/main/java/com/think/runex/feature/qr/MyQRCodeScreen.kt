package com.think.runex.feature.qr

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import com.jozzee.android.core.permission.shouldShowPermissionRationale
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.showToast
import com.jozzee.android.core.view.visible
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.base.PermissionsLauncherScreen
import com.think.runex.util.extension.*
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.screen_my_qr_code.*
import kotlinx.android.synthetic.main.toolbar.*

class MyQRCodeScreen : PermissionsLauncherScreen() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_my_qr_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()

        performGenerateQR()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(requireContext()).not())
        setupToolbar(toolbar_layout, R.string.my_qr_code, R.drawable.ic_navigation_back)
    }

    private fun subscribeUi() {
        save_button?.setOnClickListener {
            checkPermissionAndSaveQRCodeToStorage()
        }
    }

    private fun performGenerateQR() = launch {
        save_button?.isClickable = false
        progress_bar?.visible()

        val data = "RUNEX|${getUserViewModel().getUSerInfoInstance()?.id ?: ""}"
        qr_code_image?.setImageBitmap(QRCodeUtil().generateQRCode(requireContext(), data))

        progress_bar?.gone()
        save_button?.isClickable = true
    }

    private fun checkPermissionAndSaveQRCodeToStorage() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permissions) { results ->
            when {
                //User allow all permissions read and write storage
                allPermissionsGranted(results) -> {
                    saveQRCodeToStorage()
                }
                //User denied access storage.
                shouldShowPermissionRationale(permissions) -> showToast(R.string.storage_permission_denied)
                //User denied and select don't ask again.
                else -> requireContext().showSettingPermissionInSettingDialog()
            }
        }
    }

    private fun saveQRCodeToStorage(): Uri? {

        val bitmap = qr_code_image?.drawToBitmap()
        val fileName = "${BuildConfig.APP_SCHEME}_my_qr_code.jpg"

        val uri: Uri? = bitmap?.saveToExternalStorage(requireContext(), fileName)

        //Clear bitmap
        bitmap?.recycle()

        showSaveFileCompleteSnackBar(uri)

        return uri
    }
}