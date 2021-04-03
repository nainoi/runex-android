package com.think.runex.feature.workout.summary

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.drawToBitmap
import androidx.core.view.isVisible
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jozzee.android.core.permission.shouldShowPermissionRationale
import com.jozzee.android.core.resource.getColor
import com.jozzee.android.core.resource.getDimension
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.gone
import com.jozzee.android.core.view.showDialog
import com.jozzee.android.core.view.showToast
import com.jozzee.android.core.view.visible
import com.think.runex.BuildConfig
import com.think.runex.R
import com.think.runex.util.extension.*
import com.think.runex.config.KEY_DATA
import com.think.runex.feature.workout.data.WorkoutInfo
import com.think.runex.base.PermissionsLauncherBottomSheet
import com.think.runex.component.ImageSourcesDialog
import com.think.runex.feature.workout.MapPresenter
import com.think.runex.util.GetContentHelper
import com.think.runex.util.TakePictureHelper
import kotlinx.android.synthetic.main.bottom_sheet_share_workout.*
import kotlinx.android.synthetic.main.layout_workout_summary_on_map.*
import java.io.File
import java.io.FileOutputStream

class ShareWorkoutBottomSheet : PermissionsLauncherBottomSheet(), ImageSourcesDialog.OnSelectImageSourceListener {

