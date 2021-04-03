package com.think.runex.util.extension

import android.location.Location
import com.google.android.gms.maps.model.LatLng

fun Location.toLatLng() = LatLng(longitude, longitude)