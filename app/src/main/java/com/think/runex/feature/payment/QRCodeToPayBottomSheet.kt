package com.think.runex.feature.payment

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jozzee.android.core.permission.shouldShowPermissionRationale
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.showToast
import com.jozzee.android.core.view.visible
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.base.PermissionsLauncherBottomSheet
import com.think.runex.util.extension.*
import com.think.runex.feature.payment.data.PaymentType
import com.think.runex.util.launch
import kotlinx.android.synthetic.main.bottom_sheet_qr_code_to_pay.*

class QRCodeToPayBottomSheet : PermissionsLauncherBottomSheet() {

    private lateinit var viewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetStyle)

        viewModel = requireParentFragment().getViewModel(PaymentViewModel.Factory(requireContext()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_qr_code_to_pay, container, false)
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
            closeBottomSheet()
        }

        save_button?.setOnClickListener {
            checkPermissionAndSaveQRCodeToStorage()
        }
    }

    private fun performGenerateQRData() = launch {
        progress_layout?.visible()

        when (viewModel.paymentMethod?.type) {
            PaymentType.QR -> viewModel.generateQRImage(requireContext())?.also {
                qr_code_image?.setImageBitmap(it)
            }
            PaymentType.QR_CODE -> viewModel.generateQRCodeImage().also {
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

        val uri: Uri? = bitmap?.saveToExternalStorage(requireContext(), fileName)

        //Clear bitmap
        bitmap?.recycle()

        showSaveFileCompleteSnackBar(uri)

        return uri
    }

}