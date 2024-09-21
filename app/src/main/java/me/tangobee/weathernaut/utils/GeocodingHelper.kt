package me.tangobee.weathernaut.utils

import me.tangobee.weathernaut.models.GeocodingData.GeocodingResult
import kotlin.math.*

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

        // Haversine formula to calculate distance between two lat/lng points in kilometers
        private fun calculateDistance(
            userLat: Double, userLng: Double,
            placeLat: Double, placeLng: Double
        ): Double {
            val earthRadiusKm = 6371.0

            val dLat = Math.toRadians(placeLat - userLat)
            val dLng = Math.toRadians(placeLng - userLng)

            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(Math.toRadians(userLat)) * cos(Math.toRadians(placeLat)) *
                    sin(dLng / 2) * sin(dLng / 2)

            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            return earthRadiusKm * c
        }

        fun sortLocationsByProximity(
            userLat: Double, userLng: Double,
            locationRVModelList: MutableList<GeocodingResult>
        ): List<GeocodingResult> {
            return locationRVModelList.sortedBy { location ->
                calculateDistance(userLat, userLng, location.latitude, location.longitude)
            }
        }
    }
}