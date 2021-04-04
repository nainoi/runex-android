package com.think.runex.feature.qr

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.util.Size
import android.util.SparseIntArray
import android.view.MenuItem
import android.view.Surface
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.impl.ImageAnalysisConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.Barcode
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.inVisible
import com.jozzee.android.core.view.showToast
import com.jozzee.android.core.view.visible
import com.think.runex.R
import com.think.runex.base.PermissionsLauncherActivity
import com.think.runex.config.KEY_QR
import com.think.runex.util.GetContentHelper
import com.think.runex.util.NightMode
import com.think.runex.util.extension.*
import com.think.runex.util.extension.launch
import kotlinx.android.synthetic.main.activity_qr_code_scanner.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.delay
import java.util.concurrent.Executors

class QRCodeScannerActivity : PermissionsLauncherActivity(), QRCodeAnalyzer.AnalysisListener {

    companion object {
        const val PREFIX_RUNEX = "RUNEX|"
    }

    private var getContentHelper: GetContentHelper? = null

    private val orientations = SparseIntArray()

    //Camera and preview
    private lateinit var cameraProvider: ProcessCameraProvider
    private val cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var preview: Preview? = null
    private var displayId = -1

    //QR Code analyzer
    private var imageAnalyzer: ImageAnalysis? = null
    private lateinit var qrCodeAnalyzer: QRCodeAnalyzer

    /** Internal reference of the [DisplayManager] */
    private lateinit var displayManager: DisplayManager

    /**
     * We need a display listener for orientation changes that do not trigger a configuration
     * change, for example if we choose to override config change in manifest or for 180-degree
     * orientation changes.
     */
    private val displayListener = object : DisplayManager.DisplayListener {

        override fun onDisplayAdded(displayId: Int) = Unit

        override fun onDisplayRemoved(displayId: Int) = Unit

        @SuppressLint("UnsafeExperimentalUsageError")
        override fun onDisplayChanged(displayId: Int) = root_layout?.let { view ->
            if (displayId == this@QRCodeScannerActivity.displayId) {
                Log.d(simpleName(), "Rotation changed: ${view.display.rotation}")
                preview?.targetRotation = view.display.rotation
                //imageAnalyzer?.setTargetRotation(view.display.rotation)
            }
        } ?: Unit
    }

    private var prefixFormat: String? = null

    init {
        orientations.append(Surface.ROTATION_0, 0)
        orientations.append(Surface.ROTATION_90, 90)
        orientations.append(Surface.ROTATION_180, 180)
        orientations.append(Surface.ROTATION_270, 270)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getContentHelper = GetContentHelper(this)

        setContentView(R.layout.activity_qr_code_scanner)

        setStatusBarColor(isLightStatusBar = NightMode.isNightMode(this).not())
        setupToolbar(toolbar_layout, "", getDrawable(R.drawable.ic_close, ContextCompat.getColor(this, R.color.iconColorWhite)))

        //Setup components
        qrCodeAnalyzer = QRCodeAnalyzer(this)

        //Update view from input data
        val input = QRCodeScannerContract.parseIntent(this)
        prefixFormat = input.prefixFormat
        title_label?.text = input.titleText ?: ""
        description_label?.text = input.descriptionText ?: ""

        if (input.titleTextAppearance != null) {
            title_label?.setTextStyle(input.titleTextAppearance!!)
        }
        if (input.descriptionTextAppearance != null) {
            description_label.setTextStyle(input.descriptionTextAppearance!!)
        }

        //Subscribe Ui
        upload_qr_button?.setOnClickListener {
            checkPermissionAndGetImageContent()
        }

        open_camera_button?.setOnClickListener {
            checkPermissionsAndStartCameraPreview()
        }

    }

    override fun onStart() {
        super.onStart()
        checkPermissionsAndStartCameraPreview()
    }

