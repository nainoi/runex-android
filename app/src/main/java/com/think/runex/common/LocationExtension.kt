package com.think.runex.common

import android.location.Location
import com.google.android.libraries.maps.model.LatLng

fun Location.toLatLng() = LatLng(longitude, longitude)