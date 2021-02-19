package com.think.runex.ui.workout.record

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.GoogleMap
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.LatLngBounds
import com.google.android.libraries.maps.model.Polyline
import com.google.android.libraries.maps.model.PolylineOptions
import com.think.runex.config.GOOGLE_MAP_DEFAULT_ZOOM
import com.think.runex.feature.workout.model.WorkingOutLocation
import com.think.runex.util.launchMainThread
import io.realm.Realm
import kotlinx.coroutines.delay

class MapPresenter(private var googleMap: GoogleMap?,
                   @ColorInt private var colorLine: Int = Color.BLACK,
                   @Dimension private var widthLine: Float = 10f) {

    private var points: ArrayList<WorkingOutLocation>? = null
    private var lastPolyline: Polyline? = null

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
    fun addPolyline(location: WorkingOutLocation) {
        if (points == null) {
            Realm.getDefaultInstance().run {
                points = ArrayList(copyFromRealm(where(WorkingOutLocation::class.java).findAllAsync())
                        ?: emptyList())
            }
        }
        points?.add(location)
        drawPolyline()
    }

    fun drawPolylineFromDatabase(/*startTimeMillis: Long*/) {
        points?.clear()
        Realm.getDefaultInstance().run {
            points = ArrayList(copyFromRealm(where(WorkingOutLocation::class.java).findAllAsync())
                    ?: emptyList())
        }
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

    /**
     * Clear polyline on map
     */
    fun clearPolyline() {
        lastPolyline?.remove()
        lastPolyline = null
        points?.clear()
        points = null
    }

    fun zoomToFitWorkoutLine() {
        if (points?.isNullOrEmpty() == true) return

        val bounds = LatLngBounds.Builder().apply {
            points?.forEach { location ->
                include(location.toLatLng())
            }
        }.build()

        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 65))
        launchMainThread {
            delay(900)
            val zoom = (googleMap?.cameraPosition?.zoom ?: GOOGLE_MAP_DEFAULT_ZOOM) - 0.5f
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(bounds.center, zoom))
        }
    }
}