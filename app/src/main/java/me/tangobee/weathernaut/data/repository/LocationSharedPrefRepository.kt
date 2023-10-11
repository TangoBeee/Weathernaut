package me.tangobee.weathernaut.data.repository

import me.tangobee.weathernaut.data.local.LocationSharedPrefService
import me.tangobee.weathernaut.model.CurrentLocationData

class LocationSharedPrefRepository(private val locationSharedPrefService: LocationSharedPrefService) {

    fun getData(): CurrentLocationData? {
        return locationSharedPrefService.getData()
    }

    fun sendData(locationData: CurrentLocationData) {
        locationSharedPrefService.sendData(locationData)
    }

}