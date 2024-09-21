package me.tangobee.weathernaut.utils

import kotlin.math.abs

class GeocodingHelper {
    companion object {
        fun areLocationsSame(
            lat1: Double,
            lon1: Double,
            lat2: Double,
            lon2: Double,
            threshold: Double = 0.01
        ): Boolean {
            return abs(lat1 - lat2) < threshold && abs(lon1 - lon2) < threshold
        }
    }
}