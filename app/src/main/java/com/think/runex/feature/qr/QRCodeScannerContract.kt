package com.think.runex.feature.qr

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.StyleRes
import com.think.runex.config.KEY_QR

class QRCodeScannerContract : ActivityResultContract<QRCodeScannerContract.Input, QRCodeScannerContract.Output>() {

    companion object {
        fun parseIntent(activity: Activity): Input = with(activity) {
            Input(titleText = intent.getStringExtra("titleText"),
                    titleTextAppearance = intent.getIntExtra("titleTextAppearance", -1),
                    descriptionText = intent.getStringExtra("descriptionText"),
                    descriptionTextAppearance = intent.getIntExtra("descriptionTextAppearance", -1),
                    prefixFormat = intent.getStringExtra("prefixFormat"))
        }
    }

    override fun createIntent(context: Context, input: Input?): Intent {
        return Intent(context, QRCodeScannerActivity::class.java).apply {
            putExtra("titleText", input?.titleText)
            putExtra("titleTextAppearance", input?.titleTextAppearance)
            putExtra("descriptionText", input?.descriptionText)
            putExtra("descriptionTextAppearance", input?.descriptionTextAppearance)
            putExtra("prefixFormat", input?.prefixFormat)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Output {
        return Output(intent?.getStringExtra(KEY_QR))
    }

    data class Input(
            var titleText: String? = null,
            @StyleRes var titleTextAppearance: Int? = null,
            var descriptionText: String? = null,
            @StyleRes var descriptionTextAppearance: Int? = null,
            var prefixFormat: String? = null)

    data class Output(
            var data: String? = null)
}