package com.think.runex.ui.workout

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.SupportMapFragment
import com.google.android.libraries.maps.model.LatLng
import com.jozzee.android.core.permission.allPermissionsGranted
import com.jozzee.android.core.permission.havePermissions
import com.jozzee.android.core.permission.shouldShowPermissionRationale
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.showToast
import com.think.runex.R
import com.think.runex.common.setStatusBarColor
import com.think.runex.common.showAlertDialog
import com.think.runex.config.GOOGLE_MAP_DEFAULT_ZOOM
import com.think.runex.config.RC_OPEN_GPS
import com.think.runex.config.RC_PERMISSION_LOCATION
import com.think.runex.feature.location.LocationUtil
import com.think.runex.ui.base.BaseScreen
import com.think.runex.util.NightMode
import com.think.runex.util.launch
import kotlinx.coroutines.delay

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

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(hidden.not()){
            setStatusBarColor(isLightStatusBar = true)
        }
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
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = false
        getLastKnownLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        //Check location permissions before get last location
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (havePermissions(permissions, RC_PERMISSION_LOCATION)) {
            googleMap?.isMyLocationEnabled = true
            when (LocationUtil.isGpsEnabled(requireContext())) {
                true -> {
                    //TODO("May be update ui to detecting location")
                    LocationUtil.getLastKnownLocation(requireContext()) { location ->
                        if (location != null) {
                            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), GOOGLE_MAP_DEFAULT_ZOOM))
                        }
                    }
                }
                false -> LocationUtil.openGps(requireActivity())
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RC_PERMISSION_LOCATION -> when {
                //User allow to access location.
                allPermissionsGranted(grantResults) -> getLastKnownLocation()
                //User denied access to location.
                shouldShowPermissionRationale(permissions) -> showToast(R.string.location_permission_denied)
                //User tick Don't ask again.
                else -> showSettingPermissionInSettingDialog()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_OPEN_GPS -> if (resultCode == Activity.RESULT_OK) {
                launch {
                    delay(100)
                    getLastKnownLocation()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
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