package com.think.runex.feature.workout

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.think.runex.config.GOOGLE_MAP_DEFAULT_ZOOM
import com.think.runex.feature.workout.data.WorkingOutLocation
import com.think.runex.feature.workout.db.WorkoutDataBase
import com.think.runex.util.extension.launch
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay

class MapPresenter(
    private var googleMap: GoogleMap?,
    @ColorInt private var colorLine: Int = Color.BLACK,
    @Dimension private var widthLine: Float = 10f
) {

    private var points: ArrayList<WorkingOutLocation>? = null
    private var lastPolyline: Polyline? = null
    private var snapshot: Bitmap? = null

    @SuppressLint("MissingPermission")
    fun setMyLocationEnabled(isEnabled: Boolean) {
        googleMap?.isMyLocationEnabled = isEnabled
    }

    fun animateCamera(latLng: LatLng, zoom: Float = GOOGLE_MAP_DEFAULT_ZOOM) {
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    fun moveCamera(latLng: LatLng, zoom: Float = GOOGLE_MAP_DEFAULT_ZOOM) {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom))
    }

    /**
     * Add location and draw polyline to map
     * In first time [points] is null will be get temp location from
     * Realm data base adn redraw polyline
     */
    fun addPolyline(context: Context?, location: WorkingOutLocation) {
        if (points == null && context != null) {
            points = ArrayList(WorkoutDataBase.getDatabase(context).locationDao().getLocations())
        }
        points?.add(location)
        drawPolyline()
    }

    fun drawPolylineFromDatabase(context: Context/*startTimeMillis: Long*/) {
        points?.clear()
        points = ArrayList(WorkoutDataBase.getDatabase(context).locationDao().getLocations())
        drawPolyline()
    }

    private fun drawPolyline() {
        val polyline = googleMap?.addPolyline(PolylineOptions().apply {
            width(widthLine)
            this@MapPresenter.points?.forEach {
                add(it.toLatLng())
            }
        })
        polyline?.color = colorLine
        lastPolyline?.remove()
        lastPolyline = polyline
    }

    fun drawPolyline(points: List<WorkingOutLocation>) {
        this.points = ArrayList(points)
        drawPolyline()
    }

    fun zoomToFitWorkoutLine() {
        if (points.isNullOrEmpty()) return

        val bounds = LatLngBounds.Builder().apply {
            points?.forEach { location ->
                include(location.toLatLng())
            }
        }.build()

        //googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 65))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100), 500, null)
        launch(Main) {
            delay(1000)
            val zoom = (googleMap?.cameraPosition?.zoom ?: GOOGLE_MAP_DEFAULT_ZOOM) - 0.5f
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.center, zoom))
        }
    }

    /**
     * Get map ui as bitmap
     */
    fun snapshot(callback: (snapshot: Bitmap?) -> Unit) {
        googleMap?.snapshot {
            snapshot = it
            callback.invoke(snapshot)
        }
    }

    /**
     * Get polyline as bitmap without map ui
     */
    fun snapshotPolyline(mapFragment: SupportMapFragment, callback: GoogleMap.SnapshotReadyCallback) {
        //TODO("Handle in a feature!! ถถถถ")
    }

    /**
     * Clear polyline on map
     */
    fun clearPolyline() {
        lastPolyline?.remove()
        lastPolyline = null
        points?.clear()
        points = null
    }

    fun clearSnapshot() {
        snapshot?.recycle()
    }

    fun clear() {
        clearPolyline()
        clearSnapshot()
    }

}