    @SuppressLint("RestrictedApi")
    private fun startCameraPreview() {
        hideOpenCameraButton()
        camera_preview?.visible()
        qr_code_overlay?.visible()

        //Wait for the views to be properly laid out
        camera_preview?.post {
            //Keep track of the display in which this view is attached
            displayId = camera_preview.display.displayId

            //Log.w("Jozzee", "PreviewView Width: ${camera_preview?.width}, Height: ${camera_preview?.height}")

            //Build UI controls and bind all camera use cases
            val screenAspectRatio = Rational(camera_preview.height, camera_preview.width)
            //val screenAspectRatio = Rational(1, 1)

            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

            cameraProviderFuture.addListener({
                // Used to bind the lifecycle of cameras to the lifecycle owner
                cameraProvider = cameraProviderFuture.get()

                //Preview
                preview = Preview.Builder()
                        //.setDefaultResolution(Size(camera_preview.width, camera_preview.height))
                        //.setTargetResolution(Size(camera_preview.width, camera_preview.height))
                        .build()
                        .also {
                            it.setSurfaceProvider(camera_preview.surfaceProvider)
                        }

                imageAnalyzer = ImageAnalysis.Builder()
                        .build()
                        .also {
                            it.setAnalyzer(Executors.newSingleThreadExecutor(), qrCodeAnalyzer)
                        }

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)

//                    preview?.attachedSurfaceResolution?.also {
//                        Log.e("Jozzee", "Width: ${it.width}, Height: ${it.height}")
//                    }


                } catch (exc: Exception) {
                    Log.e(simpleName(), "Use case binding failed", exc)
                }

            }, ContextCompat.getMainExecutor(this))
        }
    }

    @SuppressLint("RestrictedApi")
    private fun stopCameraPreview() {
        if (::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
        }
        camera_preview?.gone()
        preview?.camera?.close()
        qrCodeAnalyzer.clear()
        imageAnalyzer?.clearAnalyzer()
    }

    @SuppressLint("RestrictedApi")
    private fun freezeCameraPreview() {
        if (::cameraProvider.isInitialized) {
            cameraProvider.unbindAll()
        }
        qr_code_overlay.gone()
        preview?.camera?.close()
        qrCodeAnalyzer.clear()
        imageAnalyzer?.clearAnalyzer()
    }

    override fun onQRCodeDetected(barcode: Barcode) {
        freezeCameraPreview()
        when (prefixFormat.isNullOrBlank()) {
            true -> sendResult(barcode.rawValue ?: "")
            false -> when (barcode.rawValue?.startsWith(prefixFormat ?: "") == true) {
                true -> sendResult(barcode.rawValue ?: "")
                false -> launch {
                    showToast(R.string.qr_code_invalid)
                    delay(3000)
                    startCameraPreview()
                }
            }
        }
    }

    private fun sendResult(data: String) {
        stopCameraPreview()
        setResult(RESULT_OK, Intent().apply {
            putExtra(KEY_QR, data)
        })
        finish()
    }

    private fun checkPermissionsAndStartCameraPreview() = requestPermission(Manifest.permission.CAMERA) { isGranted ->
        when {
            //User allow all permissions (camera) will be start camera preview for scanning qr code.
            isGranted -> startCameraPreview()
            //User denied access to camera.
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                showToast(R.string.camera_permission_denied)
                showOpenCameraButton()
            }
            //User denied and select don't ask again.
            else -> {
                showSettingPermissionInSettingDialog()
                showOpenCameraButton()
            }
        }
    }

    /**
     * Get the angle by which an image must be rotated given the device's current
     * orientation.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Throws(CameraAccessException::class)
    private fun getRotationCompensation(cameraId: String, activity: Activity, isFrontFacing: Boolean): Int {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        val deviceRotation = activity.windowManager.defaultDisplay.rotation
        var rotationCompensation = orientations.get(deviceRotation)

        // Get the device's sensor orientation.
        val cameraManager = activity.getSystemService(CAMERA_SERVICE) as CameraManager
        val sensorOrientation = cameraManager
                .getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        rotationCompensation = when (isFrontFacing) {
            true -> (sensorOrientation + rotationCompensation) % 360
            false -> (sensorOrientation - rotationCompensation + 360) % 360 // back-facing
        }
        return rotationCompensation
    }

    private fun checkPermissionAndGetImageContent() = requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE) { isGranted ->
        when {
            //User allow all permissions storage
            isGranted -> getContentHelper?.getImage { imageUri ->
                imageUri?.also {
                    //performUploadProfileImage(it)
                }
            }
            //User denied access to storage.
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> showToast(R.string.gallery_permission_denied)
            //User denied and select don't ask again.
            else -> showSettingPermissionInSettingDialog()
        }
    }

    private fun showOpenCameraButton() {
        stopCameraPreview()
        open_camera_button?.isClickable = true
        open_camera_button?.setBackgroundResource(R.drawable.bg_btn_write_small)
        camera_icon?.setImageDrawable(getDrawable(R.drawable.ic_camera, getColor(R.color.iconColorAccent)))
        open_camera_label?.text = getString(R.string.open_camera)
        open_camera_label?.setTextStyle(R.style.Text_BodyHeading_Secondary_OnLight)
    }

    private fun hideOpenCameraButton() {
        open_camera_button?.isClickable = false
        open_camera_button?.background = null
        camera_icon?.setImageDrawable(getDrawable(R.drawable.ic_qr_code, getColor(R.color.iconColorSecondary)))
        open_camera_label?.text = getString(R.string.scan_qr_code)
        open_camera_label?.setTextStyle(R.style.Text_BodyHeading_Secondary)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}