package com.think.runex.ui.workout

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.jozzee.android.core.permission.allPermissionsGranted
import com.jozzee.android.core.permission.havePermissions
import com.jozzee.android.core.permission.shouldShowPermissionRationale
import com.jozzee.android.core.view.showToast
import com.think.runex.R
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.showAlertDialog
import com.think.runex.config.RC_PERMISSION_LOCATION
import com.think.runex.feature.location.LocationHelper
import com.think.runex.ui.base.BaseScreen

class WorkoutScreen : BaseScreen() {

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.screen_workout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupComponents()
    }

    private fun setupComponents() {
        setStatusBarColor(isLightStatusBar = true)
        initGoogleMap()
    }

    private fun initGoogleMap() {
        (childFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment)?.also { mapFragment: SupportMapFragment ->
            mapFragment.getMapAsync { googleMap: GoogleMap ->
                onMapReady(googleMap)
            }
        }
    }

    private fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        getLastLocation()
    }

    private fun getLastLocation() {
        //Check location permissions before get last location
        if (havePermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), RC_PERMISSION_LOCATION)) {
            if (LocationHelper.isGpsEnabled(requireContext())) {

            }else{
                LocationHelper.openGps(requireActivity())
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RC_PERMISSION_LOCATION -> when {
                //User allow to access location.
                allPermissionsGranted(grantResults) -> getLastLocation()
                //User denied access to location.
                shouldShowPermissionRationale(permissions) -> showToast(R.string.location_permission_denied)
                //User tick Don't ask again.
                else -> showSettingPermissionInSettingDialog()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun showSettingPermissionInSettingDialog() = showAlertDialog(
            message = R.string.allow_permission_in_setting,
            positiveText = R.string.setting) {

        //On positive click, go to permission setting screen.
        val uri = Uri.parse("package:${requireContext().packageName}")
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}