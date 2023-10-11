package me.tangobee.weathernaut.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import me.tangobee.weathernaut.data.repository.GeoLocationRepository
import me.tangobee.weathernaut.model.CityLocationData

class GeoLocationViewModel(private val repository: GeoLocationRepository) : ViewModel() {

    suspend fun getLocation(cityName: String, limit: Int, appID: String) {
        repository.getLocation(cityName, limit, appID)
    }

    val locationLiveData: LiveData<CityLocationData>
        get() = repository.locationLiveData

}