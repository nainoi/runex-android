package com.think.runex.feature.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.content.edit
import com.google.android.gms.location.*
import com.jozzee.android.core.util.Logger
import com.jozzee.android.core.util.simpleName
import com.jozzee.android.core.view.showToast
import com.think.runex.util.AppPreference
import com.think.runex.util.launchMainThread

class LocationTracking(private val context: Context,
                       locationCallback: LocationCallback? = null) {

    companion object {
        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 3000

        /**
         * The fastest rate for active location updates. Updates will never be more frequent
         * than this value.
         */
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 1000

        private const val KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates"
    }

    /**
     * Sets the location request parameters.
     */
    private val locationRequest: LocationRequest by lazy {
        LocationRequest().apply {
            //TODO("Update Location Request interval and fast interval")
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    /**
     * Callback for changes in location.
     */
    private var locationCallback: LocationCallback? = null


    /**
     * Provides access to the Fused Location Provider API.
     */
    private var fusedLocationClient: FusedLocationProviderClient? = null
    //private var settingsClient: SettingsClient? = null
    //private var locationSettingsRequest: LocationSettingsRequest? = null

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        //settingsClient = LocationServices.getSettingsClient(context)
        this.locationCallback = locationCallback
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        Logger.info(simpleName(), "Requesting location updates")
        val settingsClient = LocationServices.getSettingsClient(context)
        val locationSettingsRequest = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .build()

        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener {
                    try {
                        //setRequestingLocationUpdate(true)
                        fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                    } catch (unlikely: SecurityException) {
                        //setRequestingLocationUpdate(false)
                        Logger.error(simpleName(), "Lost location permission. Could not request updates. $unlikely")
                        unlikely.printStackTrace()
                    }
                }
                .addOnFailureListener { error ->
                    LocationUtil.onCheckLocationSettingsError(context, error)
                }
    }

    fun removeLocationUpdates() {
        Logger.error(simpleName(), "Removing location updates")
        try {
            fusedLocationClient?.removeLocationUpdates(locationCallback)
            //setRequestingLocationUpdate(false)
        } catch (unlikely: SecurityException) {
            Logger.error(simpleName(), "Lost location permission. Could not remove updates. $unlikely")
            unlikely.printStackTrace()
        }
    }

//    private fun setRequestingLocationUpdate(isRequestingUpdate: Boolean) {
//        AppPreference.createPreference(context).edit {
//            putBoolean(KEY_REQUESTING_LOCATION_UPDATES, isRequestingUpdate)
//        }
//    }
//
//    fun isRequestingLocationUpdate(): Boolean {
//        return AppPreference.createPreference(context).getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
//    }
}