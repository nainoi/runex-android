package com.think.runex.feature.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.jozzee.android.core.permission.havePermission
import com.jozzee.android.core.permission.havePermissions
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.showToast
import com.think.runex.config.RC_OPEN_GPS
import java.lang.Exception

object LocationUtil {

    fun isGpsEnabled(context: Context): Boolean {
        return (context.getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun openGps(activity: Activity, requestCode: Int = RC_OPEN_GPS) {

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        val locationRequestSetting = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build()

        LocationServices.getSettingsClient(activity).checkLocationSettings(locationRequestSetting).addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                // All location settings are satisfied. The client can initialize location
                // requests here.
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(activity, requestCode)
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                    }
                }
            }
        }
    }

    //@RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
    @SuppressLint("MissingPermission")
    fun getLastLocation(context: Context, onLocationFound: ((location: Location?) -> Unit)) {
        if (context.havePermission(Manifest.permission.ACCESS_FINE_LOCATION).not()) {
            Logger.error(simpleName(), "Location permissions denied")
            onLocationFound.invoke(null)
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                //Get last location normal case
                logFoundLocation(location)
                onLocationFound.invoke(location)
            } else {
                //If last location is null will request location update Once and remove location update
                val locationRequest = LocationRequest().apply {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    // Sets the desired interval for active location updates. This interval is
                    // inexact. You may not receive updates at all if no location sources are available, or
                    // you may receive them slower than requested. You may also receive updates faster than
                    // requested if other applications are requesting location at a faster interval.
                    interval = 5000 //5 seconds

                    // Sets the fastest rate for active location updates. This interval is exact, and your
                    // application will never receive updates faster than this value.
                    fastestInterval = 1000 //1 seconds
                }
                val locationSettingsRequest = LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest)
                        .build()

                LocationServices.getSettingsClient(context)
                        .checkLocationSettings(locationSettingsRequest)
                        .addOnSuccessListener {
                            fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                                override fun onLocationResult(result: LocationResult?) {
                                    super.onLocationResult(result)
                                    result?.lastLocation?.also { location ->
                                        fusedLocationClient?.removeLocationUpdates(this)
                                        logFoundLocation(location)
                                        onLocationFound.invoke(location)
                                    }
                                }
                            }, Looper.getMainLooper())
                        }
                        .addOnFailureListener { error ->
                            onCheckLocationSettingsError(context, error)
                            onLocationFound.invoke(null)
                        }
            }
        }
    }

    fun onCheckLocationSettingsError(context: Context, error: Exception) {
        if (error is ApiException) {
            when (error.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    context.showToast("Location settings are not satisfied. Attempting to upgrade location settings")
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    context.showToast("Location settings are inadequate, and cannot be fixed here. Fix in Settings.")
                }
            }
        }
    }

    private fun logFoundLocation(location: Location?) {
        Logger.debug(simpleName(), "Found location: latitude: ${location?.latitude}, longitude: ${location?.longitude}")
    }
}