    companion object {
        @JvmStatic
        fun newInstance(workoutInfo: WorkoutInfo?) = ShareWorkoutBottomSheet().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_DATA, workoutInfo)
            }
        }
    }

    private var mapPresenter: MapPresenter? = null
    private var takePictureHelper: TakePictureHelper? = null
    private var getContentHelper: GetContentHelper? = null

    private var workoutInfo: WorkoutInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetStyle)

        takePictureHelper = TakePictureHelper(this)
        getContentHelper = GetContentHelper(this)

        workoutInfo = arguments?.getParcelable(KEY_DATA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_share_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
        subscribeUi()
    }

    private fun setupComponents() {
        //Update bottom sheet show full height of layout.
        getBottomSheetBehavior()?.apply {
            isFitToContents = true
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        //Update workout detail views
        workoutInfo?.getDisplayData()?.also { displayDate ->
            distance_on_map_label?.text = displayDate.distances
            duration_on_map_label?.text = displayDate.duration
        }

        workout_time_on_map_label?.visible()
        workout_time_on_map_label?.text = workoutInfo?.getWorkoutDateTime()

        //Update location polyline to map
        initMaps {
            workoutInfo?.locations?.also {
                mapPresenter?.drawPolyline(it)
                mapPresenter?.zoomToFitWorkoutLine()
            }
        }
    }

    private fun subscribeUi() {
        close_button?.setOnClickListener {
            getBottomSheetBehavior()?.state = BottomSheetBehavior.STATE_HIDDEN
            //dismissAllowingStateLoss()
        }

        add_image_button?.setOnClickListener {
            showDialog(ImageSourcesDialog())
        }

        remove_image_button?.setOnClickListener {
            removeWorkoutImage()
        }

        share_to_facebook_button?.setOnClickListener {
            prepareMapLayoutForSaveImage {
                shareToFacebook()
            }
        }

        share_to_other_button?.setOnClickListener {
            prepareMapLayoutForSaveImage {
                val uri = saveWorkoutImageToStorage(true)
                clearMapSnapshotImage()
                //Stat share to other
                if (uri != null) {
                    shareToOther(uri)
                }
            }
        }

        save_button?.setOnClickListener {
            checkPermissionAndSaveImageToStorage()
        }
    }

    private fun initMaps(callbacks: () -> Unit) {
        if (mapPresenter != null) {
            callbacks.invoke()
            return
        }
        Logger.warning(simpleName(), "initialMaps...")
        (childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment)?.also { mapFragment ->
            mapFragment.getMapAsync { googleMap: GoogleMap ->
                googleMap.uiSettings?.isMyLocationButtonEnabled = false
                googleMap.uiSettings.setAllGesturesEnabled(false)
                mapPresenter = MapPresenter(googleMap, getColor(R.color.colorAccent), getDimension(R.dimen.space_8dp).toFloat())
                Logger.warning(simpleName(), "Setup mapPresenter")
                map_layout?.visible()
                callbacks.invoke()
            }
        }
    }

    override fun onSelectImageSource(source: Int) {
        when (source) {
            ImageSourcesDialog.SOURCE_CAMERA -> checkPermissionsAndTakePicture()
            ImageSourcesDialog.SOURCE_GALLERY -> checkPermissionAndGetImageContent()
        }
    }

    private fun checkPermissionsAndTakePicture() {
        val permissions = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permissions) { results ->
            when {
                //User allow all permissions (camera, storage) will be take picture
                allPermissionsGranted(results) -> takePictureHelper?.takePicture(requireContext()) { pictureUri ->
                    pictureUri?.also { addWorkoutImage(it) }
                }
                //User denied access to camera or storage.
                shouldShowPermissionRationale(permissions) -> showToast(R.string.camera_permission_denied)
                //User denied and select don't ask again.
                else -> requireContext().showSettingPermissionInSettingDialog()
            }
        }
    }

    private fun checkPermissionAndGetImageContent() = requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE) { isGranted ->
        when {
            //User allow all permissions storage
            isGranted -> getContentHelper?.getImage { imageUri ->
                imageUri?.also { addWorkoutImage(it) }
            }
            //User denied access to storage.
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> showToast(R.string.gallery_permission_denied)
            //User denied and select don't ask again.
            else -> requireContext().showSettingPermissionInSettingDialog()
        }
    }

    private fun checkPermissionAndSaveImageToStorage() {
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permissions) { results ->
            when {
                //User allow all permissions read and write storage
                allPermissionsGranted(results) -> prepareMapLayoutForSaveImage {
                    saveWorkoutImageToStorage()
                    clearMapSnapshotImage()
                }
                //User denied access storage.
                shouldShowPermissionRationale(permissions) -> showToast(R.string.storage_permission_denied)
                //User denied and select don't ask again.
                else -> requireContext().showSettingPermissionInSettingDialog()
            }
        }
    }

    private fun addWorkoutImage(uri: Uri) {
        workout_image?.loadImage(uri, ImageView.ScaleType.CENTER_CROP)
        workout_image?.visible()
        remove_image_button?.visible()
    }

    private fun removeWorkoutImage() {
        workout_image?.setImageDrawable(null)
        workout_image?.gone()
        remove_image_button?.gone()
    }

    /**
     *  Check have set custom image on map
     *  If [remove_image_button] isVisible = `false` it not set custom image
     *  will be snapshot map to bitmap and set to custom image before
     *  draw map layout to bitmap.
     */
    private fun prepareMapLayoutForSaveImage(callbacks: () -> Unit) {
        when (remove_image_button?.isVisible == false) {
            true -> mapPresenter?.snapshot { screedShot ->
                screedShot?.also { workout_image?.setImageBitmap(it) }
                callbacks.invoke()
            }
            false -> callbacks.invoke()
        }
    }

    private fun saveWorkoutImageToStorage(saveToCacheDirectory: Boolean = false): Uri? {

        val bitmap = map_layout?.drawToBitmap()
        val fileName = "${BuildConfig.APP_SCHEME}_workout_${workoutInfo?.getWorkoutDateTimeForImageName()}.jpg"
        var uri: Uri? = null

        when (saveToCacheDirectory) {
            true -> {
                val tempFile = File.createTempFile(fileName, ".jpg", requireContext().getTempDirectory("images"))
                uri = tempFile.getUriProvider(requireContext())
                bitmap?.writeToOutputStream(FileOutputStream(tempFile), Bitmap.CompressFormat.JPEG)
            }
            false -> uri = bitmap?.saveToExternalStorage(requireContext(), fileName)
        }

        //Clear bitmap
        bitmap?.recycle()

        if (saveToCacheDirectory.not()) {
            showSaveFileCompleteSnackBar(uri)
        }

        return uri
    }

    private fun shareToOther(uri: Uri) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/jpeg"
            //addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            //addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        }
        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.share))
        startActivity(shareIntent)
    }

    private fun shareToFacebook() {
        val bitmap = map_layout?.drawToBitmap()
        val content = SharePhotoContent.Builder()
                .addPhoto(SharePhoto.Builder().setBitmap(bitmap).build())
                .build()
        ShareDialog(this).apply {
            registerCallback(CallbackManager.Factory.create(), object : FacebookCallback<Sharer.Result> {
                override fun onSuccess(result: Sharer.Result?) {
                    showToast(R.string.success)
                    bitmap?.recycle()
                    clearMapSnapshotImage()
                }

                override fun onCancel() {
                    bitmap?.recycle()
                    clearMapSnapshotImage()
                }

                override fun onError(error: FacebookException?) {
                    bitmap?.recycle()
                    clearMapSnapshotImage()
                }
            })
        }.show(content)
    }

    /**
     * Clear map snapshot bitmap if not set workout image.
     */
    private fun clearMapSnapshotImage() {
        mapPresenter?.clearSnapshot()
        if (remove_image_button?.isVisible == false) {
            workout_image?.setImageDrawable(null)
        }
    }

    override fun onDestroy() {
        takePictureHelper?.unregisterTakePictureResult()
        takePictureHelper?.clear(requireContext())
        getContentHelper?.unregisterAllResult()
        mapPresenter?.clear()
        super.onDestroy()
    }
}