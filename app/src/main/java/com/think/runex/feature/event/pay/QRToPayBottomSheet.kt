package com.think.runex.feature.event.pay

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.jozzee.android.core.permission.shouldShowPermissionRationale
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.showToast
import com.jozzee.android.core.view.visible
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.base.PermissionsLauncherBottomSheet
import com.think.runex.common.*
import com.think.runex.feature.payment.PaymentViewModel
import com.think.runex.feature.payment.PaymentViewModelFactory
import com.think.runex.feature.payment.data.PaymentType
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.bottom_sheet_qr_to_pay.*
import java.io.File
import java.io.FileOutputStream

class QRToPayBottomSheet : PermissionsLauncherBottomSheet() {

    private lateinit var viewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetStyle)

        viewModel = requireParentFragment().getViewModel(PaymentViewModelFactory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_qr_to_pay, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
        performGenerateQRData()
    }

    private fun setupComponents() {
        //Update bottom sheet show full height of layout.
        getBottomSheetBehavior()?.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        total_price_label?.text = ("${viewModel.getTotalPrice()} ${getString(R.string.thai_bath)}")
    }

    private fun subscribeUi() {
        close_button?.setOnClickListener {
            getBottomSheetBehavior()?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        save_button?.setOnClickListener {
            checkPermissionAndSaveQRCodeToStorage()
        }
    }

    private fun performGenerateQRData() = launch {
        progress_layout?.visible()

        when (viewModel.paymentMethod?.type) {
            PaymentType.QR -> viewModel.generateQRData(requireContext())?.also {
                qr_code_image?.setImageBitmap(it)
            }
            PaymentType.QR_CODE -> viewModel.generateQRCodeData(requireContext()).also {
                qr_code_image?.setImageBitmap(it)
            }
        }
        progress_layout?.gone()
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

        val fileName = "${BuildConfig.APP_SCHEME}_pay_${viewModel.orderId}.jpg"

        var uri: Uri? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val value = ContentValues().apply {
                put(MediaStore.Images.ImageColumns.DISPLAY_NAME, fileName)
                put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.Images.ImageColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/${BuildConfig.APP_NAME}")
                put(MediaStore.Images.Media.IS_PENDING, true)
                //RELATIVE_PATH and IS_PENDING are introduced in API 29.
            }

            uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, value)
            if (uri != null) {
                requireContext().contentResolver.openOutputStream(uri)?.use { outputStream ->
                    bitmap?.writeToOutputStream(outputStream)
                }
                value.put(MediaStore.Images.Media.IS_PENDING, false)
                requireContext().contentResolver.update(uri, value, null, null)
            }

        } else {

            //getExternalStorageDirectory is deprecated in API 29
            val outputFile = File("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)}/${BuildConfig.APP_NAME}").let { directory ->
                if (directory.exists().not()) {
                    directory.mkdirs()
                }
                File("$directory/$fileName")
            }
            uri = outputFile.getUriProvider(requireContext())
            bitmap?.writeToOutputStream(FileOutputStream(outputFile))
        }

        //Clear bitmap
        bitmap?.recycle()

        showSaveWorkoutImageCompleteSnackBar(uri)

        return uri
    }

    private fun showSaveWorkoutImageCompleteSnackBar(uri: Uri?) {
        if (uri == null) return
        val filename = uri.getDisplayName(requireContext()) ?: "Save image success."
        Snackbar.make(requireView(), filename, Snackbar.LENGTH_LONG)
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
}