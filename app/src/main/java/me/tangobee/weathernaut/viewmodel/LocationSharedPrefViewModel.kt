package me.tangobee.weathernaut.viewmodel

import androidx.lifecycle.ViewModel
import me.tangobee.weathernaut.data.repository.LocationSharedPrefRepository
import me.tangobee.weathernaut.model.CurrentLocationData

class LocationSharedPrefViewModel(private val locationSharedPrefRepository: LocationSharedPrefRepository): ViewModel() {

    fun getData(): CurrentLocationData? {
        return locationSharedPrefRepository.getData()
    }

    fun sendData(locationData: CurrentLocationData) {
        locationSharedPrefRepository.sendData(locationData)
    }

}