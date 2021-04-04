package com.think.runex.feature.qr

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class QRCodeAnalyzer() : ImageAnalysis.Analyzer {

    private var scanner: BarcodeScanner? = null

    var listener: AnalysisListener? = null

    constructor(listener: AnalysisListener) : this() {
        this.listener = listener
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(imageProxy: ImageProxy) {

        val mediaImage = imageProxy.image
        if (mediaImage != null) {

            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            if (scanner == null) {
                initialScanner()
            }

            scanner?.process(image)
                    ?.addOnSuccessListener { barcodes ->
                        for (barcode in barcodes) {
                            listener?.onQRCodeDetected(barcode)
                        }
                        imageProxy.close()
                    }
                    ?.addOnFailureListener { exception ->
                        // Task failed with an exception
                        // ...
                        exception.printStackTrace()
                        imageProxy.close()
                    }
                    ?.addOnCompleteListener {
                        imageProxy.close()
                    }
        }
    }

    private fun initialScanner() {
        val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
                .build()

        scanner = BarcodeScanning.getClient(options)
    }

    fun clear() {
        scanner?.close()
        scanner = null
    }

    interface AnalysisListener {
        fun onQRCodeDetected(barcode: Barcode)
    }

}