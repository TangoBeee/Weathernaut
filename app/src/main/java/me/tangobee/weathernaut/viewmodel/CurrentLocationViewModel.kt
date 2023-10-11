package me.tangobee.weathernaut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import me.tangobee.weathernaut.data.repository.CurrentLocationRepository
import me.tangobee.weathernaut.model.CurrentLocationData

class CurrentLocationViewModel(private val currentLocationRepository: CurrentLocationRepository) : ViewModel() {
    suspend fun getLocation() {
        currentLocationRepository.getLocation()
    }

    val approximateLocationLiveData: LiveData<CurrentLocationData>
        get() = currentLocationRepository.locationLiveData
}